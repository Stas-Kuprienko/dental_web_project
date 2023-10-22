package edu.dental.domain.records;

import edu.dental.domain.entities.I_DentalWork;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Map;
import java.util.Properties;

/**
 * The basic service for managing an instances of the {@link WorkRecordBook} interface.
 * The {@code RecordManager} returns instances implementing the {@link WorkRecordBook},
 *  loaded at the {@linkplain RecordManager#service specified path}.
 */
public final class RecordManager {

    private RecordManager() {}
    static {
        service = new Properties();
    }

    private static final Properties service;
    private static final String PROP_PATH = "service.properties";


    /**
     * Return an instance of the {@link WorkRecordBook}.
     */
    public static synchronized WorkRecordBook getWorkRecordBook() {
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
    public static synchronized WorkRecordBook getWorkRecordBook(Collection<I_DentalWork> records, Map<String, Integer> map) {
        try {
            @SuppressWarnings("unchecked")
            Class<? extends WorkRecordBook> clas =
                    (Class<? extends WorkRecordBook>) Class.forName(getClassName(WorkRecordBook.class));
            Constructor<?> constructor = clas.getDeclaredConstructor(Collection.class, Map.class);
            constructor.setAccessible(true);
            WorkRecordBook recordBook = (WorkRecordBook) constructor.newInstance(records, map);
            constructor.setAccessible(false);
            return recordBook;
        } catch (ClassNotFoundException | InvocationTargetException | NoSuchMethodException
                 | ClassCastException | InstantiationException | IllegalAccessException e) {
            //TODO loggers
            throw new RuntimeException(e);
        }
    }

    public static synchronized ProductMap getProductMap() {
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

    public static synchronized ProductMap getProductMap(Collection<ProductMap.Item> list) {
        try {
            @SuppressWarnings("unchecked")
            Class<? extends ProductMap> clas =
                    (Class<? extends ProductMap>) Class.forName(getClassName(ProductMap.class));
            Constructor<?> constructor = clas.getDeclaredConstructor(Collection.class);
            constructor.setAccessible(true);
            ProductMap productMap = (ProductMap) constructor.newInstance(list);
            constructor.setAccessible(false);
            return productMap;
        } catch (ClassNotFoundException | InvocationTargetException | NoSuchMethodException
                 | ClassCastException | InstantiationException | IllegalAccessException e) {
            //TODO loggers
            throw new RuntimeException(e);
        }
    }

    private static synchronized <T> String getClassName(Class<T> clas) {
        try (InputStream inStream = RecordManager.class.getClassLoader().getResourceAsStream(PROP_PATH)) {
            service.load(inStream);
            return service.getProperty(clas.getSimpleName());
        } catch (IOException e) {
            //TODO loggers
            throw new RuntimeException(e);
        }
    }
}
