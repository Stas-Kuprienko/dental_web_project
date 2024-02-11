package edu.dental.service;

import edu.dental.control.*;
import jakarta.servlet.http.HttpServlet;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public enum WebAPIManager {

    INSTANCE;

    private static final String SERVICE_PROP_PATH = "D:\\Development Java\\pet_projects\\dental_web_project\\backend\\core\\target\\classes\\service.properties";
    private static final String LOGGING_PROP_PATH = "D:\\Development Java\\pet_projects\\dental_web_project\\backend\\core\\target\\classes\\log_path.properties";

    private static final Logger logger;
    public static final FileHandler fileHandler;
    public static final SimpleFormatter formatter;

    public static final String logPropKey = "frontend_dental_log";

    public static final Properties logProperties;

    private final Properties service;

    private LoggerKit loggerKit;
    private Administrator administrator;


    static {
        logger = Logger.getLogger(WebAPIManager.class.getName());

        logProperties = new Properties();
        formatter = new SimpleFormatter();

        try (FileInputStream fileInput = new FileInputStream(LOGGING_PROP_PATH)) {
            logProperties.load(fileInput);
            String filePath = logProperties.getProperty(logPropKey);
            fileHandler = new FileHandler(filePath);
            fileHandler.setFormatter(formatter);
            logger.addHandler(fileHandler);
            logger.setLevel(Level.ALL);
        } catch (IOException e) {
            Logger.getLogger(WebAPIManager.class.getName()).log(Level.SEVERE, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    WebAPIManager() {
        service = new Properties();
        try (FileInputStream fileInput = new FileInputStream(SERVICE_PROP_PATH)) {
            service.load(fileInput);
        } catch (IOException e) {
            Logger.getLogger(WebAPIManager.class.getName()).log(Level.SEVERE, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public synchronized Administrator getAdministrator() {
        if (administrator == null) {
            administrator = init(Administrator.class);
        }
        return administrator;
    }

    public synchronized LoggerKit getLoggerKit() {
        if (loggerKit == null) {
            loggerKit = createLoggerKit();
        }
        return loggerKit;
    }

    @SuppressWarnings("unchecked")
    public <T> T init(Class<T> clas) {
        Constructor<?> constructor = null;
        try {
            Class<?> c = Class.forName(getClassName(clas));
            constructor = c.getDeclaredConstructor();
            constructor.setAccessible(true);
            loggerKit.doLogging(this.getClass(), c.getName(), Level.INFO);
            return (T) constructor.newInstance();
        } catch (InvocationTargetException | NoSuchMethodException | InstantiationException
                 | IllegalAccessException | ClassNotFoundException e) {
            loggerKit.doLogging(WebAPIManager.class, e, Level.SEVERE);
            throw new RuntimeException(e);
        } finally {
            if (constructor != null) {
                constructor.setAccessible(false);
            }
        }
    }

    private <T> String getClassName(Class<T> clas) {
        return service.getProperty(clas.getSimpleName());
    }

    private LoggerKit createLoggerKit() {
        LoggerKit loggerKit = new LoggerKit();
        loggerKit.addLogger(WebAPIManager.class);
        loggerKit.addLogger(WebUtility.HttpRequestSender.class);
        loggerKit.addLogger(HttpServlet.class);
        loggerKit.addLogger(Administrator.class);
        loggerKit.addLogger(DentalWorkService.class);
        loggerKit.addLogger(DentalWorksListService.class);
        loggerKit.addLogger(ProductMapService.class);
        loggerKit.addLogger(ProfitRecordService.class);
        return loggerKit;
    }
}
