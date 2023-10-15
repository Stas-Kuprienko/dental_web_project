package edu.dental.database.mysql_api.dao;

import edu.dental.database.DatabaseException;
import edu.dental.database.connection.DBConfiguration;
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

public class ProductMapMySql implements ProductMapDAO {

    public static final String FIELDS = "id, title, price";

    public final String TABLE;

    private final User user;

    public ProductMapMySql(User user) {
        this.user = user;
        this.TABLE = DBConfiguration.DATA_BASE + ".product_map_" + user.getId();
    }

    @Override
    public boolean putAll(Collection<ProductMap.Item> list) throws DatabaseException {
        if (list == null || list.isEmpty()) {
            throw new DatabaseException("The  given argument is null or empty.");
        }
        try (Request request = new Request()) {
            Statement statement = request.getStatement();
            for (java.util.Map.Entry<String, Integer> entry : list) {
                String values = String.format("DEFAULT, '%s', %s", entry.getKey(), entry.getValue());
                String query = String.format(MySqlSamples.INSERT_BATCH.QUERY, TABLE, values);
                statement.addBatch(query);
            }
            statement.executeBatch();
            ResultSet resultSet = statement.getGeneratedKeys();

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
        String query = String.format(MySqlSamples.SELECT_ALL.QUERY, "*", TABLE);
        MyList<ProductMap.Item> list;
        try (Request request = new Request(query)) {
            ResultSet resultSet = request.getPreparedStatement().executeQuery();
            list = (MyList<ProductMap.Item>) new ProductMapperInstantiation(resultSet).build();
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
            list = (MyList<ProductMap.Item>) new ProductMapperInstantiation(resultSet).build();
            return list.get(0);
        } catch (SQLException | NullPointerException e) {
            throw new DatabaseException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public Collection<ProductMap.Item> search(Object... args) throws DatabaseException {
        String where = "title = ?";
        String query = String.format(MySqlSamples.SELECT_WHERE.QUERY, "*", TABLE, where);
        try (Request request = new Request(query)) {
            request.getPreparedStatement().setString(1, (String) args[0]);
            ResultSet resultSet = request.getPreparedStatement().executeQuery();
            return new ProductMapperInstantiation(resultSet).build();
        } catch (SQLException | NullPointerException e) {
            //TODO
            throw new DatabaseException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public boolean edit(ProductMap.Item object) throws DatabaseException {
        String sets = "price = ?";
        String where = "title = ?";
        String query = String.format(MySqlSamples.UPDATE.QUERY, TABLE, sets, where);
        try (Request request = new Request(query)) {
            request.getPreparedStatement().setInt(1, object.getValue());
            request.getPreparedStatement().setString(2, object.getKey());
            return request.getPreparedStatement().execute();
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public boolean delete(int id) throws DatabaseException {
        String query = String.format(MySqlSamples.DELETE.QUERY, TABLE, "id = ?");
        try (Request request = new Request(query)) {
            request.getPreparedStatement().setInt(1, id);
            return request.getPreparedStatement().execute();
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(), e.getCause());
        }
    }

    public boolean delete(String title, int price) throws DatabaseException {
        String query = String.format(MySqlSamples.DELETE.QUERY, TABLE, "title = ? AND price = ?");
        try (Request request = new Request(query)) {
            request.getPreparedStatement().setString(1, title);
            request.getPreparedStatement().setInt(2, price);
            return request.getPreparedStatement().execute();
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(), e.getCause());
        }
    }


    protected static class ProductMapperInstantiation implements Instantiating<ProductMap.Item> {

        private final MyList<ProductMap.Item> items;
        private final ResultSet resultSet;

        public ProductMapperInstantiation(ResultSet resultSet) {
            this.resultSet = resultSet;
            items = new MyList<>();
        }

        @Override
        public Collection<ProductMap.Item> build() throws SQLException, DatabaseException {
            try (resultSet) {
                Constructor<MyProductMap.Item> constructor = MyProductMap.Item
                        .class.getDeclaredConstructor(String.class, int.class);
                while (resultSet.next()) {
                    MyProductMap.Item item = constructor.newInstance(resultSet.getString("title")
                                                                        , resultSet.getInt("price"));
                    items.add(item);
                }
                return items;
            } catch (NoSuchMethodException | IllegalAccessException
                     | InvocationTargetException | InstantiationException e) {
                //TODO loggers
                throw new DatabaseException(e.getMessage(), e.getCause());
            }
        }
    }
}
