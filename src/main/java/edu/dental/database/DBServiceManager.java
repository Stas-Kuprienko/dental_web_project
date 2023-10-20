package edu.dental.database;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

public final class DBServiceManager {

    private DBServiceManager() {}
    static {
        service = new Properties();
    }

    private static final Properties service;
    private static final String PROP_PATH = "service.properties";


    public static synchronized DBService getDBService() {
        try {
            Class<?> clas = Class.forName(getClassName());
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

    private static synchronized String getClassName() {
        try (InputStream inStream = DBServiceManager.class.getClassLoader().getResourceAsStream(PROP_PATH)) {
            service.load(inStream);
            return service.getProperty(DBService.class.getSimpleName());
        } catch (IOException e) {
            //TODO loggers
            throw new RuntimeException(e);
        }
    }
}
