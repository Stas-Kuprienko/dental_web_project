package edu.dental.domain.records;


import edu.dental.domain.APIManager;
import stas.utilities.LoggerKit;

import java.util.logging.Level;

public class WorkRecordException extends Exception {

    private static final LoggerKit loggerKit;

    static {
        loggerKit = new LoggerKit(APIManager.getFileHandler());
        loggerKit.addLogger(WorkRecordBook.class);
    }

    public WorkRecordException(Exception e) {
        super(e);
        loggerKit.doLog(WorkRecordBook.class, e, Level.SEVERE);
    }

    public WorkRecordException(Exception e, Level level) {
        super(e);
        loggerKit.doLog(WorkRecordBook.class, e, level);
    }
}