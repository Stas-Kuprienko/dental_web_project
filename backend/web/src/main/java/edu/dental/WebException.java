package edu.dental;

import edu.dental.domain.APIManager;
import jakarta.servlet.http.HttpServlet;

import java.util.logging.Level;
import java.util.logging.Logger;

public class WebException extends Exception {

    private static final Logger logger;

    static {
        logger = Logger.getLogger(HttpServlet.class.getName());
        logger.addHandler(APIManager.fileHandler);
        logger.setLevel(Level.SEVERE);
    }

    public WebException(CODE code, Exception e) {
        this.code = code;
        this.message = e.getMessage();
        logger.log(Level.INFO, e.getMessage());
    }

    public WebException(CODE code, String message) {
        this.code = code;
        this.message = message;
        logger.log(Level.INFO, message);
    }

    public WebException(CODE code) {
        this.code = code;
    }

    public final CODE code;
    public String message;


    public enum CODE {
        SERVER_ERROR(500),
        BAD_REQUEST(400),
        UNAUTHORIZED(401),
        FORBIDDEN(403),
        NOT_FOUND(404);


        public final int code;
        CODE(int code) {
            this.code = code;
        }
    }
}
