package dental.database.dao;

import dental.domain.MyList;
import dental.database.ConnectionPool;

import java.lang.reflect.Field;
import java.sql.*;

public interface DAO<E> {

    boolean insert(E e) throws SQLException, NoSuchFieldException, IllegalAccessException;

    MyList<E> getAll() throws Exception;

    E get(int id) throws Exception;

    boolean remove(int id) throws SQLException;

    enum SQL_DAO {
        INSERT("INSERT INTO %s (%s) VALUES (%s);"),
        SELECT_ALL("SELECT %s FROM %s;"),
        SELECT_WHERE("SELECT %s FROM %s WHERE %s;"),
        DELETE("DELETE FROM %s WHERE %s;"),
        //TODO
        UPDATE("UPDATE %s SET %s = %s WHERE %s;");

        final String QUERY;

        SQL_DAO(String QUERY) {
            this.QUERY = QUERY;
        }
    }

    class DBRequest {
        private Connection connection;
        private PreparedStatement statement;

        protected DBRequest(String QUERY) {
            try {
                this.connection = ConnectionPool.get();
                this.statement = connection.prepareStatement(QUERY, Statement.RETURN_GENERATED_KEYS);
            } catch (SQLException e) {
                if (connection != null) {
                    ConnectionPool.put(connection);
                }
            }
        }

        protected void close() {
            ConnectionPool.put(this.connection);
            connection = null;
        }

        protected static boolean setID(Object object, Statement statement) throws SQLException, NoSuchFieldException, IllegalAccessException {
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                Field id = object.getClass().getDeclaredField("id");
                id.setAccessible(true);
                id.set(object, (int) resultSet.getLong(1));
                id.setAccessible(false);
                resultSet.close();
                return true;
            } else {
                return false;
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
