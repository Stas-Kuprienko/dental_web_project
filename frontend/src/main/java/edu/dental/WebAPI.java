package edu.dental;

import edu.dental.servlets.RequestSender;

public enum WebAPI {

    INSTANCE;

    private static final String apiUrl = "http://localhost:8080/dental-api/";

    private final RequestSender requestSender;


    WebAPI() {
        this.requestSender = new RequestSender(apiUrl);
    }


    public RequestSender requestSender() {
        return requestSender;
    }
}
