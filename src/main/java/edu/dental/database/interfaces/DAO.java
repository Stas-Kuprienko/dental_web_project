package edu.dental.database.interfaces;

import edu.dental.database.DatabaseException;
import edu.dental.database.connection.ConnectionPool;
import edu.dental.domain.entities.IDHaving;
import edu.dental.domain.entities.User;

import java.io.IOException;
import java.sql.*;
import java.util.Collection;

public interface DAO<T> {

    boolean put(T object) throws DatabaseException;

    Collection<T> getAll() throws DatabaseException;

    T get(int id) throws DatabaseException;

    Collection<T> search(Object... args) throws DatabaseException;

    boolean edit(T object) throws DatabaseException;

    boolean delete(int id) throws DatabaseException;


    interface Queries {}


    class Request implements IRequest {

        private Connection connection;

        private final PreparedStatement statement;

        //TODO!!!!
        private final Statement state;

        public Request(String query) throws SQLException {
            this.connection = ConnectionPool.get();
            try {
                this.statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                this.state = null;
            } catch (SQLException e) {
                //TODO loggers
                ConnectionPool.put(connection);
                throw e;
            }
        }

        public Request() throws SQLException {
            this.connection = ConnectionPool.get();
            try {
                this.state = connection.createStatement();
                this.statement = null;
            } catch (SQLException e) {
                //TODO !!!
                ConnectionPool.put(connection);
                throw e;
            }
        }

        @Override
        public boolean setID(IDHaving object) throws SQLException {
            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    object.setId((int) resultSet.getLong(1));
                    return true;
                } else return false;
            }
        }

        @Override
        public Blob createBlob() throws SQLException {
            return connection.createBlob();
        }

        @Override
        public PreparedStatement getStatement() {
            return statement;
        }

        public Statement getState() {
            return state;
        }

        @Override
        public void close() {
            try {
                if (statement != null) {
                    statement.close();
                } else {
                    state.close();
                }
            } catch (SQLException e) {
                //TODO logger
                e.printStackTrace();
            } finally {
                ConnectionPool.put(connection);
                connection = null;
            }
        }
    }

    interface Instantiating<T> {

        Collection<T> build() throws SQLException, IOException, DatabaseException;

    }

}
