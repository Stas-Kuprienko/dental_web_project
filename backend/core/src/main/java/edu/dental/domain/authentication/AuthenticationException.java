package edu.dental.domain.authentication;

import org.apache.log4j.Logger;

public class AuthenticationException extends Exception {

    private static final Logger logger;
    //TODO
    static {
        logger = Logger.getLogger("");
    }

    public AuthenticationException(String message) {
        super(message);
    }

    public AuthenticationException(Exception e) {
        super(e);
        logger.info(e);
    }

    public AuthenticationException(Causes cause) {
        this(cause.cause);
        logger.info(cause);
        this.causes = cause;
    }

    public Causes causes;

    public enum Causes {

        NULL("The given argument is null or empty."),

        NO("The specified user is not found."),

        PASS("The specified password is incorrect."),

        ERROR("Happen exception on server.");


        public final String cause;
        Causes(String cause) {
            this.cause = cause;
        }
    }
}
