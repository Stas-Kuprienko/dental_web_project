package edu.dental.domain.reports;

import edu.dental.domain.APIManager;
import stas.utilities.LoggerKit;

import java.util.logging.Level;

public class ReportException extends Exception {

    private static final LoggerKit loggerKit;

    static {
        loggerKit = new LoggerKit(APIManager.getFileHandler());
        loggerKit.addLogger(ReportService.class);
    }
    public ReportException(Exception e) {
        super(e);
        loggerKit.doLog(ReportService.class, e, Level.SEVERE);
    }
}
