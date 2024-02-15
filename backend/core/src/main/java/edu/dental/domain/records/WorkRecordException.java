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
        logger.log(Level.SEVERE, buildStackMessage(e.getStackTrace(), Level.SEVERE));
    }

    public WorkRecordException(Exception e, Level level) {
        super(e);
        logger.log(level, buildStackMessage(e.getStackTrace(), level));
    }


    private static String buildStackMessage(StackTraceElement[] stackTrace, Level level) {
        StringBuilder str = new StringBuilder();
        if (level.intValue() > Level.INFO.intValue()) {
            for (StackTraceElement e : stackTrace) {
                str.append(e.toString()).append("\n");
            }
        } else {
            str.append(stackTrace[0]).append("\n");
            str.append(stackTrace[1]).append("\n");
            str.append(stackTrace[2]).append("\n");
        }
        return str.toString();
    }
}