package edu.dental.domain.records;

import edu.dental.domain.entities.I_WorkRecord;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Map;

/**
 * The basic service for managing an instances of the {@link WorkRecordBook} interface.
 * The {@code RecordManager} returns instances implementing the {@link WorkRecordBook},
 *  loaded at the {@linkplain RecordManager#CLASS_NAME specified path}.
 */
public final class RecordManager {

    private RecordManager() {}

    /**
     * The path to the class implementing the {@link WorkRecordBook}.
     */
    private static final String CLASS_NAME = "edu.dental.domain.records.my_work_record_book.MyWorkRecordBook";

    /**
     * Return an instance of the {@link WorkRecordBook}.
     * @throws WorkRecordBookException if somewhat goes wrong.
     */
    public static synchronized WorkRecordBook getWorkRecordBook() throws WorkRecordBookException {
        try {
            @SuppressWarnings("unchecked")
            Class<? extends WorkRecordBook> clas = (Class<? extends WorkRecordBook>) Class.forName(CLASS_NAME);
            Constructor<?> constructor = clas.getDeclaredConstructor();
            constructor.setAccessible(true);
            WorkRecordBook recordBook = (WorkRecordBook) constructor.newInstance();
            constructor.setAccessible(false);
            return recordBook;
        } catch (ClassNotFoundException | InvocationTargetException | NoSuchMethodException
                 | ClassCastException | InstantiationException | IllegalAccessException e) {

            throw new WorkRecordBookException(e.getMessage(), e.getCause());
        }
    }

    /**
     * Return an instance of the {@link WorkRecordBook}, with the set argument values in the class fields.
     * @throws WorkRecordBookException if somewhat goes wrong.
     */
    public static synchronized WorkRecordBook getWorkRecordBook(Collection<I_WorkRecord> records, Map<String, Integer> map) throws WorkRecordBookException {
        try {
            @SuppressWarnings("unchecked")
            Class<? extends WorkRecordBook> clas = (Class<? extends WorkRecordBook>) Class.forName(CLASS_NAME);
            Constructor<?> constructor = clas.getDeclaredConstructor(Collection.class, Map.class);
            constructor.setAccessible(true);
            WorkRecordBook recordBook = (WorkRecordBook) constructor.newInstance(records, map);
            constructor.setAccessible(false);
            return recordBook;
        } catch (ClassNotFoundException | InvocationTargetException | NoSuchMethodException
                 | ClassCastException | InstantiationException | IllegalAccessException e) {

            throw new WorkRecordBookException(e.getMessage(), e.getCause());
        }
    }
}
