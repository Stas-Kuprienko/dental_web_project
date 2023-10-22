package edu.dental.database;

import edu.dental.database.connection.ConnectionPool;
import edu.dental.database.connection.DBConfiguration;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public interface TableInitializer {

    String USER = DBConfiguration.DATA_BASE + ".user";
    String REPORT = DBConfiguration.DATA_BASE + ".report";
    String PRODUCT_MAP = DBConfiguration.DATA_BASE + ".product_map";
    String DENTAL_WORK = DBConfiguration.DATA_BASE + ".dental_work";
    String PRODUCT = DBConfiguration.DATA_BASE + ".product";

    void init();

    class Request extends Thread implements AutoCloseable {

        private Connection connection;
        private final Statement statement;
        private final String[] queries;

        public Request(String... queries) throws SQLException {
            this.connection = ConnectionPool.get();
            this.queries = queries;
            try {
                this.statement = connection.createStatement();
            } catch (SQLException e) {
                //TODO loggers
                ConnectionPool.put(connection);
                throw e;
            }
        }

        @Override
        public void run() {
            try {
                for (String s : queries) {
                    statement.addBatch(s);
                }
                statement.executeBatch();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
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
