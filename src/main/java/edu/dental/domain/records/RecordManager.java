package edu.dental.domain.records;

import edu.dental.domain.entities.WorkRecord;
import edu.dental.utils.data_structures.MyList;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public final class RecordManager {

    private RecordManager() {}

    private static final String CLASS_NAME = "edu.dental.domain.records.my_work_record_book.MyWorkRecordBook";

    public static synchronized WorkRecordBook getWorkRecordBook() throws WorkRecordBookException {
        try {
            Class<?> clas = Class.forName(CLASS_NAME);
            Constructor<?> constructor = clas.getDeclaredConstructor();
            constructor.setAccessible(true);
            WorkRecordBook recordBook = (WorkRecordBook) constructor.newInstance();
            constructor.setAccessible(false);
            return recordBook;
        } catch (ClassNotFoundException | InvocationTargetException | NoSuchMethodException
                                        | InstantiationException | IllegalAccessException e) {

            throw new WorkRecordBookException(e.getMessage(), e.getCause());
        }
    }

    public static synchronized WorkRecordBook getWorkRecordBook(MyList<WorkRecord> records, ProductMapper productMap) throws WorkRecordBookException {
        try {
            Class<?> clas = Class.forName(CLASS_NAME);
            Constructor<?> constructor = clas.getDeclaredConstructor(MyList.class, ProductMapper.class);
            constructor.setAccessible(true);
            WorkRecordBook recordBook = (WorkRecordBook) constructor.newInstance(records, productMap);
            constructor.setAccessible(false);
            return recordBook;
        } catch (ClassNotFoundException | InvocationTargetException | NoSuchMethodException
                                        | InstantiationException | IllegalAccessException e) {

            throw new WorkRecordBookException(e.getMessage(), e.getCause());
        }
    }
}
