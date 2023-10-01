package edu.dental.domain.records;

public class WorkRecordBookException extends Exception {

    public WorkRecordBookException(String message) {
        super(message);
    }

    public WorkRecordBookException(String message, Throwable cause) {
        super(message, cause);
    }
}
