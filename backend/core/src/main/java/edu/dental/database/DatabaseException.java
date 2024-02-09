package edu.dental.database;

import edu.dental.database.dao.DAO;
import edu.dental.domain.APIManager;

import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseException extends Exception {

    private static final Logger logger;

    static {
        logger = Logger.getLogger(DAO.class.getName());
        logger.addHandler(APIManager.fileHandler);
        logger.setLevel(Level.ALL);
    }

    public DatabaseException(Exception e) {
        super(e);
        logger.log(Level.WARNING, buildStackMessage(e.getStackTrace()));
    }

    public static void logging(Exception e) {
        logger.log(Level.SEVERE, buildStackMessage(e.getStackTrace()));
    }


    private static String buildStackMessage(StackTraceElement[] stackTrace) {
        StringBuilder str = new StringBuilder();
        for (StackTraceElement e : stackTrace) {
            if (e.toString().startsWith("edu.")) {
                str.append(e).append("\n");
            } else {
                break;
            }
        }
        return str.toString();
    }
}