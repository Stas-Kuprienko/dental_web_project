package dental.database.dao;

import dental.app.MyList;
import dental.database.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface DAO<E> {

    void add(E e) throws SQLException;

    MyList<E> getAll() throws Exception;

    E get(int id) throws Exception;

    void remove(int id) throws SQLException;
    void remove(E e) throws SQLException;


    class DBRequest {
        private Connection connection;
        private final PreparedStatement statement;

        protected DBRequest(String QUERY) throws SQLException {
            this.connection = ConnectionPool.get();
            this.statement = connection.prepareStatement(QUERY);
        }

        protected void close() {
            ConnectionPool.put(this.connection);
            connection = null;
        }

        public PreparedStatement getStatement() {
            return statement;
        }
    }

}
