package edu.dental;

public class APIResponseException extends Exception {

    public final int CODE;
    public String MESSAGE;

    public APIResponseException(int CODE, String MESSAGE) {
        super();
        this.CODE = CODE;
        this.MESSAGE = MESSAGE;
    }

    public APIResponseException(int CODE) {
        super();
        this.CODE = CODE;
    }
}
//
//try {
//        // Code that may throw an exception
//        } catch (Exception e) {
//        request.setAttribute("message", "An error occurred: " + e.getMessage());
//        request.getRequestDispatcher("/error.jsp").forward(request, response);
//        }