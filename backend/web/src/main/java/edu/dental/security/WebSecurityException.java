package edu.dental.security;

import edu.dental.domain.APIManager;
import stas.utilities.LoggerKit;

import java.util.logging.Level;

public class WebSecurityException extends Exception {

    private static final LoggerKit loggerKit;

    static {
        loggerKit = new LoggerKit(APIManager.getFileHandler());
        loggerKit.addLogger(AuthenticationService.class);
    }

    public final int code;

    public WebSecurityException(Level level, AuthenticationService.ERROR error, Exception e) {
        this.code = error.code;
        loggerKit.doLog(AuthenticationService.class, e, level);
    }
}
