package edu.dental.domain.reports;

import edu.dental.domain.APIManager;
import edu.dental.domain.records.WorkRecordBook;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ReportServiceException extends Exception {

    private static final Logger logger;

    static {
        logger = Logger.getLogger(WorkRecordBook.class.getName());
        logger.addHandler(APIManager.fileHandler);
        logger.setLevel(Level.ALL);
    }

    public ReportServiceException(Exception e) {
        logger.log(Level.SEVERE, e.getMessage());
    }
}
