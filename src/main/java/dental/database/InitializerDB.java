package dental.database;

import dental.database.dao.AccountDAO;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public final class InitializerDB extends Thread {

    private static final InitializerDB instance;
    private InitializerDB() {}

    static {
        instance = new InitializerDB();
    }

    public final String DROP = String.format("DROP TABLE IF EXISTS %s.", DBConfig.DATA_BASE);

    public final String ACCOUNT = String.format("""
            CREATE TABLE %s.%s (
                id INT NOT NULL AUTO_INCREMENT,
                name VARCHAR(25),
                login VARCHAR(15) NOT NULL,
                password BLOB NOT NULL,
                created DATE NOT NULL,
                PRIMARY KEY (id)
            );""", DBConfig.DATA_BASE, AccountDAO.TABLE_NAME);

    @Override
    public void run() {
        try {
            Connection connection = ConnectionPool.get();
            Statement statement = connection.createStatement();
            statement.addBatch(DROP + AccountDAO.TABLE_NAME);
            statement.addBatch(ACCOUNT);
            ConnectionPool.put(connection);
        } catch (SQLException e) {
            //TODO loggers
            throw new RuntimeException(e);
        }
    }

    public static void initializeDataBase() {
        instance.start();
    }
}
