package edu.dental.domain.reports;

import edu.dental.domain.entities.User;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public final class ReportServiceManager {

    private ReportServiceManager() {}

    private static final String CLASS_NAME = "edu.dental.domain.reports.my_report_service.MyReportService";

    public static synchronized ReportService getReportService(User user) {
        try {
            Class<?> clas = Class.forName(CLASS_NAME);
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

}
