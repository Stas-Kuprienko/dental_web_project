package dental.database.dao;

import dental.domain.data_structures.MyList;
import dental.database.service.ConnectionPool;

import java.lang.reflect.Field;
import java.sql.*;

public interface DAO<E> {

    boolean insert(E e) throws Exception;

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
        private final PreparedStatement statement;

        protected DBRequest(String QUERY) throws SQLException {
            try {
                this.connection = ConnectionPool.get();
                this.statement = connection.prepareStatement(QUERY, Statement.RETURN_GENERATED_KEYS);
            } catch (SQLException e) {
                if (connection != null) {
                    ConnectionPool.put(connection);
                }
                throw new SQLException(e);
            }
        }

        protected void close() {
            try {
                statement.close();
            } catch (SQLException e) {
                //TODO
                e.printStackTrace();
            } finally {
                ConnectionPool.put(this.connection);
                connection = null;
            }
        }

        protected static boolean setID(Object object, Statement statement) throws NoSuchFieldException, IllegalAccessException, SQLException {
            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    Field id = object.getClass().getDeclaredField("id");
                    id.setAccessible(true);
                    id.set(object, (int) resultSet.getLong(1));
                    id.setAccessible(false);
                    return true;
                } else {
                    return false;
                }
            } catch (SQLException e) {
                throw new SQLException(e);
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
