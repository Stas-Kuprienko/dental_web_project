package dental.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * The class for working with the database by SQL queries.
 */
public class DBManager {
    private DBManager() {}

    /**
     * Generate the {@link Connection} object.
     * @return The {@link Connection} object with the {@linkplain DBConfig#DB_URL URL},
     *         {@linkplain DBConfig#DB_LOGIN user} and {@linkplain DBConfig#DB_PASSWORD password}.
     * @throws SQLException If something goes wrong.
     */
    private Connection doConnection() throws SQLException {
        return DriverManager.getConnection(DBConfig.getProp(DBConfig.DB_URL),
                                           DBConfig.getProp(DBConfig.DB_LOGIN),
                                           DBConfig.getProp(DBConfig.DB_PASSWORD));
    }



    /*
    Enums SQL syntax just for usable.
     */
    public enum dml
    { SELECT, UPDATE, INSERT, DELETE }
    public enum ddl
    { CREATE, DROP, ALTER, TRUNCATE }
    public enum sub
    { WHERE, FROM, INTO,
      ALL, AND, ANY, BETWEEN,
      IN, NOT, OR, EXISTS, LIKE }
}
