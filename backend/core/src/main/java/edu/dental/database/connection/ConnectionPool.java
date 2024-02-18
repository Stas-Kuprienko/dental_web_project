package edu.dental.database.connection;

import edu.dental.domain.APIManager;
import stas.utilities.LoggerKit;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The class for creating a database {@link Connection connection}.
 */
public final class ConnectionPool {

    private final static ConnectionPool instance = new ConnectionPool();
    private static final Logger logger;

    private final ConcurrentLinkedQueue<Connection> free;
    private final ConcurrentLinkedQueue<Connection> using;


    static {
        logger = Logger.getLogger(ConnectionPool.class.getName());
        logger.setLevel(Level.ALL);
        logger.addHandler(APIManager.fileHandler);
    }

    private ConnectionPool() {
        free = new ConcurrentLinkedQueue<>();
        using = new ConcurrentLinkedQueue<>();
    }


    /**
     * Generate the {@link Connection} object.
     * @return The {@link Connection} object with the {@linkplain DBConfiguration#URL URL},
     *         {@linkplain DBConfiguration#LOGIN user} and {@linkplain DBConfiguration#PASSWORD password}.
     */
    private static Connection createConnection() {
        try {
            Class.forName(DBConfiguration.get().getProp(DBConfiguration.DRIVER));
            Exception e = new Exception("Connection is created - " + DBConfiguration.get().getProp(DBConfiguration.URL));
            logger.log(Level.INFO, LoggerKit.buildStackTraceMessage(e.getStackTrace()));
            return DriverManager.getConnection(DBConfiguration.get().getProp(DBConfiguration.URL),
                    DBConfiguration.get().getProp(DBConfiguration.LOGIN),
                    DBConfiguration.get().getProp(DBConfiguration.PASSWORD));
        } catch (SQLException | ClassNotFoundException e) {
            logger.log(Level.SEVERE, LoggerKit.buildStackTraceMessage(e.getStackTrace()));
            throw new RuntimeException(e);
        }
    }

    public static Connection get() {
        if (instance.free.isEmpty()) {
            instance.free.add(createConnection());
        }
        Connection connection = instance.free.poll();
        instance.using.add(connection);
        return connection;
    }

    public static void put(Connection connection) {
        if (connection == null) {
            NullPointerException e = new NullPointerException("connection is null");
            logger.log(Level.SEVERE, LoggerKit.buildStackTraceMessage(e));
            throw e;
        }
        instance.free.add(connection);
        instance.using.remove(connection);
    }

    public static synchronized void deregister() {
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        StringBuilder message = new StringBuilder();
        while (drivers.hasMoreElements()) {
            try {
                Driver driver = drivers.nextElement();
                DriverManager.deregisterDriver(driver);
                message.append(driver.toString()).append('\n');
            } catch (SQLException e) {
                logger.log(Level.SEVERE, LoggerKit.buildStackTraceMessage(e.getStackTrace()));
                throw new RuntimeException(e);
            }
        }
        logger.log(Level.INFO, LoggerKit.buildStackTraceMessage(new Exception(message.toString()).getStackTrace()));
    }
}