package edu.dental.security;

import edu.dental.domain.APIManager;

import java.util.logging.Level;
import java.util.logging.Logger;

public class WebSecurityException extends Exception {

    private static final Logger logger;

    static {
        logger = Logger.getLogger(AuthenticationService.class.getName());
        logger.addHandler(APIManager.fileHandler);
        logger.setLevel(Level.ALL);
    }

    public final int code;

    public WebSecurityException(Level level, AuthenticationService.ERROR error, Exception e) {
        super(e);
        this.code = error.code;
        logger.log(level, buildStackMessage(e.getStackTrace()));
    }

    public WebSecurityException(Level level, AuthenticationService.ERROR error, String message) {
        this.code = error.code;
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
