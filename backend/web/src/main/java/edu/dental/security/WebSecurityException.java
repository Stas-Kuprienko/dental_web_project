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
        this.code = error.code;
        logger.log(level, buildStackMessage(e.getStackTrace(), level, e));
    }


    private static String buildStackMessage(StackTraceElement[] stackTrace, Level level, Exception e) {
        StringBuilder str = new StringBuilder();
        if (level.intValue() < Level.WARNING.intValue()) {
            str.append(stackTrace[0].toString()).append(" - ")
                    .append(e.getMessage()).append('\n');
        } else {
            for (StackTraceElement st : stackTrace) {
                str.append(st.toString()).append('\n');
            }
        }
        return str.toString();
    }
}
