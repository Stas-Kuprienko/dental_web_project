package edu.dental.domain.authentication;

public class AuthenticationException extends Exception {

    public AuthenticationException(String message) {
        super(message);
    }

    public AuthenticationException(Exception e) {
        super(e);
    }

    public AuthenticationException(Causes cause) {
        this(cause.cause);
        this.causes = cause;
    }

    public Causes causes;

    public enum Causes {

        NULL("The given argument is null or empty."),

        NO("The specified user is not found."),

        PASS("The specified password is incorrect.");


        public final String cause;
        Causes(String cause) {
            this.cause = cause;
        }
    }
}
