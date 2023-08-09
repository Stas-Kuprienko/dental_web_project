package dental.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * The class for creating a database {@link Connection connection}.
 */
public class DBManager {
    private DBManager() {}

    /**
     * Generate the {@link Connection} object.
     * @return The {@link Connection} object with the {@linkplain DBConfig#DB_URL URL},
     *         {@linkplain DBConfig#DB_LOGIN user} and {@linkplain DBConfig#DB_PASSWORD password}.
     * @throws SQLException If something goes wrong.
     */
    private static Connection doConnection() {
        try{
            return DriverManager.getConnection(DBConfig.getProp(DBConfig.DB_URL),
                    DBConfig.getProp(DBConfig.DB_LOGIN),
                    DBConfig.getProp(DBConfig.DB_PASSWORD));
        } catch (SQLException e) {
            //TODO loggers
            throw new RuntimeException(e);
        }
    }

    public static Statement getStatement() {
        try (Statement statement = doConnection().createStatement()) {
            return statement;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
