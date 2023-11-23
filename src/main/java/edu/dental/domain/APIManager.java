package edu.dental.domain;

import edu.dental.database.DBService;
import edu.dental.database.DatabaseException;
import edu.dental.database.dao.ProductMapDAO;
import edu.dental.domain.entities.I_DentalWork;
import edu.dental.domain.entities.User;
import edu.dental.domain.records.ProductMap;
import edu.dental.domain.records.WorkRecordBook;
import edu.dental.domain.reports.ReportService;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Properties;

public final class APIManager {

    private static final String PROP_PATH = "D:\\Development Java\\pet_projects\\dental\\target\\classes\\service.properties";

    private static final APIManager manager;
    static {
        manager = new APIManager();
    }
    private final Properties service;

    private APIManager() {
        service = new Properties();
        try (FileInputStream fileInput = new FileInputStream(PROP_PATH)) {
            service.load(fileInput);
        } catch (IOException e) {
            //TODO loggers
            throw new RuntimeException(e);
        }
    }


    /**
     * Return an instance of the {@link WorkRecordBook}.
     */
    public WorkRecordBook getWorkRecordBook() {
        try {
            @SuppressWarnings("unchecked")
            Class<? extends WorkRecordBook> clas =
                    (Class<? extends WorkRecordBook>) Class.forName(getClassName(WorkRecordBook.class));
            Constructor<?> constructor = clas.getDeclaredConstructor();
            constructor.setAccessible(true);
            WorkRecordBook recordBook = (WorkRecordBook) constructor.newInstance();
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
    public WorkRecordBook getWorkRecordBook(List<I_DentalWork> records, ProductMap productMap) {
        try {
            @SuppressWarnings("unchecked")
            Class<? extends WorkRecordBook> clas =
                    (Class<? extends WorkRecordBook>) Class.forName(getClassName(WorkRecordBook.class));
            Constructor<?> constructor = clas.getDeclaredConstructor(List.class, ProductMap.class);
            constructor.setAccessible(true);
            WorkRecordBook recordBook = (WorkRecordBook) constructor.newInstance(records, productMap);
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

    public ProductMap getProductMap(User user) {
        try {
            @SuppressWarnings("unchecked")
            Class<? extends ProductMapDAO> clas =
                    (Class<? extends ProductMapDAO>) Class.forName(getClassName(ProductMapDAO.class));
            Constructor<?> constructor = clas.getDeclaredConstructor(User.class);
            constructor.setAccessible(true);
            ProductMapDAO mapDAO = (ProductMapDAO) constructor.newInstance(user);
            ProductMap productMap = mapDAO.get();
            constructor.setAccessible(false);
            return productMap;
        } catch (ClassNotFoundException | InvocationTargetException | NoSuchMethodException
                 | ClassCastException | InstantiationException | IllegalAccessException | DatabaseException e) {
            //TODO loggers
            throw new RuntimeException(e);
        }
    }

    public ReportService getReportService(User user) {
        try {
            Class<?> clas = Class.forName(getClassName(ReportService.class));
            Constructor<?> constructor = clas.getDeclaredConstructor(User.class);
            constructor.setAccessible(true);
            ReportService reportService = (ReportService) constructor.newInstance(user);
            constructor.setAccessible(false);
            return reportService;
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            //TODO logger
            throw new RuntimeException(e);
        }
    }

    public DBService getDBService() {
        try {
            Class<?> clas = Class.forName(getClassName(DBService.class));
            Constructor<?> constructor = clas.getDeclaredConstructor();
            constructor.setAccessible(true);
            DBService dbService = (DBService) constructor.newInstance();
            constructor.setAccessible(false);
            return dbService;
        } catch (ClassNotFoundException | InvocationTargetException | NoSuchMethodException
                 | InstantiationException | IllegalAccessException e) {
            //TODO logger
            throw new RuntimeException(e);
        }
    }

    private <T> String getClassName(Class<T> clas) {
        return service.getProperty(clas.getSimpleName());
    }

    public static synchronized APIManager instance() {
        return manager;
    }
}
