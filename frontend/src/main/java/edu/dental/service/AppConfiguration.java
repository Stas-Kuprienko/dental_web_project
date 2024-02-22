package edu.dental.service;

import edu.dental.control.*;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpServlet;
import stas.http_tools.HttpRequester;
import stas.utilities.LoggerKit;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;

@WebListener("/")
public class AppConfiguration implements ServletContextListener {

    private static final String API_URL_KEY = "API_URL";
    private static final String SERVICE_PROP_PATH = "service.properties";
    private static final String LOGGING_PROP_PATH = "log_path.properties";
    public static final String logPropKey = "frontend_dental_log";


    @Override
    public void contextInitialized(ServletContextEvent sce) {
        appSetting();

        System.out.println("application ready!");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {}


    private void appSetting() {
        try {
            try (InputStream inputStream = AppConfiguration.class.getClassLoader().getResourceAsStream(SERVICE_PROP_PATH)) {
                Properties service = new Properties();
                service.load(inputStream);
                WebAPIManager.INSTANCE.setService(service);
            }
            try (InputStream inputStream = AppConfiguration.class.getClassLoader().getResourceAsStream(LOGGING_PROP_PATH)) {
                Properties logProp = new Properties();
                logProp.load(inputStream);
                String logFile = logProp.getProperty(logPropKey);
                FileHandler fileHandler = new FileHandler(logFile);
                SimpleFormatter formatter = new SimpleFormatter();
                fileHandler.setFormatter(formatter);
                WebAPIManager.setFileHandler(fileHandler);
            }
            LoggerKit loggerKit = new LoggerKit(WebAPIManager.getFileHandler());
            fillLoggerKit(loggerKit);
            WebAPIManager.setLoggerKit(loggerKit);
            String apiUrl = WebAPIManager.INSTANCE.getService().getProperty(API_URL_KEY);
            WebUtility.INSTANCE.setRequestSender(HttpRequester.getXWWWFormRequester(apiUrl));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void fillLoggerKit(LoggerKit loggerKit) {
        loggerKit.addLogger(WebAPIManager.class);
        loggerKit.addLogger(HttpServlet.class);
        loggerKit.addLogger(Administrator.class);
        loggerKit.addLogger(DentalWorkService.class);
        loggerKit.addLogger(DentalWorksListService.class);
        loggerKit.addLogger(ProductMapService.class);
        loggerKit.addLogger(ProfitRecordService.class);
    }
}
