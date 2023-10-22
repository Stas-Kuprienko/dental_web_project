package edu.dental.domain.reports;

import edu.dental.domain.entities.User;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

public final class ReportServiceManager {

    private ReportServiceManager() {}
    static {
        service = new Properties();
    }

    private static final Properties service;
    private static final String PROP_PATH = "service.properties";


    public static synchronized ReportService getReportService(User user) {
        try {
            Class<?> clas = Class.forName(getClassName());
            Constructor<?> constructor = clas.getDeclaredConstructor(User.class);
            constructor.setAccessible(true);
            ReportService reportService = (ReportService) constructor.newInstance(user);
            constructor.setAccessible(false);
            return reportService;
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            //TODO logger
            throw new RuntimeException(e);
        }
    }

    private static synchronized String getClassName() {
        try (InputStream inStream = ReportServiceManager.class.getClassLoader().getResourceAsStream(PROP_PATH)) {
            service.load(inStream);
            return service.getProperty(ReportService.class.getSimpleName());
        } catch (IOException e) {
            //TODO loggers
            throw new RuntimeException(e);
        }
    }
}
