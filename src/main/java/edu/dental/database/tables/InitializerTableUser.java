package edu.dental.database.tables;

import edu.dental.database.connection.ConnectionPool;
import edu.dental.database.dao.UserDAO;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public final class InitializerTableUser extends Thread {

    private static final InitializerTableUser instance;
    private InitializerTableUser() {}

    static {
        instance = new InitializerTableUser();
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
              );""", UserDAO.TABLE);

    @Override
    public void run() {
        Connection connection = ConnectionPool.get();
        try (Statement statement = connection.createStatement()) {
            statement.addBatch(DROP + UserDAO.TABLE);
            statement.addBatch(ACCOUNT);
            statement.executeBatch();
        } catch (SQLException e) {
            //TODO loggers
            throw new RuntimeException(e);
        } finally {
            ConnectionPool.put(connection);
        }
    }

    public static void initializeDataBase() {
        instance.start();
    }
}
