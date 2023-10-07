package edu.dental.database.mysql_api.dao;

import edu.dental.database.DatabaseException;
import edu.dental.database.connection.DBConfiguration;
import edu.dental.database.interfaces.DAO;
import edu.dental.domain.entities.Product;
import edu.dental.utils.data_structures.MyList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

public class ProductMySql implements DAO<Product> {

    public final String TABLE;
    private final int workId;

    private static final String FIELDS = "work_id, title, quantity, price";

    public ProductMySql(int userId, int workId) {
        this.TABLE = DBConfiguration.DATA_BASE + ".product_" + userId;
        this.workId = workId;
    }

    public boolean putAll(MyList<Product> products) throws DatabaseException{
        if (products == null || products.isEmpty()) {
            throw new DatabaseException("The  given argument is null or empty.");
        }
        try (Request request = new Request()) {
            PreparedStatement statement = request.getStatement();
            for (Product p : products) {
                String values = String.format("%s, %s, %s, %s", workId, p.title(), p.quantity(), p.price());
                String query = String.format(MySqlSamples.INSERT.QUERY, TABLE, FIELDS, values);
                statement.addBatch(query);
            }
            return statement.executeBatch().length == products.size();
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
            PreparedStatement statement = request.getStatement();
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
            request.getStatement().setInt(1, workId);
            ResultSet resultSet = request.getStatement().executeQuery();
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
            request.getStatement().setInt(1, id);
            ResultSet resultSet = request.getStatement().executeQuery();
            MyList<Product> list = (MyList<Product>) new ProductInstantiation(resultSet).build();
            return list.get(0);
        } catch (SQLException | NullPointerException e) {
            //TODO loggers
            throw new DatabaseException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public MyList<Product> search(Object value1, Object value2) throws DatabaseException {
        String where = "title = ? AND quantity = ?";
        String query = String.format(MySqlSamples.SELECT_WHERE.QUERY, "*", TABLE, where);
        try (Request request = new Request(query)) {
            request.getStatement().setString(1, (String) value1);
            request.getStatement().setInt(2, (Integer) value2);
            ResultSet resultSet = request.getStatement().executeQuery();
            return (MyList<Product>) new ProductInstantiation(resultSet).build();
        } catch (SQLException | ClassCastException e) {
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
            PreparedStatement statement = request.getStatement();
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
            request.getStatement().setInt(1, id);
            request.getStatement().setString(2,title);
            return request.getStatement().execute();
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public boolean delete(int id) throws DatabaseException {
        String query = String.format(MySqlSamples.DELETE.QUERY, TABLE, "work_id = ?");
        try (Request request = new Request(query)) {
            request.getStatement().setInt(1, id);
            return request.getStatement().execute();
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
