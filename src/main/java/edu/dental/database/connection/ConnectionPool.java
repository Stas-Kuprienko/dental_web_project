package edu.dental.database.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
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
     * @return The {@link Connection} object with the {@linkplain DBConfiguration#DB_URL URL},
     *         {@linkplain DBConfiguration#DB_LOGIN user} and {@linkplain DBConfiguration#DB_PASSWORD password}.
     */
    private static Connection createConnection() {
        try {
            return DriverManager.getConnection(DBConfiguration.getProp(DBConfiguration.DB_URL),
                    DBConfiguration.getProp(DBConfiguration.DB_LOGIN),
                    DBConfiguration.getProp(DBConfiguration.DB_PASSWORD));
        } catch (SQLException e) {
            //TODO loggers
            throw new RuntimeException(e);
        }
    }

    public static synchronized Connection get() {
        if (instance.free.isEmpty()) {
            instance.free.add(createConnection());
        }
        Connection connection = instance.free.poll();
        instance.using.add(connection);
        return connection;
    }

    public static synchronized void put(Connection connection) {
        if (connection == null) {
            throw new NullPointerException("connection is null");
        }
        instance.free.add(connection);
        instance.using.remove(connection);

    }
}