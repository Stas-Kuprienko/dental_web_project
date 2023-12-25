package edu.dental.domain.authentication;

public class AuthenticationException extends Exception {


    public AuthenticationException(String message) {
        super(message);
    }

    public AuthenticationException(Causes cause) {
        this(cause.cause);
        this.cause = cause;
    }

    public Causes cause;

    public enum Causes {

        NULL("The given argument is null or empty.", 400),

        NO("The specified user is not found.", 404),

        PASS("The specified password is incorrect.", 403),

        ERROR("Happen exception on server.", 500);


        public final String cause;
        public final int code;
        Causes(String cause, int code) {
            this.cause = cause;
            this.code = code;
        }
    }
}
