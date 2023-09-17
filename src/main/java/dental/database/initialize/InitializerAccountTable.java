package dental.database.initialize;

import dental.database.dao.AccountDAO;
import dental.database.service.ConnectionPool;
import dental.database.service.DBConfig;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public final class InitializerAccountTable extends Thread {

    private static final InitializerAccountTable instance;
    private InitializerAccountTable() {}

    static {
        instance = new InitializerAccountTable();
    }

    public final String DROP = "DROP TABLE IF EXISTS ";

    public final String ACCOUNT = String.format("""
            CREATE TABLE %s (
                id INT NOT NULL AUTO_INCREMENT,
                name VARCHAR(25),
                login VARCHAR(15) NOT NULL,
                password BLOB NOT NULL,
                created DATE NOT NULL,
                PRIMARY KEY (id)
            );""", AccountDAO.TABLE_NAME);

    @Override
    public void run() {
        Connection connection = null;
        Statement statement = null;
        try {
            connection = ConnectionPool.get();
            statement = connection.createStatement();
            statement.addBatch(DROP + AccountDAO.TABLE_NAME);
            statement.addBatch(ACCOUNT);
            statement.executeBatch();
        } catch (SQLException e) {
            //TODO loggers
            throw new RuntimeException(e);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                //TODO
            } finally {
                ConnectionPool.put(connection);
            }
        }
    }

    public static void initializeDataBase() {
        instance.start();
    }
}
