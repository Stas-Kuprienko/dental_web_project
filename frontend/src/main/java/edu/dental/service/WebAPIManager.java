package edu.dental.service;

import edu.dental.control.Administrator;
import stas.utilities.LoggerKit;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Level;

public enum WebAPIManager {

    INSTANCE;

    private static FileHandler fileHandler;
    private static LoggerKit loggerKit;

    private Properties service;
    private Administrator administrator;


    public static FileHandler getFileHandler() {
        return fileHandler;
    }

    public static void setFileHandler(FileHandler fileHandler) {
        WebAPIManager.fileHandler = fileHandler;
    }

    public static LoggerKit getLoggerKit() {
        return loggerKit;
    }

    public static void setLoggerKit(LoggerKit loggerKit) {
        WebAPIManager.loggerKit = loggerKit;
    }


    public synchronized Administrator getAdministrator() {
        if (administrator == null) {
            administrator = init(Administrator.class);
        }
        return administrator;
    }

    @SuppressWarnings("unchecked")
    public <T> T init(Class<T> clas) {
        Constructor<?> constructor = null;
        try {
            Class<?> c = Class.forName(getClassName(clas));
            constructor = c.getDeclaredConstructor();
            constructor.setAccessible(true);
            Exception e = new Exception(c.getName());
            loggerKit.doLog(this.getClass(), e, Level.INFO);
            return (T) constructor.newInstance();
        } catch (InvocationTargetException | NoSuchMethodException | InstantiationException
                 | IllegalAccessException | ClassNotFoundException e) {
            loggerKit.doLog(WebAPIManager.class, e, Level.SEVERE);
            throw new RuntimeException(e);
        } finally {
            if (constructor != null) {
                constructor.setAccessible(false);
            }
        }
    }

    public Properties getService() {
        return service;
    }

    public void setService(Properties service) {
        this.service = service;
    }

    private <T> String getClassName(Class<T> clas) {
        return service.getProperty(clas.getSimpleName());
    }
}
