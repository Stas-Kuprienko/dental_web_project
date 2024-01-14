package edu.dental;

public class APIResponseException extends Exception {

    public final int CODE;
    public final String MESSAGE;

    public APIResponseException(int CODE, String MESSAGE) {
        super();
        this.CODE = CODE;
        this.MESSAGE = MESSAGE;
    }
}
