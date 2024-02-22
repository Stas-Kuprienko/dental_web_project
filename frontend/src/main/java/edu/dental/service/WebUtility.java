package edu.dental.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import stas.http_tools.HttpRequester;
import stas.utilities.LoggerKit;

public enum WebUtility {

    INSTANCE;

    public final String attribUser = "user";
    public final String attribToken = "token";
    public final String attribWorks = "works";
    public final String attribWork = "work";
    public final String attribMap = "map";

    public final String errorPageURL = "/error";

    private final LoggerKit loggerKit;
    private final Gson jsonParser;
    private HttpRequester requestSender;


    WebUtility() {
        this.loggerKit = WebAPIManager.getLoggerKit();
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

    public HttpRequester getRequestSender() {
        return requestSender;
    }

    public void setRequestSender(HttpRequester requestSender) {
        this.requestSender = requestSender;
    }

    public LoggerKit loggerKit() {
        return loggerKit;
    }
}
