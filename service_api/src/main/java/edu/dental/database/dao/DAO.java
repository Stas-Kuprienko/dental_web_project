package edu.dental.database.dao;

import edu.dental.database.DatabaseException;
import edu.dental.database.connection.ConnectionPool;
import edu.dental.domain.entities.IDHaving;

import java.io.IOException;
import java.sql.*;
import java.util.List;

public interface DAO<T> {

    boolean putAll(List<T> list) throws DatabaseException;

    boolean put(T object) throws DatabaseException;

    List<T> getAll() throws DatabaseException;

    T get(int id) throws DatabaseException;

    List<T> search(Object... args) throws DatabaseException;

    boolean edit(T object) throws DatabaseException;

    boolean delete(int id) throws DatabaseException;


    interface Queries {}


    class Request implements IRequest {

        private Connection connection;

        private final PreparedStatement preparedStatement;

        private final Statement statement;

        public Request(String query) throws SQLException {
            this.connection = ConnectionPool.get();
            try {
                this.preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                this.statement = null;
            } catch (SQLException e) {
                //TODO loggers
                ConnectionPool.put(connection);
                throw e;
            }
        }

        public Request() throws SQLException {
            this.connection = ConnectionPool.get();
            try {
                this.statement = connection.createStatement();
                this.preparedStatement = null;
            } catch (SQLException e) {
                //TODO loggers
                ConnectionPool.put(connection);
                throw e;
            }
        }

        @Override
        public boolean setID(IDHaving object) throws SQLException {
            try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
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
        public PreparedStatement getPreparedStatement() {
            return preparedStatement;
        }

        public Statement getStatement() {
            return statement;
        }

        @Override
        public void close() {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (statement != null) {
                    statement.close();
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

        List<T> build() throws SQLException, IOException, DatabaseException;

    }

}
