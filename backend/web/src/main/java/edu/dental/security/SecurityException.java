package edu.dental.security;

import edu.dental.domain.APIManager;

import java.util.logging.Level;
import java.util.logging.Logger;

public class SecurityException extends Exception {

    private static final Logger logger;

    static {
        logger = Logger.getLogger(AuthenticationService.class.getName());
        logger.addHandler(APIManager.fileHandler);
        logger.setLevel(Level.ALL);
    }

    public SecurityException(Exception e) {
        super(e);
        logger.log(Level.SEVERE, buildStackMessage(e.getStackTrace()));
    }

    public SecurityException(String message) {
        super(message);
        logger.log(Level.SEVERE, message);
    }

    public SecurityException(Level level, Exception e) {
        super(e);
        logger.log(level, buildStackMessage(e.getStackTrace()));
    }


    private static String buildStackMessage(StackTraceElement[] stackTrace) {
        StringBuilder str = new StringBuilder();
        for (StackTraceElement e : stackTrace) {
            str.append(e.toString()).append("\n");
        }
        return str.toString();
    }
}
