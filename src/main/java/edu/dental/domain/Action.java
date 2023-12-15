package edu.dental.domain;

import edu.dental.domain.records.WorkRecordBook;
import edu.dental.domain.reports.ReportService;
import edu.dental.domain.reports.ReportServiceException;
import edu.dental.web.Repository;

import java.io.OutputStream;

public final class Action {

    private Action() {}

    public static void saveReport(String login, OutputStream output) throws ActionException {
        ReportService reportService = ReportService.getInstance();
        WorkRecordBook recordBook = Repository.getInstance().getRecordBook(login);
        try {
            reportService.saveReportToFile(output, recordBook.getMap().keysToArray(), recordBook.getList());
        } catch (ReportServiceException e) {
            throw new ActionException(e, 500);
        }
    }

    public static class ActionException extends Exception {

        public final int CODE;

        public ActionException(Throwable cause, int code) {
            super(cause);
            CODE = code;
        }
    }
}
