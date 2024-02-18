package edu.dental.domain;

import edu.dental.database.DatabaseException;
import edu.dental.database.DatabaseService;
import edu.dental.database.dao.ProductMapDAO;
import edu.dental.domain.records.WorkRecordBook;
import edu.dental.domain.reports.ReportService;
import edu.dental.entities.DentalWork;
import edu.dental.entities.ProductMap;
import stas.utilities.LoggerKit;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Level;

public enum APIManager {

    INSTANCE;

    private static LoggerKit loggerKit;
    private static FileHandler fileHandler;

    private Properties service;

    private DatabaseService databaseService;
    private ReportService reportService;


    public static LoggerKit getLoggerKit() {
        return loggerKit;
    }

    public static void setLoggerKit(LoggerKit loggerKit) {
        APIManager.loggerKit = loggerKit;
    }

    public static FileHandler getFileHandler() {
        return fileHandler;
    }

    public static void setFileHandler(FileHandler fileHandler) {
        APIManager.fileHandler = fileHandler;
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
            Exception e = new Exception(clas.getName());
            loggerKit.doLog(this.getClass(), LoggerKit.buildStackTraceMessage(e), Level.INFO);
            return recordBook;
        } catch (ClassNotFoundException | InvocationTargetException | NoSuchMethodException
                 | ClassCastException | InstantiationException | IllegalAccessException e) {
            loggerKit.doLog(this.getClass(), e, Level.SEVERE);
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
            Exception e = new Exception(clas.getName());
            loggerKit.doLog(this.getClass(), LoggerKit.buildStackTraceMessage(e), Level.INFO);
            return recordBook;
        } catch (ClassNotFoundException | InvocationTargetException | NoSuchMethodException
                 | ClassCastException | InstantiationException | IllegalAccessException e) {
            loggerKit.doLog(this.getClass(), e, Level.SEVERE);
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
            Exception e = new Exception(clas.getName());
            loggerKit.doLog(this.getClass(), LoggerKit.buildStackTraceMessage(e), Level.INFO);
            return productMap;
        } catch (ClassNotFoundException | InvocationTargetException | NoSuchMethodException
                 | ClassCastException | InstantiationException | IllegalAccessException e) {
            loggerKit.doLog(this.getClass(), e, Level.SEVERE);
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
            Exception e = new Exception(clas.getName());
            loggerKit.doLog(this.getClass(), LoggerKit.buildStackTraceMessage(e), Level.INFO);
            return productMap;
        } catch (ClassNotFoundException | InvocationTargetException | NoSuchMethodException
                 | ClassCastException | InstantiationException | IllegalAccessException | DatabaseException e) {
            loggerKit.doLog(this.getClass(), e, Level.SEVERE);
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
            Exception e = new Exception(c.getName());
            loggerKit.doLog(this.getClass(), LoggerKit.buildStackTraceMessage(e), Level.INFO);
            return (T) constructor.newInstance();
        } catch (InvocationTargetException | NoSuchMethodException | InstantiationException
                 | IllegalAccessException | ClassNotFoundException e) {
            loggerKit.doLog(this.getClass(), e, Level.SEVERE);
            throw new RuntimeException(e);
        } finally {
            if (constructor != null) {
                constructor.setAccessible(false);
            }
        }
    }

    public Properties getService() {
        return service;
    }

    public void setService(Properties service) {
        this.service = service;
    }

    private <T> String getClassName(Class<T> clas) {
        return service.getProperty(clas.getSimpleName());
    }
}