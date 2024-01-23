package edu.dental.domain.records;


import edu.dental.domain.APIManager;

import java.util.logging.Level;
import java.util.logging.Logger;

public class WorkRecordBookException extends Exception {

    private static final Logger logger;

    static {
        logger = Logger.getLogger(WorkRecordBook.class.getName());
        logger.addHandler(APIManager.fileHandler);
        logger.setLevel(Level.ALL);
    }

    public WorkRecordBookException(Exception e) {
        String message = buildStackMessage(e.getStackTrace());
        logger.log(Level.SEVERE, message);
    }

    private static String buildStackMessage(StackTraceElement[] stackTrace) {
        //TODO
        StringBuilder str = new StringBuilder();
        for (StackTraceElement e : stackTrace) {
            str.append(e.toString()).append("\n");
        }
        return str.toString();
    }
}