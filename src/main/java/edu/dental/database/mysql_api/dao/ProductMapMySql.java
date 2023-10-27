package edu.dental.database.mysql_api.dao;

import edu.dental.database.DatabaseException;
import edu.dental.database.TableInitializer;
import edu.dental.database.dao.ProductMapDAO;
import edu.dental.domain.entities.User;
import edu.dental.domain.records.ProductMap;
import edu.dental.domain.records.RecordManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;

public class ProductMapMySql implements ProductMapDAO {

    public static final String FIELDS = "id, user_id, title, price";

    public final String TABLE = TableInitializer.PRODUCT_MAP;

    private final User user;

    public ProductMapMySql(User user) {
        this.user = user;
    }

    @Override
    public boolean putAll(Map<String, Integer> map) throws DatabaseException {
        if (map == null || map.isEmpty()) {
            throw new DatabaseException("The  given argument is null or empty.");
        }
        ProductMap productMap = (ProductMap) map;
        String values = "DEFAULT, " + user.getId() + ", ?, ?";
        String query = String.format(MySqlSamples.INSERT_BATCH.QUERY, TABLE, values);
        try(Request request = new Request(query)) {
            PreparedStatement statement = request.getPreparedStatement();
            Iterator<ProductMap.Item> iterator = productMap.iterator();
            while (iterator.hasNext()) {
                ProductMap.Item entry = iterator.next();
                statement.setInt(1, entry.getId());
                statement.setInt(2, entry.getValue());
                statement.addBatch();
            }
            statement.executeBatch();
            ResultSet resultSet = statement.getGeneratedKeys();
            setId(resultSet, productMap);
            return true;
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public int put(String key, int value) throws DatabaseException {
        String values = "DEFAULT, " + user.getId() + ", ?, ?";
        String query = String.format(MySqlSamples.INSERT.QUERY, TABLE, FIELDS, values);
        try (Request request = new Request(query)) {
            PreparedStatement statement = request.getPreparedStatement();
            statement.setString(1, key);
            statement.setInt(2, value);
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            resultSet.next();
            return (int) resultSet.getLong(1);
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public Map<String, Integer> get() throws DatabaseException {
        //TODO
        String query = String.format(MySqlSamples.SELECT_WHERE.QUERY, "*", TABLE, "user_id = ?");
        ProductMap productMap = RecordManager.getProductMap();
        try (Request request = new Request(query)) {
            request.getPreparedStatement().setInt(1, user.getId());
            ResultSet resultSet = request.getPreparedStatement().executeQuery();
            while (resultSet.next()) {

            }
            return null;
        } catch (SQLException | NullPointerException e) {
            throw new DatabaseException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public boolean edit(int id, int value) throws DatabaseException {
        //TODO
        String sets = "price = ?, title = ?";
        String where = "id = ? AND user_id = ?";
        String query = String.format(MySqlSamples.UPDATE.QUERY, TABLE, sets, where);
        try (Request request = new Request(query)) {
            PreparedStatement statement = request.getPreparedStatement();
            return request.getPreparedStatement().executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public boolean delete(int id) throws DatabaseException {
        String query = String.format(MySqlSamples.DELETE.QUERY, TABLE, "id = ? AND user_id = ?");
        try (Request request = new Request(query)) {
            request.getPreparedStatement().setInt(1, id);
            request.getPreparedStatement().setInt(2, user.getId());
            return request.getPreparedStatement().executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(), e.getCause());
        }
    }

    public boolean delete(String title, int price) throws DatabaseException {
        String where = "title = ? AND price = ? AND user_id = ?";
        String query = String.format(MySqlSamples.DELETE.QUERY, TABLE, where);
        try (Request request = new Request(query)) {
            request.getPreparedStatement().setString(1, title);
            request.getPreparedStatement().setInt(2, price);
            request.getPreparedStatement().setInt(3, user.getId());
            return request.getPreparedStatement().executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(), e.getCause());
        }
    }

    private void setId(ResultSet resultSet, ProductMap map) throws SQLException {
        Iterator<ProductMap.Item> iterator = map.iterator();
        while (resultSet.next()) {
            iterator.next().setId((int) resultSet.getLong(1));
        }
    }
}