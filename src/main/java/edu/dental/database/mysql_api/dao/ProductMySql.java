package edu.dental.database.mysql_api.dao;

import edu.dental.database.DatabaseException;
import edu.dental.database.connection.DBConfiguration;
import edu.dental.database.interfaces.ProductDAO;
import edu.dental.domain.entities.Product;
import edu.dental.utils.data_structures.MyList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;

public class ProductMySql implements ProductDAO {

    public final String TABLE;
    private final int workId;

    private static final String FIELDS = "work_id, title, quantity, price";

    public ProductMySql(int userId, int workId) {
        this.TABLE = DBConfiguration.DATA_BASE + ".product_" + userId;
        this.workId = workId;
    }

    @Override
    public boolean putAll(Collection<Product> list) throws DatabaseException{
        if (list == null || list.isEmpty()) {
            throw new DatabaseException("The  given argument is null or empty.");
        }
        try (Request request = new Request()) {
            Statement statement = request.getStatement();
            for (Product p : list) {
                String values = String.format("%s, '%s', %s, %s", workId, p.title(), p.quantity(), p.price());
                String query = String.format(MySqlSamples.INSERT_BATCH.QUERY, TABLE, values);
                statement.addBatch(query);
            }
            return statement.executeBatch().length == list.size();
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public boolean put(Product object) throws DatabaseException {
        String injections = "?, ".repeat(FIELDS.split(", ").length);
        injections = injections.substring(0, injections.length() - 2);
        String query = String.format(MySqlSamples.INSERT.QUERY, TABLE, FIELDS, injections);
        try (Request request = new Request(query)) {
            byte i = 1;
            PreparedStatement statement = request.getPreparedStatement();
            statement.setInt(i++, workId);
            statement.setString(i++, object.title());
            statement.setByte(i++, object.quantity());
            statement.setInt(i, object.price());
            return statement.execute();
        } catch (SQLException e) {
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

    @Deprecated
    @Override
    public Product get(int id) throws DatabaseException {
        String query = String.format(MySqlSamples.SELECT_WHERE.QUERY, "*", TABLE, "work_id = ?");
        try (Request request = new Request(query)) {
            request.getPreparedStatement().setInt(1, id);
            ResultSet resultSet = request.getPreparedStatement().executeQuery();
            MyList<Product> list = (MyList<Product>) new ProductInstantiation(resultSet).build();
            return list.get(0);
        } catch (SQLException | NullPointerException e) {
            //TODO loggers
            throw new DatabaseException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public Collection<Product> search(Object... args) throws DatabaseException {
        String where = "title = ? AND quantity = ?";
        String query = String.format(MySqlSamples.SELECT_WHERE.QUERY, "*", TABLE, where);
        try (Request request = new Request(query)) {
            request.getPreparedStatement().setString(1, (String) args[0]);
            request.getPreparedStatement().setInt(2, (Integer) args[1]);
            ResultSet resultSet = request.getPreparedStatement().executeQuery();
            return new ProductInstantiation(resultSet).build();
        } catch (SQLException | NullPointerException e) {
            throw new DatabaseException(e.getMessage(), e.getCause());
        }
    }

    public boolean editAll(MyList<Product> products) throws DatabaseException {
        if (products == null || products.isEmpty()) {
            throw new DatabaseException("The  given argument is null or empty.");
        }
        boolean result = false;
        for (Product product : products) {
            result = edit(product);
        }
        return result;
    }

    @Override
    public boolean edit(Product object) throws DatabaseException {
        String sets = "quantity = ?, price = ?";
        String where = "work_id = ? AND title = ?";
        String query = String.format(MySqlSamples.UPDATE.QUERY, TABLE, sets, where);
        try (Request request = new Request(query)){
            byte i = 1;
            PreparedStatement statement = request.getPreparedStatement();
            statement.setByte(i++, object.quantity());
            statement.setInt(i++, object.price());
            statement.setInt(i++, workId);
            statement.setString(i, object.title());
            return statement.execute();
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(), e.getCause());
        }
    }

    public boolean delete(int id, String title) throws DatabaseException {
        String query = String.format(MySqlSamples.DELETE.QUERY, TABLE, "work_id = ? AND title = ?");
        try (Request request = new Request(query)) {
            request.getPreparedStatement().setInt(1, id);
            request.getPreparedStatement().setString(2,title);
            return request.getPreparedStatement().execute();
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public boolean delete(int id) throws DatabaseException {
        String query = String.format(MySqlSamples.DELETE.QUERY, TABLE, "work_id = ?");
        try (Request request = new Request(query)) {
            request.getPreparedStatement().setInt(1, id);
            return request.getPreparedStatement().execute();
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(), e.getCause());
        }
    }


    protected static class ProductInstantiation implements Instantiating<Product> {

        private final MyList<Product> productsList;
        private final ResultSet resultSet;

        public ProductInstantiation(ResultSet resultSet) {
            this.resultSet = resultSet;
            this.productsList = new MyList<>();
        }

        @Override
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
}
