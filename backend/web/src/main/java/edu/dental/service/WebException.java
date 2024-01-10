package edu.dental.service;

public class WebException extends Exception {

    public WebException(CODE code) {
        this.code = code;
    }

    public WebException(String message, CODE code) {
        super(message);
        this.code = code;
    }

    public final CODE code;

    public enum CODE {
        SERVER_ERROR(500),
        BAD_REQUEST(400),
        UNAUTHORIZED(401),
        FORBIDDEN(403);


        public final int i;
        CODE(int i) {
            this.i = i;
        }
    }
}
