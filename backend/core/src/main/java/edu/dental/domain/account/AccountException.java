package edu.dental.domain.account;

import edu.dental.domain.APIManager;
import edu.dental.domain.records.WorkRecordBook;

import java.util.logging.Level;
import java.util.logging.Logger;

public class AccountException extends Exception{

    private static final Logger logger;

    static {
        logger = Logger.getLogger(WorkRecordBook.class.getName());
        logger.addHandler(APIManager.fileHandler);
        logger.setLevel(Level.ALL);
    }

    public AccountException(CAUSE cause, Exception e) {
        this.cause = cause;
        this.message = e.getMessage();
        logger.log(Level.SEVERE, e.getMessage());
    }

    public AccountException(CAUSE cause, MESSAGE message) {
        this.cause = cause;
        this.message = message.message;
        String str = "Error code: " + cause.code + ";\n";
        logger.log(Level.SEVERE, str + message.message);
    }

    public final CAUSE cause;
    public final String message;

    public enum CAUSE {
        SERVER_ERROR(500),
        BAD_REQUEST(400),
        UNAUTHORIZED(401),
        FORBIDDEN(403),
        NOT_FOUND(404);


        public final int code;
        CAUSE(int code) {
            this.code = code;
        }
    }

    public enum MESSAGE {
        USER_NOT_FOUND("user is not found"),
        PASSWORD_INVALID("password is invalid"),
        DATABASE_ERROR("happens database error"),
        TOKEN_INVALID("token is unrecognized");

        public final String message;
        MESSAGE(String message) {
            this.message = message;
        }
    }
}
