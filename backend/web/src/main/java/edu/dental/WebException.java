package edu.dental;

import edu.dental.domain.APIManager;
import edu.dental.domain.account.AccountException;
import jakarta.servlet.http.HttpServlet;

import java.util.logging.Level;
import java.util.logging.Logger;

public class WebException extends Exception {

    private static final Logger logger;

    static {
        logger = Logger.getLogger(HttpServlet.class.getName());
        logger.addHandler(APIManager.fileHandler);
        logger.setLevel(Level.ALL);
    }

    public WebException(AccountException.CAUSE cause, Exception e) {
        this.cause = cause;
        this.message = e.getMessage();
    }

    public WebException(AccountException.CAUSE cause, AccountException.MESSAGE message) {
        this.cause = cause;
        this.message = message.message;
    }

    public final AccountException.CAUSE cause;
    public final String message;
}
