package edu.dental.web;

import edu.dental.WebAPI;

public interface JsonObjectParser {

    static JsonObjectParser getInstance() {
        return WebAPI.INSTANCE.getJsonParser();
    }

    String parseToJson(Object o);

    Object parseFromJson(String s, Class<?> clas);
}
