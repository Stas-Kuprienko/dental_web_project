package edu.dental.web.servlets.basic;

import edu.dental.database.DBService;
import edu.dental.database.DBServiceManager;
import edu.dental.database.connection.ConnectionPool;
import edu.dental.domain.entities.User;
import edu.dental.domain.records.RecordManager;
import edu.dental.domain.reports.ReportServiceManager;
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
        DBService dbService = DBServiceManager.get().getDBService();
        dbService.getTableInitializer().init();
        RecordManager.get().getWorkRecordBook();
        RecordManager.get().getProductMap();
        ReportServiceManager.get().getReportService(new User());
        System.out.println("contextInitialized");
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
