package edu.dental.database.interfaces;

import edu.dental.database.connection.ConnectionPool;
import edu.dental.domain.entities.IDHaving;

import java.io.IOException;
import java.sql.*;
import java.util.Collection;

public interface DAO<T> {

    boolean put(T object) throws SQLException;

    Collection<T> getAll() throws SQLException;

    T get(int id) throws SQLException;

    T search(Object value1, Object value2) throws SQLException;

    boolean edit(T object);

    boolean delete(int id) throws SQLException;


    interface Queries {}


    class Request implements IRequest {

        private Connection connection;

        private final PreparedStatement statement;

        public Request(String query) throws SQLException {
            this.connection = ConnectionPool.get();
            try {
                this.statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            } catch (SQLException e) {
                ConnectionPool.put(connection);
                throw e;
            }
        }

        @Override
        public boolean setID(IDHaving object, Statement statement) throws SQLException {
            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    object.setId((int) resultSet.getLong(1));
                }
            } catch (SQLException e) {
                //TODO logger
                throw e;
            }
            return false;
        }

        @Override
        public Blob createBlob() throws SQLException {
            return connection.createBlob();
        }

        @Override
        public PreparedStatement getStatement() {
            return statement;
        }

        @Override
        public void close() {
            try {
                statement.close();
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

        Collection<T> build() throws SQLException, IOException;

    }

}
