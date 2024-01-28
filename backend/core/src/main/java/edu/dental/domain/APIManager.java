package edu.dental.domain;

import edu.dental.database.DatabaseException;
import edu.dental.database.DatabaseService;
import edu.dental.database.dao.ProductMapDAO;
import edu.dental.domain.records.WorkRecordBook;
import edu.dental.domain.reports.ReportService;
import edu.dental.entities.DentalWork;
import edu.dental.entities.ProductMap;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public enum APIManager {

    INSTANCE;

    private static final String SERVICE_PROP_PATH = "D:\\Development Java\\pet_projects\\dental_web_project\\backend\\core\\target\\classes\\service.properties";
    private static final String LOGGING_PROP_PATH = "D:\\Development Java\\pet_projects\\dental_web_project\\backend\\core\\target\\classes\\log_path.properties";

    private static final Logger logger;
    public static final FileHandler fileHandler;
    public static final SimpleFormatter formatter;

    public static final String logPropKey = "dental_log";

    public static final Properties logProperties;

    private final Properties service;

    private DatabaseService databaseService;
    private ReportService reportService;

    static {
        logger = Logger.getLogger(APIManager.class.getName());

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
            throw new RuntimeException(e);
        }
    }

    APIManager() {
        service = new Properties();
        try (FileInputStream fileInput = new FileInputStream(SERVICE_PROP_PATH)) {
            service.load(fileInput);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Return an instance of the {@link WorkRecordBook}.
     */
    public WorkRecordBook getWorkRecordBook(int userId) {
        try {
            @SuppressWarnings("unchecked")
            Class<? extends WorkRecordBook> clas =
                    (Class<? extends WorkRecordBook>) Class.forName(getClassName(WorkRecordBook.class));
            Constructor<?> constructor = clas.getDeclaredConstructor(int.class);
            constructor.setAccessible(true);
            WorkRecordBook recordBook = (WorkRecordBook) constructor.newInstance(userId);
            constructor.setAccessible(false);
            return recordBook;
        } catch (ClassNotFoundException | InvocationTargetException | NoSuchMethodException
                 | ClassCastException | InstantiationException | IllegalAccessException e) {
            logger.log(Level.SEVERE, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Return an instance of the {@link WorkRecordBook}, with the set argument values in the class fields.
     */
    public WorkRecordBook getWorkRecordBook(int userId, List<DentalWork> records, ProductMap productMap) {
        try {
            @SuppressWarnings("unchecked")
            Class<? extends WorkRecordBook> clas =
                    (Class<? extends WorkRecordBook>) Class.forName(getClassName(WorkRecordBook.class));
            Constructor<?> constructor = clas.getDeclaredConstructor(int.class, List.class, ProductMap.class);
            constructor.setAccessible(true);
            WorkRecordBook recordBook = (WorkRecordBook) constructor.newInstance(userId, records, productMap);
            constructor.setAccessible(false);
            return recordBook;
        } catch (ClassNotFoundException | InvocationTargetException | NoSuchMethodException
                 | ClassCastException | InstantiationException | IllegalAccessException e) {
            logger.log(Level.SEVERE, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public ProductMap getProductMap() {
        try {
            @SuppressWarnings("unchecked")
            Class<? extends ProductMap> clas =
                    (Class<? extends ProductMap>) Class.forName(getClassName(ProductMap.class));
            Constructor<?> constructor = clas.getDeclaredConstructor();
            constructor.setAccessible(true);
            ProductMap productMap = (ProductMap) constructor.newInstance();
            constructor.setAccessible(false);
            return productMap;
        } catch (ClassNotFoundException | InvocationTargetException | NoSuchMethodException
                 | ClassCastException | InstantiationException | IllegalAccessException e) {
            logger.log(Level.SEVERE, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public ProductMap getProductMap(int userId) {
        try {
            @SuppressWarnings("unchecked")
            Class<? extends ProductMapDAO> clas =
                    (Class<? extends ProductMapDAO>) Class.forName(getClassName(ProductMapDAO.class));
            Constructor<?> constructor = clas.getDeclaredConstructor(int.class);
            constructor.setAccessible(true);
            ProductMapDAO mapDAO = (ProductMapDAO) constructor.newInstance(userId);
            ProductMap productMap = mapDAO.get();
            constructor.setAccessible(false);
            return productMap;
        } catch (ClassNotFoundException | InvocationTargetException | NoSuchMethodException
                 | ClassCastException | InstantiationException | IllegalAccessException | DatabaseException e) {
            logger.log(Level.SEVERE, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public synchronized ReportService getReportService() {
        if (reportService == null) {
            reportService = init(ReportService.class);
        }
        return reportService;
    }

    public synchronized DatabaseService getDatabaseService() {
        if (databaseService == null) {
            databaseService = init(DatabaseService.class);
        }
        return databaseService;
    }

    @SuppressWarnings("unchecked")
    public <T> T init(Class<T> clas) {
        Constructor<?> constructor = null;
        try {
            Class<?> c = Class.forName(getClassName(clas));
            constructor = c.getDeclaredConstructor();
            constructor.setAccessible(true);
            return (T) constructor.newInstance();
        } catch (InvocationTargetException | NoSuchMethodException | InstantiationException
                 | IllegalAccessException | ClassNotFoundException e) {
            logger.log(Level.SEVERE, e.getMessage());
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
}