package edu.dental.domain;

import edu.dental.database.DatabaseException;
import edu.dental.database.DatabaseService;
import edu.dental.database.dao.ProductMapDAO;
import edu.dental.entities.DentalWork;
import edu.dental.entities.User;
import edu.dental.entities.ProductMap;
import edu.dental.domain.records.WorkRecordBook;
import edu.dental.domain.reports.ReportService;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Properties;

public enum APIManager {

    INSTANCE;

    private static final String PROP_PATH = "D:\\Development Java\\pet_projects\\dental_web_project\\backend\\core\\target\\classes\\service.properties";

    private final Properties service;

    private final DatabaseService databaseService;
    private final ReportService reportService;


    APIManager() {
        service = new Properties();
        try (FileInputStream fileInput = new FileInputStream(PROP_PATH)) {
            service.load(fileInput);
        } catch (IOException e) {
            //TODO loggers
            throw new RuntimeException(e);
        }
        this.databaseService = init(DatabaseService.class);
        this.reportService = init(ReportService.class);
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
            //TODO loggers
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
            //TODO loggers
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
            //TODO loggers
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
            //TODO loggers
            throw new RuntimeException(e);
        }
    }

    public ReportService getReportService() {
        return reportService;
    }

    public DatabaseService getDatabaseService() {
        return databaseService;
    }


    private <T> String getClassName(Class<T> clas) {
        return service.getProperty(clas.getSimpleName());
    }

    @SuppressWarnings("unchecked")
    private <T> T init(Class<T> clas) {
        Constructor<?> constructor = null;
        try {
            Class<?> c = Class.forName(getClassName(clas));
            constructor = c.getDeclaredConstructor();
            constructor.setAccessible(true);
            return (T) constructor.newInstance();
        } catch (InvocationTargetException | NoSuchMethodException | InstantiationException
                 | IllegalAccessException | ClassNotFoundException e) {
            //TODO logger
            throw new RuntimeException(e);
        } finally {
            if (constructor != null) {
                constructor.setAccessible(false);
            }
        }
    }
}