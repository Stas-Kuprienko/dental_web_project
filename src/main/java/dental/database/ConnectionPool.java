package dental.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * The class for creating a database {@link Connection connection}.
 */
public final class ConnectionPool {
    private ConnectionPool() {
        free = new ConcurrentLinkedQueue<>();
        using = new ConcurrentLinkedQueue<>();
    }

    public static final DateTimeFormatter SQL_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-LL-dd");

    private final static ConnectionPool instance = new ConnectionPool();
    private final ConcurrentLinkedQueue<Connection> free;
    private final ConcurrentLinkedQueue<Connection> using;

    /**
     * Generate the {@link Connection} object.
     * @return The {@link Connection} object with the {@linkplain DBConfig#DB_URL URL},
     *         {@linkplain DBConfig#DB_LOGIN user} and {@linkplain DBConfig#DB_PASSWORD password}.
     */
    private static Connection createConnection() {
        try {
            return DriverManager.getConnection(DBConfig.getProp(DBConfig.DB_URL),
                    DBConfig.getProp(DBConfig.DB_LOGIN),
                    DBConfig.getProp(DBConfig.DB_PASSWORD));
        } catch (SQLException e) {
            //TODO loggers
            throw new RuntimeException(e);
        }
    }

    public static synchronized Connection get() throws SQLException {
        if (instance.free.isEmpty()) {
            instance.free.add(createConnection());
        }
        Connection connection = instance.free.poll();
        instance.using.add(connection);
        return connection;
    }

    public static synchronized void put(Connection connection) {
        if (connection == null) {
            throw new NullPointerException();
        }
        instance.free.add(connection);
        instance.using.remove(connection);

    }
}