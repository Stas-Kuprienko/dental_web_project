package edu.dental.database.mysql_api;

import edu.dental.database.DatabaseException;
import edu.dental.database.connection.ConnectionPool;
import edu.dental.database.dao.DAO;
import edu.dental.entities.IDHaving;

import java.sql.*;

public interface MySQL_DAO extends DAO {

    class Request implements DAO.DatabaseRequest {

        private final Connection connection;

        private final PreparedStatement preparedStatement;

        private final Statement statement;

        public Request(String query) throws DatabaseException {
            this.connection = ConnectionPool.get();
            try {
                this.preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                this.statement = null;
            } catch (SQLException e) {
                ConnectionPool.put(connection);
                throw new DatabaseException(e);
            }
        }

        public Request() throws DatabaseException {
            this.connection = ConnectionPool.get();
            try {
                this.statement = connection.createStatement();
                this.preparedStatement = null;
            } catch (SQLException e) {
                ConnectionPool.put(connection);
                throw new DatabaseException(e);
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
                DatabaseException.logging(e);
            } finally {
                ConnectionPool.put(connection);
            }
        }
    }
}
