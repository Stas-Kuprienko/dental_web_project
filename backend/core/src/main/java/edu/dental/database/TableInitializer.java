package edu.dental.database;

import edu.dental.database.connection.ConnectionPool;
import edu.dental.database.connection.DBConfiguration;
import edu.dental.domain.APIManager;
import stas.utilities.LoggerKit;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public interface TableInitializer {

    String USER = DBConfiguration.DATA_BASE + ".user";
    String REPORT = DBConfiguration.DATA_BASE + ".report";
    String PRODUCT_MAP = DBConfiguration.DATA_BASE + ".product_map";
    String DENTAL_WORK = DBConfiguration.DATA_BASE + ".dental_work";
    String PRODUCT = DBConfiguration.DATA_BASE + ".product";
    String PHOTO = DBConfiguration.DATA_BASE + ".photo";

    void init();
    void addReports();

    class Request extends Thread implements AutoCloseable {

        private static final Logger logger;
        private final Connection connection;
        private final Statement statement;
        private final String[] queries;


        static {
            logger = Logger.getLogger(TableInitializer.class.getName());
            logger.setLevel(Level.ALL);
            logger.addHandler(APIManager.getFileHandler());
        }

        public Request(String... queries) throws SQLException {
            this.connection = ConnectionPool.get();
            this.queries = queries;
            try {
                this.statement = connection.createStatement();
            } catch (SQLException e) {
                logger.log(Level.SEVERE, LoggerKit.buildStackTraceMessage(e));
                ConnectionPool.put(connection);
                throw e;
            }
        }

        @Override
        public void run() {
            try (this) {
                for (String s : queries) {
                    statement.addBatch(s);
                }
                statement.executeBatch();
                String message = LoggerKit.buildStackTraceMessage(new Exception("SQL tables is created"));
                logger.log(Level.INFO, message);
            } catch (SQLException e) {
                logger.log(Level.SEVERE, LoggerKit.buildStackTraceMessage(e));
                throw new RuntimeException(e);
            }
        }

        @Override
        public void close() {
            try {
                statement.close();
            } catch (SQLException e) {
                logger.log(Level.SEVERE, LoggerKit.buildStackTraceMessage(e));
            } finally {
                ConnectionPool.put(connection);
            }
        }
    }
}
