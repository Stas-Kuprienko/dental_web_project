package edu.dental.domain;

import edu.dental.database.DatabaseException;
import edu.dental.database.DatabaseService;
import edu.dental.database.dao.DentalWorkDAO;
import edu.dental.domain.entities.DentalWork;
import edu.dental.domain.entities.User;
import edu.dental.domain.records.WorkRecordBook;
import edu.dental.domain.records.WorkRecordBookException;
import edu.dental.domain.reports.ReportService;
import edu.dental.domain.reports.ReportServiceException;
import edu.dental.web.Repository;

import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.util.List;

public final class Action {

    private Action() {}


    public static void newWorkRecord(String login, String patient, String clinic, String product, int quantity, LocalDate complete) throws ActionException {
        User user = Repository.getInstance().getUser(login);
        WorkRecordBook recordBook = Repository.getInstance().getRecordBook(login);
        DentalWorkDAO workDAO = DatabaseService.getInstance().getDentalWorkDAO(user);
        DentalWork dw;

        if (product == null || product.isEmpty()) {
            dw = recordBook.createRecord(patient, clinic);
        } else {
            try {
                dw = recordBook.createRecord(patient, clinic, product, quantity, complete);
            } catch (WorkRecordBookException e) {
                throw new ActionException(e, 400);
            }
        }
        try {
            workDAO.put(dw);
        } catch (DatabaseException e) {
            throw new ActionException(e, 500);
        }
    }

    public static void newProductToMap() {

    }

    public static void editWork(String login, int id, String field, String value) throws ActionException {
        User user = Repository.getInstance().getUser(login);
        WorkRecordBook recordBook = Repository.getInstance().getRecordBook(login);
        DentalWorkDAO workDAO = DatabaseService.getInstance().getDentalWorkDAO(user);
        DentalWork dw;
        try {
            dw = recordBook.getByID(id);
        } catch (WorkRecordBookException e) {
            throw new ActionException(e, 400);
        }
        try {
            DentalWork.class.getMethod(concatenate(field, "set"), String.class).invoke(dw, value);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new ActionException(e, 400);
        }
        try {
            workDAO.edit(dw);
        } catch (DatabaseException e) {
            throw new ActionException(e, 500);
        }
    }

    public static void addProductToWork(String login, int id, String product, int quantity) throws ActionException {
        User user = Repository.getInstance().getUser(login);
        WorkRecordBook recordBook = Repository.getInstance().getRecordBook(login);
        DentalWorkDAO workDAO = DatabaseService.getInstance().getDentalWorkDAO(user);
        DentalWork dw;
        try {
            dw = recordBook.getByID(id);
        } catch (WorkRecordBookException e) {
            throw new ActionException(e, 500);
        }
        try {
            recordBook.addProductToRecord(dw, product, quantity);
            workDAO.edit(dw);
        } catch (WorkRecordBookException | DatabaseException e) {
            throw new ActionException(e, 500);
        }
    }

    public static void deleteWorkRecord(String login, int id) throws ActionException {
        User user = Repository.getInstance().getUser(login);
        WorkRecordBook recordBook = Repository.getInstance().getRecordBook(login);
        DentalWorkDAO workDAO = DatabaseService.getInstance().getDentalWorkDAO(user);
        DentalWork dw;
        try {
            dw = recordBook.getByID(id);
            recordBook.deleteRecord(dw);
            workDAO.delete(id);
        } catch (WorkRecordBookException | DatabaseException e) {
            throw new ActionException(e, 500);
        }
    }

    public static void deleteProductFromWork(String login, int id, String product) throws ActionException {
        User user = Repository.getInstance().getUser(login);
        WorkRecordBook recordBook = Repository.getInstance().getRecordBook(login);
        DentalWorkDAO workDAO = DatabaseService.getInstance().getDentalWorkDAO(user);
        DentalWork dw;
        try {
            dw = recordBook.getByID(id);
            recordBook.removeProduct(dw, product);
        } catch (WorkRecordBookException e) {
            throw new ActionException(e, 400);
        }
        try {
            workDAO.edit(dw);
        } catch (DatabaseException e) {
            throw new ActionException(e, 500);
        }
    }

    public static void workSorting(String login) throws ActionException {
        User user = Repository.getInstance().getUser(login);
        WorkRecordBook recordBook = Repository.getInstance().getRecordBook(login);
        DentalWorkDAO workDAO = DatabaseService.getInstance().getDentalWorkDAO(user);
        List<DentalWork> closedWorks = recordBook.sorting();
        try {
            workDAO.setFieldValue(closedWorks, "status", "CLOSED");
            workDAO.setReportId(closedWorks);
        } catch (DatabaseException e) {
            throw new ActionException(e, 500);
        }
    }

    public static void saveReport(String login, OutputStream output) throws ActionException {
        ReportService reportService = ReportService.getInstance();
        WorkRecordBook recordBook = Repository.getInstance().getRecordBook(login);
        try {
            reportService.saveReportToFile(output, recordBook.getMap().keysToArray(), recordBook.getList());
        } catch (ReportServiceException e) {
            throw new ActionException(e, 500);
        }
    }

    private static String concatenate(String toModify, @SuppressWarnings("all") String toInsert) {
        char firstLetter = (char) (toModify.charAt(0) - 32);
        StringBuilder str = new StringBuilder(toModify);
        str.setCharAt(0, firstLetter);
        str.insert(0, toInsert, 0, toInsert.length());
        return str.toString();
    }

    public static class ActionException extends Exception {

        public final int CODE;

        public ActionException(Throwable cause, int code) {
            super(cause);
            CODE = code;
        }
    }
}
