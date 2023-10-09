package edu.dental.database;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public final class DBServiceManager {

    private DBServiceManager() {}

    private static final String CLASS_NAME = "edu.dental.database.mysql_api.MyDBService";

    public static synchronized DBService getDBService() throws DatabaseException {
        try {
            Class<?> clas = Class.forName(CLASS_NAME);
            Constructor<?> constructor = clas.getDeclaredConstructor();
            constructor.setAccessible(true);
            DBService dbService = (DBService) constructor.newInstance();
            constructor.setAccessible(false);
            return dbService;
        } catch (ClassNotFoundException | InvocationTargetException | NoSuchMethodException
                                        | InstantiationException | IllegalAccessException e) {
            //TODO logger
            throw new DatabaseException(e.getMessage(), e.getCause());
        }
    }
}
