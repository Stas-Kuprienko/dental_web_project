package edu.dental.domain.records;

import edu.dental.database.DatabaseException;
import edu.dental.database.dao.ProductMapDAO;
import edu.dental.domain.entities.I_DentalWork;
import edu.dental.domain.entities.User;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Properties;

/**
 * The basic service for managing an instances of the {@link WorkRecordBook} interface.
 * The {@code RecordManager} returns instances implementing the {@link WorkRecordBook},
 *  loaded at the {@linkplain RecordManager#service specified path}.
 */
public final class RecordManager {

    private static final RecordManager manager;
    static {
        manager = new RecordManager();
    }

    private RecordManager() {
        service = new Properties();
        try (FileInputStream fileInput = new FileInputStream(PROP_PATH)) {
            service.load(fileInput);
        } catch (IOException e) {
            //TODO loggers
            throw new RuntimeException(e);
        }
    }

    private final Properties service;
    private static final String PROP_PATH = "D:\\Development Java\\pet_projects\\dental\\target\\classes\\service.properties";


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
    public WorkRecordBook getWorkRecordBook(Collection<I_DentalWork> records, ProductMap productMap) {
        try {
            @SuppressWarnings("unchecked")
            Class<? extends WorkRecordBook> clas =
                    (Class<? extends WorkRecordBook>) Class.forName(getClassName(WorkRecordBook.class));
            Constructor<?> constructor = clas.getDeclaredConstructor(Collection.class, ProductMap.class);
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

    private <T> String getClassName(Class<T> clas) {
        try (InputStream inStream = RecordManager.class.getClassLoader().getResourceAsStream(PROP_PATH)) {
            service.load(inStream);
            return service.getProperty(clas.getSimpleName());
        } catch (IOException e) {
            //TODO loggers
            throw new RuntimeException(e);
        }
    }

    public static synchronized RecordManager get() {
        return manager;
    }
}
