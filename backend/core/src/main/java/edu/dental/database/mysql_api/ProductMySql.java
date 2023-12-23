package edu.dental.database.mysql_api;

import edu.dental.database.DatabaseException;
import edu.dental.database.TableInitializer;
import edu.dental.database.dao.ProductDAO;
import edu.dental.entities.Product;
import utils.collections.SimpleList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class ProductMySql implements ProductDAO {

    public final String TABLE = TableInitializer.PRODUCT;
    private final int workId;

    private static final String FIELDS = "work_id, title, quantity, price";

    ProductMySql(int workId) {
        this.workId = workId;
    }

    @Override
    public boolean putAll(List<Product> list) throws DatabaseException{
        if (list == null || list.isEmpty()) {
            throw new DatabaseException("The  given argument is null or empty.");
        }
        try (Request request = new Request()) {
            Statement statement = request.getStatement();
            String valuesFormat = "%s, %s, %s, %s";
            for (Product p : list) {
                String values = String.format(valuesFormat, workId, p.entryId(), p.quantity(), p.price());
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
            statement.setInt(2, product.entryId());
            statement.setByte(3, product.quantity());
            statement.setInt(4, product.price());
            return statement.execute();
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public List<Product> instantiate(ResultSet resultSet) throws DatabaseException {
        try {
            SimpleList<Product> products = new SimpleList<>();
            if (resultSet.getString("entry_id") != null) {
                String[] entryId = resultSet.getString("entry_id").split(",");
                String[] title = resultSet.getString("title").split(",");
                String[] quantity = resultSet.getString("quantity").split(",");
                String[] price = resultSet.getString("price").split(",");
                for (int i = 0; i < title.length; i++) {
                    Product product = new Product(Integer.parseInt(entryId[i]), title[i],
                            (byte) Integer.parseInt(quantity[i]), Integer.parseInt(price[i]));
                    products.add(product);
                }
            }
            return products;
        } catch (SQLException | NumberFormatException e) {
            //TODO loggers
            throw new DatabaseException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public List<Product> getAll() throws DatabaseException {
        String query = String.format(MySqlSamples.SELECT_WHERE.QUERY, "*", TABLE, "work_id = ?");
        SimpleList<Product> products;
        try (Request request = new Request(query)) {
            request.getPreparedStatement().setInt(1, workId);
            ResultSet resultSet = request.getPreparedStatement().executeQuery();
            products = (SimpleList<Product>) new ProductInstantiation(resultSet).build();
        } catch (SQLException e) {
            //TODO loggers
            throw new DatabaseException(e.getMessage(), e.getCause());
        }
        return products;
    }

    @Override
    public List<Product> search(String title, int quantity) throws DatabaseException {
        String query = MySqlSamples.SELECT_PRODUCT.QUERY;
        try (Request request = new Request(query)) {
            request.getPreparedStatement().setString(1, title);
            request.getPreparedStatement().setInt(2, workId);
            request.getPreparedStatement().setByte(3, (byte) quantity);
            ResultSet resultSet = request.getPreparedStatement().executeQuery();
            return new ProductInstantiation(resultSet).build();
        } catch (SQLException | NullPointerException e) {
            throw new DatabaseException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public boolean overwrite(List<Product> list) throws DatabaseException {
        String delete = String.format(MySqlSamples.DELETE.QUERY, TABLE, "work_id = " + workId);
        String injections = "?, ".repeat(FIELDS.split(", ").length - 1);
        injections = workId + ", " + injections.substring(0, injections.length()-2);
        String query = String.format(MySqlSamples.INSERT.QUERY, TABLE, FIELDS, injections);
        try (Request request = new Request(query)){
            PreparedStatement statement = request.getPreparedStatement();
            statement.addBatch(delete);
            for (Product p : list) {
                statement.setInt(1, p.entryId());
                statement.setByte(2, p.quantity());
                statement.setInt(3, p.price());
                statement.addBatch();
            }
            return statement.executeBatch().length == list.size() + 1;
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

        private final SimpleList<Product> productsList;
        private final ResultSet resultSet;

        public ProductInstantiation(ResultSet resultSet) {
            this.resultSet = resultSet;
            this.productsList = new SimpleList<>();
        }

        public List<Product> build() throws SQLException {
            try (resultSet) {
                while (resultSet.next()) {
                    Product p = new Product(
                            resultSet.getInt("entry_id"),
                            resultSet.getString("title"),
                            resultSet.getByte("quantity"),
                            resultSet.getInt("price"));
                    productsList.add(p);
                }
            }
            return this.productsList;
        }
    }
}
