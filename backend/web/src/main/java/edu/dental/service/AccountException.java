package edu.dental.service;

import edu.dental.domain.APIManager;

import java.util.logging.Level;
import java.util.logging.Logger;

public class AccountException extends Exception{

    private static final Logger logger;

    static {
        logger = Logger.getLogger(Repository.class.getName());
        logger.addHandler(APIManager.fileHandler);
        logger.setLevel(Level.ALL);
    }

    public AccountException(Exception e) {
        super(e);
        logger.log(Level.SEVERE, buildStackMessage(e.getStackTrace()));
    }

    public AccountException(String message) {
        logger.log(Level.SEVERE, message);
    }

    public AccountException(Level level, Exception e) {
        super(e);
        logger.log(level, buildStackMessage(e.getStackTrace()));
    }

    public AccountException(Level level, String message) {
        logger.log(level, message);
    }


    private static String buildStackMessage(StackTraceElement[] stackTrace) {
        StringBuilder str = new StringBuilder();
        for (StackTraceElement e : stackTrace) {
            str.append(e.toString()).append("\n");
        }
        return str.toString();
    }
}