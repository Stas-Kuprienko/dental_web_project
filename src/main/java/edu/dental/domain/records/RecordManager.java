package edu.dental.domain.records;

import edu.dental.domain.entities.I_WorkRecord;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Map;

/**
 * The basic service for managing an instances of the {@link WorkRecordBook} interface.
 * The {@code RecordManager} returns instances implementing the {@link WorkRecordBook},
 *  loaded at the {@linkplain RecordManager#BOOK_CLASS_NAME specified path}.
 */
public final class RecordManager {

    private RecordManager() {}

    /**
     * The path to the class implementing the {@link WorkRecordBook}.
     */
    private static final String BOOK_CLASS_NAME = "edu.dental.domain.records.my_work_record_book.MyWorkRecordBook";

    private static final String MAP_CLASS_NAME = "edu.dental.domain.records.my_work_record_book.MyProductMap";

    /**
     * Return an instance of the {@link WorkRecordBook}.
     */
    public static synchronized WorkRecordBook getWorkRecordBook() {
        try {
            @SuppressWarnings("unchecked")
            Class<? extends WorkRecordBook> clas = (Class<? extends WorkRecordBook>) Class.forName(BOOK_CLASS_NAME);
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
    public static synchronized WorkRecordBook getWorkRecordBook(Collection<I_WorkRecord> records, Map<String, Integer> map) {
        try {
            @SuppressWarnings("unchecked")
            Class<? extends WorkRecordBook> clas = (Class<? extends WorkRecordBook>) Class.forName(BOOK_CLASS_NAME);
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
            Class<? extends ProductMap> clas = (Class<? extends ProductMap>) Class.forName(MAP_CLASS_NAME);
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
            Class<? extends ProductMap> clas = (Class<? extends ProductMap>) Class.forName(MAP_CLASS_NAME);
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
}
