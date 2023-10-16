package edu.dental.database.mysql_api.dao;

import edu.dental.database.DatabaseException;
import edu.dental.database.TableInitializer;
import edu.dental.database.dao.DAO;
import edu.dental.database.dao.ProductDAO;
import edu.dental.domain.entities.Product;
import edu.dental.utils.data_structures.MyList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;

public class ProductMySql implements ProductDAO {

    public final String TABLE = TableInitializer.PRODUCT;
    private final int workId;

    private static final String FIELDS = "work_id, title, quantity, price";

    public ProductMySql(int workId) {
        this.workId = workId;
    }

    @Override
    public boolean putAll(Collection<Product> list) throws DatabaseException{
        if (list == null || list.isEmpty()) {
            throw new DatabaseException("The  given argument is null or empty.");
        }
        try (Request request = new Request()) {
            Statement statement = request.getStatement();
            String valuesFormat = "%s, '%s', %s, %s";
            for (Product p : list) {
                String values = String.format(valuesFormat, workId, p.title(), p.quantity(), p.price());
                String query = String.format(MySqlSamples.INSERT_BATCH.QUERY, TABLE, values);
                statement.addBatch(query);
            }
            return statement.executeBatch().length == list.size();
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public boolean put(Product product) throws DatabaseException {
        String injections = "?, ".repeat(FIELDS.split(", ").length);
        injections = injections.substring(0, injections.length() - 2);
        String query = String.format(MySqlSamples.INSERT.QUERY, TABLE, FIELDS, injections);
        try (Request request = new Request(query)) {
            PreparedStatement statement = request.getPreparedStatement();
            statement.setInt(1, workId);
            statement.setString(2, product.title());
            statement.setByte(3, product.quantity());
            statement.setInt(4, product.price());
            return statement.execute();
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public Collection<Product> instantiate(ResultSet resultSet) throws DatabaseException {
        try {
            MyList<Product> products = new MyList<>();
            String[] title = resultSet.getString("title").split(",");
            String[] quantity = resultSet.getString("quantity").split(",");
            String[] price = resultSet.getString("price").split(",");
            for (int i = 0; i < title.length; i++) {
                Product product = new Product(title[i],
                        (byte) Integer.parseInt(quantity[i]), Integer.parseInt(price[i]));
                products.add(product);
            }
            return products;
        } catch (SQLException | NumberFormatException e) {
            //TODO loggers
            throw new DatabaseException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public Collection<Product> getAll() throws DatabaseException {
        String query = String.format(MySqlSamples.SELECT_WHERE.QUERY, "*", TABLE, "work_id = ?");
        MyList<Product> products;
        try (Request request = new Request(query)) {
            request.getPreparedStatement().setInt(1, workId);
            ResultSet resultSet = request.getPreparedStatement().executeQuery();
            products = (MyList<Product>) new ProductInstantiation(resultSet).build();
        } catch (SQLException e) {
            //TODO loggers
            throw new DatabaseException(e.getMessage(), e.getCause());
        }
        return products;
    }

    @Override
    public Collection<Product> search(String title, int quantity) throws DatabaseException {
        String where = "work_id = ? AND title = ? AND quantity = ?";
        String query = String.format(MySqlSamples.SELECT_WHERE.QUERY, "*", TABLE, where);
        try (Request request = new Request(query)) {
            request.getPreparedStatement().setInt(1, workId);
            request.getPreparedStatement().setString(2, title);
            request.getPreparedStatement().setInt(3, quantity);
            ResultSet resultSet = request.getPreparedStatement().executeQuery();
            return new ProductInstantiation(resultSet).build();
        } catch (SQLException | NullPointerException e) {
            throw new DatabaseException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public boolean overwrite(Collection<Product> list) throws DatabaseException {
        String delete = String.format(MySqlSamples.DELETE.QUERY, TABLE, "work_id = " + workId);
        String valuesFormat = "%s, '%s', %s, %s";
        try (Request request = new Request()){
            Statement statement = request.getStatement();
            statement.addBatch(delete);
            for (Product p : list) {
                String values = String.format(valuesFormat, workId, p.title(), p.quantity(), p.price());
                String query = String.format(MySqlSamples.INSERT_BATCH.QUERY, TABLE, values);
                statement.addBatch(query);
            }
            return statement.executeBatch().length == list.size();
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public boolean delete(String title) throws DatabaseException {
        String query = String.format(MySqlSamples.DELETE.QUERY, TABLE, "work_id = ? AND title = ?");
        try (Request request = new Request(query)) {
            request.getPreparedStatement().setInt(1, workId);
            request.getPreparedStatement().setString(2,title);
            return request.getPreparedStatement().execute();
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public boolean deleteAll() throws DatabaseException {
        String query = String.format(MySqlSamples.DELETE.QUERY, TABLE, "work_id = ?");
        try (Request request = new Request(query)) {
            request.getPreparedStatement().setInt(1, workId);
            return request.getPreparedStatement().execute();
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(), e.getCause());
        }
    }


    protected static class ProductInstantiation {

        private final MyList<Product> productsList;
        private final ResultSet resultSet;

        public ProductInstantiation(ResultSet resultSet) {
            this.resultSet = resultSet;
            this.productsList = new MyList<>();
        }

        public Collection<Product> build() throws SQLException {
            try (resultSet) {
                String[] fields = FIELDS.split(", ");
                while (resultSet.next()) {
                    byte i = 1;

                    Product p = new Product(
                            resultSet.getString(fields[i++]),
                            resultSet.getByte(fields[i++]),
                            resultSet.getInt(fields[i]));
                    productsList.add(p);
                }
            }
            return this.productsList;
        }
    }

    protected static class Request extends DAO.Request {
        public Request(String query) throws SQLException {
            super(query);
        }

        public Request() throws SQLException {
            super();
        }
    }
}
