package dental.database.dao;

import dental.app.MyList;
import dental.database.ConnectionPool;

import java.lang.reflect.Field;
import java.sql.*;

public interface DAO<E> {

    void add(E e) throws SQLException, NoSuchFieldException, IllegalAccessException;

    MyList<E> getAll() throws Exception;

    E get(int id) throws Exception;

    void remove(int id) throws SQLException;
    void remove(E e) throws SQLException;


    class DBRequest {
        private Connection connection;
        private final PreparedStatement statement;

        protected DBRequest(String QUERY) throws SQLException {
            this.connection = ConnectionPool.get();
            this.statement = connection.prepareStatement(QUERY, Statement.RETURN_GENERATED_KEYS);
        }

        protected void close() {
            ConnectionPool.put(this.connection);
            connection = null;
        }

        protected static void setID(Object object, Statement statement) throws SQLException, NoSuchFieldException, IllegalAccessException {
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                Field id = object.getClass().getDeclaredField("id");
                id.setAccessible(true);
                id.set(object, (int) resultSet.getLong(1));
                id.setAccessible(false);
                resultSet.close();
            }
        }

        protected Blob createBlob() throws SQLException {
            return connection.createBlob();
        }

        public PreparedStatement getStatement() {
            return statement;
        }
    }

}
