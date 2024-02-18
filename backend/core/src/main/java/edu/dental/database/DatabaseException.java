package edu.dental.database;

import edu.dental.database.dao.DAO;
import edu.dental.domain.APIManager;
import stas.utilities.LoggerKit;

import java.util.logging.Level;

public class DatabaseException extends Exception {

    private static final LoggerKit loggerKit;

    static {
        loggerKit = new LoggerKit(APIManager.getFileHandler());
        loggerKit.addLogger(DAO.class);
    }

    public DatabaseException(Exception e) {
        super(e);
        loggerKit.doLog(DAO.class, e, Level.SEVERE);
    }

    public DatabaseException(Exception e, Level level) {
        super(e);
        loggerKit.doLog(DAO.class, e, level);
    }

    public static void logging(Exception e) {
        loggerKit.doLog(DAO.class, e, Level.SEVERE);
    }
}