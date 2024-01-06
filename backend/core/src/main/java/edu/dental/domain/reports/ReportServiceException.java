package edu.dental.domain.reports;

public class ReportServiceException extends Exception {

    public ReportServiceException(Exception e) {
        super(e);
    }

    public ReportServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
