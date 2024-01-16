package edu.dental.database.connection;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * The class for creating a database {@link Connection connection}.
 */
public final class ConnectionPool {

    private ConnectionPool() {
        free = new ConcurrentLinkedQueue<>();
        using = new ConcurrentLinkedQueue<>();
    }

    private final static ConnectionPool instance = new ConnectionPool();
    private final ConcurrentLinkedQueue<Connection> free;
    private final ConcurrentLinkedQueue<Connection> using;

    /**
     * Generate the {@link Connection} object.
     * @return The {@link Connection} object with the {@linkplain DBConfiguration#URL URL},
     *         {@linkplain DBConfiguration#LOGIN user} and {@linkplain DBConfiguration#PASSWORD password}.
     */
    private static Connection createConnection() {
        try {
            Class.forName(DBConfiguration.get().getProp(DBConfiguration.DRIVER));
            return DriverManager.getConnection(DBConfiguration.get().getProp(DBConfiguration.URL),
                    DBConfiguration.get().getProp(DBConfiguration.LOGIN),
                    DBConfiguration.get().getProp(DBConfiguration.PASSWORD));
        } catch (SQLException | ClassNotFoundException e) {
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
            throw new NullPointerException("connection is null");
        }
        instance.free.add(connection);
        instance.using.remove(connection);
    }

    public static synchronized void deregister() {
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            try {
                DriverManager.deregisterDriver(drivers.nextElement());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}