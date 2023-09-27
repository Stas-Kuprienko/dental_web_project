package edu.dental.database;

import edu.dental.database.connection.ConnectionPool;
import edu.dental.database.mysql_api.dao.UserMySql;

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
              );""", UserMySql.TABLE);

    @Override
    public void run() {
        Connection connection = ConnectionPool.get();
        try (Statement statement = connection.createStatement()) {
            statement.addBatch(DROP + UserMySql.TABLE);
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
