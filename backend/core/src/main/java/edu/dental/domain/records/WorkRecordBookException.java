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
        logger.log(Level.SEVERE, e.getMessage());
    }
}