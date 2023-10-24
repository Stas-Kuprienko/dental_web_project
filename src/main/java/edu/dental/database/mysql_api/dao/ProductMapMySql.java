package edu.dental.database.mysql_api.dao;

import edu.dental.database.DatabaseException;
import edu.dental.database.TableInitializer;
import edu.dental.database.dao.ProductMapDAO;
import edu.dental.domain.entities.User;
import edu.dental.domain.records.ProductMap;
import edu.dental.domain.records.my_work_record_book.MyProductMap;
import edu.dental.utils.data_structures.MyList;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.Iterator;

public class ProductMapMySql implements ProductMapDAO {

    public static final String FIELDS = "id, user_id, title, price";

    public final String TABLE = TableInitializer.PRODUCT_MAP;

    private final User user;

    public ProductMapMySql(User user) {
        this.user = user;
    }

    @Override
    public boolean putAll(Collection<ProductMap.Item> list) throws DatabaseException {
        if (list == null || list.isEmpty()) {
            throw new DatabaseException("The  given argument is null or empty.");
        }
        try (Request request = new Request()) {
            Statement statement = request.getStatement();
            for (ProductMap.Item entry : list) {
                String values = String.format("DEFAULT, %s, '%s', %s", user.getId(), entry.getKey(), entry.getValue());
                String query = String.format(MySqlSamples.INSERT_BATCH.QUERY, TABLE, values);
                statement.addBatch(query);
            }
            statement.executeBatch();
            ResultSet resultSet = statement.getGeneratedKeys();
            setId(resultSet, list);
            return true;
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public boolean put(ProductMap.Item entry) throws DatabaseException {
        String injections = "?, ".repeat(FIELDS.split(", ").length - 1);
        injections = "DEFAULT, " + injections.substring(0, injections.length() - 2);
        String query = String.format(MySqlSamples.INSERT.QUERY, TABLE, FIELDS, injections);
        try (Request request = new Request(query)) {
            byte i = 1;
            PreparedStatement statement = request.getPreparedStatement();
            statement.setInt(i++, user.getId());
            statement.setString(i++, entry.getKey());
            statement.setInt(i, entry.getValue());
            statement.executeUpdate();
            return request.setID(entry);
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public Collection<ProductMap.Item> getAll() throws DatabaseException {
        String query = String.format(MySqlSamples.SELECT_WHERE.QUERY, "*", TABLE, "user_id = ?");
        MyList<ProductMap.Item> list;
        try (Request request = new Request(query)) {
            request.getPreparedStatement().setInt(1, user.getId());
            ResultSet resultSet = request.getPreparedStatement().executeQuery();
            list = (MyList<ProductMap.Item>) new ProductMapInstantiation(resultSet).build();
            return list;
        } catch (SQLException e) {
            //TODO loggers
            throw new DatabaseException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public ProductMap.Item get(int id) throws DatabaseException {
        String query = String.format(MySqlSamples.SELECT_WHERE.QUERY, "*", TABLE, "id = ?");
        MyList<ProductMap.Item> list;
        try (Request request = new Request(query)) {
            request.getPreparedStatement().setInt(1, id);
            ResultSet resultSet = request.getPreparedStatement().executeQuery();
            list = (MyList<ProductMap.Item>) new ProductMapInstantiation(resultSet).build();
            return list.get(0);
        } catch (SQLException | NullPointerException e) {
            throw new DatabaseException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public Collection<ProductMap.Item> search(Object... args) throws DatabaseException {
        String where = "user_id = ? AND title = ?";
        String query = String.format(MySqlSamples.SELECT_WHERE.QUERY, "*", TABLE, where);
        try (Request request = new Request(query)) {
            request.getPreparedStatement().setInt(1, user.getId());
            request.getPreparedStatement().setString(2, (String) args[0]);
            ResultSet resultSet = request.getPreparedStatement().executeQuery();
            return new ProductMapInstantiation(resultSet).build();
        } catch (SQLException | NullPointerException e) {
            //TODO
            throw new DatabaseException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public boolean edit(ProductMap.Item object) throws DatabaseException {
        String sets = "price = ?, title = ?";
        String where = "id = ? AND user_id = ?";
        String query = String.format(MySqlSamples.UPDATE.QUERY, TABLE, sets, where);
        try (Request request = new Request(query)) {
            PreparedStatement statement = request.getPreparedStatement();
            statement.setInt(1, object.getValue());
            statement.setString(2, object.getKey());
            statement.setInt(3, object.getId());
            statement.setInt(4, user.getId());
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

    private void setId(ResultSet resultSet, Collection<ProductMap.Item> items) throws SQLException {
        Iterator<ProductMap.Item> iterator = items.iterator();
        while (resultSet.next()) {
            iterator.next().setId((int) resultSet.getLong(1));
        }
    }


    protected static class ProductMapInstantiation implements Instantiating<ProductMap.Item> {

        private final MyList<ProductMap.Item> items;
        private final ResultSet resultSet;

        public ProductMapInstantiation(ResultSet resultSet) {
            this.resultSet = resultSet;
            items = new MyList<>();
        }

        @Override
        public Collection<ProductMap.Item> build() throws SQLException, DatabaseException {
            try (resultSet) {
                Constructor<MyProductMap.Item> constructor = MyProductMap.Item
                        .class.getDeclaredConstructor(String.class, int.class);
                constructor.setAccessible(true);
                while (resultSet.next()) {
                    MyProductMap.Item item = constructor.newInstance(resultSet.getString("title")
                                                                        , resultSet.getInt("price"));
                    item.setId(resultSet.getInt("id"));
                    items.add(item);
                }
                constructor.setAccessible(false);
                return items;
            } catch (NoSuchMethodException | IllegalAccessException
                     | InvocationTargetException | InstantiationException e) {
                //TODO loggers
                throw new DatabaseException(e.getMessage(), e);
            }
        }
    }
}
