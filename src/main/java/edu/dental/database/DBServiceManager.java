package edu.dental.database;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

public final class DBServiceManager {

    private static final DBServiceManager manager;
    static {
        manager = new DBServiceManager();
    }

    private DBServiceManager() {
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


    public DBService getDBService() {
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

    private String getClassName() {
        return service.getProperty(DBService.class.getSimpleName());
    }

    public static synchronized DBServiceManager get() {
        return manager;
    }
}
