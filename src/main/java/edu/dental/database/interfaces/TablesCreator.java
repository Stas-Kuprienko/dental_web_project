package edu.dental.database.interfaces;

import edu.dental.database.DatabaseException;
import edu.dental.database.connection.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface TablesCreator {

    void createProductMapTable(int userID) throws DatabaseException;

    void createWorkRecordTable(int userID) throws DatabaseException;

    void createWorkRecordTable(int userID, String yearMonth) throws DatabaseException;

    void createProductTable(int userID) throws DatabaseException;

    void createProductTable(int userID, String yearMonth) throws DatabaseException;

    class Request implements AutoCloseable {

        private Connection connection;
        private final PreparedStatement statement;

        public Request(String query) throws SQLException {
            this.connection = ConnectionPool.get();
            try {
                this.statement = connection.prepareStatement(query);
            } catch (SQLException e) {
                //TODO loggers
                ConnectionPool.put(connection);
                throw e;
            }
        }

        public boolean execute() throws SQLException {
            return statement.execute();
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
}
