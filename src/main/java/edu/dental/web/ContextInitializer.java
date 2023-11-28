package edu.dental.web;

import edu.dental.database.DatabaseService;
import edu.dental.database.connection.ConnectionPool;
import edu.dental.domain.APIManager;
import edu.dental.domain.entities.User;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class ContextInitializer implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {

        try {
            if (!checkDB()) {
                throw new SQLException("trouble with DB connection.");
            }
        } catch (SQLException e) {
            //TODO logger
            throw new RuntimeException(e);
        }
        DatabaseService databaseService = APIManager.INSTANCE.getDatabaseService();
        databaseService.getTableInitializer().init();
        APIManager.INSTANCE.getWorkRecordBook();
        APIManager.INSTANCE.getProductMap();
        APIManager.INSTANCE.getReportService();
        //TODO logger
    }

    private boolean checkDB() throws SQLException {
        Connection connection = ConnectionPool.get();
        Statement statement = connection.createStatement();
        String query = "SELECT 1;";
        boolean result = statement.execute(query);
        statement.close();
        ConnectionPool.put(connection);
        return result;
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ServletContextListener.super.contextDestroyed(sce);
        ConnectionPool.deregister();
    }
}
