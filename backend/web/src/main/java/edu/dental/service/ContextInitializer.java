package edu.dental.service;

import edu.dental.WebAPI;
import edu.dental.database.DatabaseService;
import edu.dental.database.connection.ConnectionPool;
import edu.dental.domain.APIManager;
import edu.dental.service.lifecycle.LifecycleMonitor;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@WebListener("/")
public class ContextInitializer implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        try {
            if (!checkDB()) {
                throw new SQLException("trouble with DB connection.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        DatabaseService databaseService = APIManager.INSTANCE.getDatabaseService();
        databaseService.getTableInitializer().init();

        APIManager.INSTANCE.getUserService();
        APIManager.INSTANCE.getReportService();
        WebAPI.INSTANCE.getAuthenticationService();
        WebAPI.INSTANCE.getJsonParser();
        WebAPI.INSTANCE.getFilterVerification();

        Repository repository = WebAPI.INSTANCE.getRepository();
        repository.startMonitor();

        System.out.println("server ready");
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
        LifecycleMonitor.INSTANCE.shutdown();
        System.out.println("context destroyed");
    }
}
