package edu.dental.service;

import edu.dental.WebAPI;

public interface JsonObjectParser {

    static JsonObjectParser getInstance() {
        return WebAPI.INSTANCE.getJsonParser();
    }

    String parseToJson(Object o);

    <T> T parseFromJson(String s, Class<T> clas);
}
