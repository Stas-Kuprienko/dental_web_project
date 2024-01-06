package edu.dental;

import edu.dental.service.HttpRequestSender;

public enum WebAPI {

    INSTANCE;

    public final String sessionAttribute = "user";

    private static final String apiUrl = "http://localhost:8080/dental-api/";

    private final HttpRequestSender requestSender;


    WebAPI() {
        this.requestSender = new HttpRequestSender(apiUrl);
    }


    public HttpRequestSender requestSender() {
        return requestSender;
    }
}
