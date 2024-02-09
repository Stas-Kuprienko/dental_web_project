package edu.dental.domain.records;


import edu.dental.domain.APIManager;

import java.util.logging.Level;
import java.util.logging.Logger;

public class WorkRecordException extends Exception {

    private static final Logger logger;

    static {
        logger = Logger.getLogger(WorkRecordBook.class.getName());
        logger.addHandler(APIManager.fileHandler);
        logger.setLevel(Level.ALL);
    }

    public WorkRecordException(Exception e) {
        super(e);
        logger.log(Level.SEVERE, buildStackMessage(e.getStackTrace()));
    }


    private static String buildStackMessage(StackTraceElement[] stackTrace) {
        StringBuilder str = new StringBuilder();
        for (StackTraceElement e : stackTrace) {
            str.append(e.toString()).append("\n");
        }
        return str.toString();
    }
}