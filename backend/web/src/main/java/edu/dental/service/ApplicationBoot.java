package edu.dental.service;

import edu.dental.WebAPI;
import edu.dental.database.DatabaseService;
import edu.dental.database.connection.ConnectionPool;
import edu.dental.database.connection.DBConfiguration;
import edu.dental.domain.APIManager;
import edu.dental.service.lifecycle.LifecycleMonitor;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import stas.utilities.LoggerKit;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;

@WebListener("/")
public class ApplicationBoot implements ServletContextListener {

    private static final String SERVICE_PROP = "service.properties";
    private static final String LOG_PROP = "log_path.properties";
    private static final String SQL_PROP = "mysql.properties";
    private static final String SECRET_PROP = "secret_key.properties";
    public static final String logPropKey = "dental_log";
    private static final Properties service = new Properties();
    private static final Properties logProp = new Properties();
    private static final Properties sqlProp = new Properties();
    private static final Properties secretProp = new Properties();


    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        appSetting();
        try {
            if (!checkDB()) {
                throw new SQLException("trouble with DB connection.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        initialization();

        System.out.println("server ready!");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ConnectionPool.deregister();
        LifecycleMonitor.INSTANCE.shutdown();
        System.out.println("context destroyed");
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

    private void appSetting() {
        try {
            try (InputStream inputStream = ApplicationBoot.class.getClassLoader().getResourceAsStream(SERVICE_PROP)) {
                service.load(inputStream);
            }
            try (InputStream inputStream = ApplicationBoot.class.getClassLoader().getResourceAsStream(LOG_PROP)) {
                logProp.load(inputStream);
            }
            APIManager.INSTANCE.setService(service);
            String logFile = logProp.getProperty(logPropKey);
            FileHandler fileHandler = new FileHandler(logFile);
            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);
            APIManager.setFileHandler(fileHandler);
            APIManager.setLoggerKit(new LoggerKit(fileHandler));
            APIManager.getLoggerKit().addLogger(APIManager.class);
            try (InputStream inputStream = ApplicationBoot.class.getClassLoader().getResourceAsStream(SQL_PROP)) {
                sqlProp.load(inputStream);
            }
            DBConfiguration.get().setSqlProp(sqlProp);
            try (InputStream inputStream = ApplicationBoot.class.getClassLoader().getResourceAsStream(SECRET_PROP)) {
                secretProp.load(inputStream);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void initialization() {
        DatabaseService databaseService = APIManager.INSTANCE.getDatabaseService();
        databaseService.getTableInitializer().init();

        APIManager.INSTANCE.getReportService();
        WebAPI.setSecretKeyProperties(secretProp);
        WebAPI.INSTANCE.getAuthenticationService();
        WebAPI.INSTANCE.getJsonParser();
        WebAPI.INSTANCE.getFilterVerification();

        Repository repository = WebAPI.INSTANCE.getRepository();
        repository.startMonitor();
    }
}
