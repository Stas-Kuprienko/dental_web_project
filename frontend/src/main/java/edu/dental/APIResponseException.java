package edu.dental;

import edu.dental.service.WebAPIManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.logging.Level;

public class APIResponseException extends Exception {

    private static final String errorPageURL = "/error";
    private static final String messageAttrib = "message";
    private static final String headerAttrib = "header";
    private static final String codeAttrib = "code";

    public final int CODE;

    public final ERROR error;


    public APIResponseException(int CODE) {
        this.CODE = CODE;
        this.error = choose(CODE);
    }

    public APIResponseException(ERROR error) {
        this.CODE = error.code;
        this.error = error;
    }

    public APIResponseException(ERROR error, StackTraceElement[] stackTrace) {
        this.CODE = error.code;
        this.error = error;
        String message = buildStackMessage(stackTrace);
        WebAPIManager.INSTANCE.getLoggerKit()
                .doLogging(HttpServlet.class, message + '\n' + error.message, Level.INFO);
    }

    public void errorRedirect(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.setAttribute(codeAttrib, error.code);
        request.setAttribute(headerAttrib, error.header);
        request.setAttribute(messageAttrib, error.message);
        request.getRequestDispatcher(errorPageURL).forward(request, response);
    }

    private static ERROR choose(int code) {
        for (ERROR e : ERROR.values()) {
            if (e.code == code) {
                return e;
            }
        }
        return ERROR.SERVER_ERROR;
    }

    private static String buildStackMessage(StackTraceElement[] stackTrace) {
        StringBuilder str = new StringBuilder();
        for (StackTraceElement e : stackTrace) {
            str.append(e.toString()).append("\n");
        }
        return str.toString();
    }

    public enum ERROR {

        SERVER_ERROR(500, "Internal Server Error", "Sorry, an error occurred on the server"),
        BAD_REQUEST(400, "Bad Request", "Sorry, you have made an incorrect request"),
        UNAUTHORIZED(401, "Unauthorized", "Sorry, the request is made by an unauthorized user"),
        FORBIDDEN(403, "Forbidden", "Sorry, you cannot make this request"),
        NOT_FOUND(404, "Not Found", "Sorry, the required user is not found"),
        NOT_ALLOWED(405, "Not Allowed", "Sorry, the method cannot be applied to the current resource");


        public final int code;
        public final String header;
        public final String message;

        ERROR(int code, String header, String message) {
            this.code = code;
            this.header = header;
            this.message = message;
        }
    }
}