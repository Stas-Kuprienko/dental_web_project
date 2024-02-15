package edu.dental.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public enum WebUtility {

    INSTANCE;

    public final String attribUser = "user";
    public final String attribToken = "token";
    public final String attribWorks = "works";
    public final String attribWork = "work";
    public final String attribMap = "map";

    private static final String apiUrl = "http://localhost:8080/dental-api/";

    private final HttpRequester requestSender;
    private final Gson jsonParser;


    WebUtility() {
        this.requestSender = new HttpRequester(apiUrl);
        this.jsonParser = new GsonBuilder().create();
    }


    public static String XSSEscape(String value) {
        return value.replace("&", "&amp;")
                .replace(">", "&gt;")
                .replace("<", "&lt;");
    }

    public <T> String parseToJson(T object) {
        return jsonParser.toJson(object);
    }

    public <T> T parseFromJson(String json, Class<T> clas) {
        return jsonParser.fromJson(json, clas);
    }

    public HttpRequester requestSender() {
        return requestSender;
    }
}
