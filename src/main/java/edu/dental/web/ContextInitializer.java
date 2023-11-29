package edu.dental.web;

import edu.dental.database.DatabaseService;
import edu.dental.database.connection.ConnectionPool;
import edu.dental.domain.APIManager;
import edu.dental.domain.records.ProductMap;
import edu.dental.domain.records.WorkRecordBook;
import edu.dental.domain.reports.ReportService;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

@WebListener("/")
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

        if (!(Arrays.stream(APIManager.INSTANCE.getRepository()
                .getClass().getInterfaces()).toList().contains(Repository.class))) {
            //TODO logger
            throw new RuntimeException();
        }
        if (!(Arrays.stream(APIManager.INSTANCE.getReportService()
                .getClass().getInterfaces()).toList().contains(ReportService.class))) {
            //TODO logger
            throw new RuntimeException();
        }
        if (!(Arrays.stream(APIManager.INSTANCE.getWorkRecordBook()
                .getClass().getInterfaces()).toList().contains(WorkRecordBook.class))) {
            //TODO logger
            throw new RuntimeException();
        }
        if (!(Arrays.stream(APIManager.INSTANCE.getProductMap()
                .getClass().getInterfaces()).toList().contains(ProductMap.class))) {
            //TODO logger
            throw new RuntimeException();
        }
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
