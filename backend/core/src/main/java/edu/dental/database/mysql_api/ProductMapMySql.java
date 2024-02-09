package edu.dental.database.mysql_api;

import edu.dental.database.DatabaseException;
import edu.dental.database.TableInitializer;
import edu.dental.database.dao.ProductMapDAO;
import edu.dental.domain.APIManager;
import edu.dental.entities.ProductMap;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;

public class ProductMapMySql implements ProductMapDAO, MySQL_DAO {

    public static final String FIELDS = "id, user_id, title, price";

    public final String TABLE = TableInitializer.PRODUCT_MAP;

    private final int userId;

    ProductMapMySql(int userId) {
        this.userId = userId;
    }

    @Override
    public boolean putAll(ProductMap map) throws DatabaseException, NullPointerException {
        if (map == null || map.isEmpty()) {
            throw new NullPointerException("The  given argument is null or empty.");
        }
        String values = "DEFAULT, " + userId + ", ?, ?";
        String query = String.format(MySqlSamples.INSERT_BATCH.QUERY, TABLE, values);
        try(Request request = new Request(query)) {
            PreparedStatement statement = request.getPreparedStatement();
            Iterator<ProductMap.Item> iterator = map.iterator();
            while (iterator.hasNext()) {
                ProductMap.Item entry = iterator.next();
                statement.setInt(1, entry.getId());
                statement.setInt(2, entry.getValue());
                statement.addBatch();
            }
            statement.executeBatch();
            ResultSet resultSet = statement.getGeneratedKeys();
            setId(resultSet, map);
            return true;
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public int put(String key, int value) throws DatabaseException {
        String values = "DEFAULT, " + userId + ", ?, ?";
        String query = String.format(MySqlSamples.INSERT.QUERY, TABLE, FIELDS, values);
        try (Request request = new Request(query)) {
            PreparedStatement statement = request.getPreparedStatement();
            statement.setString(1, key.toLowerCase());
            statement.setInt(2, value);
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            resultSet.next();
            return (int) resultSet.getLong(1);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public ProductMap get() throws DatabaseException, NullPointerException {
        String query = String.format(MySqlSamples.SELECT_WHERE.QUERY, "*", TABLE, "user_id = ?");
        ProductMap productMap = APIManager.INSTANCE.getProductMap();
        try (Request request = new Request(query)) {
            request.getPreparedStatement().setInt(1, userId);
            ResultSet resultSet = request.getPreparedStatement().executeQuery();
            while (resultSet.next()) {
                String title = resultSet.getString("title");
                int price = resultSet.getInt("price");
                int id = resultSet.getInt("id");
                productMap.put(title, price, id);
            }
            resultSet.close();
            return productMap;
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public boolean edit(int id, int value) throws DatabaseException {
        String set = "price = ?";
        String where = "id = ? AND user_id = ?";
        String query = String.format(MySqlSamples.UPDATE.QUERY, TABLE, set, where);
        try (Request request = new Request(query)) {
            PreparedStatement statement = request.getPreparedStatement();
            statement.setInt(1, value);
            statement.setInt(2, id);
            statement.setInt(3, userId);
            return request.getPreparedStatement().executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public boolean delete(int id) throws DatabaseException {
        String query = String.format(MySqlSamples.DELETE.QUERY, TABLE, "id = ? AND user_id = ?");
        try (Request request = new Request(query)) {
            PreparedStatement statement = request.getPreparedStatement();
            statement.setInt(1, id);
            statement.setInt(2, userId);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    public boolean delete(String title, int price) throws DatabaseException {
        String where = "title = ? AND price = ? AND user_id = ?";
        String query = String.format(MySqlSamples.DELETE.QUERY, TABLE, where);
        try (Request request = new Request(query)) {
            PreparedStatement statement = request.getPreparedStatement();
            statement.setString(1, title);
            statement.setInt(2, price);
            statement.setInt(3, userId);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    private void setId(ResultSet resultSet, ProductMap map) throws SQLException {
        Iterator<ProductMap.Item> iterator = map.iterator();
        while (resultSet.next()) {
            iterator.next().setId((int) resultSet.getLong(1));
        }
    }
}