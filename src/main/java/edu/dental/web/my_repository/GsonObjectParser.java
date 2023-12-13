package edu.dental.web.my_repository;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import edu.dental.web.JsonObjectParser;

import java.util.List;

public class GsonObjectParser implements JsonObjectParser {

    private final Gson parser;

    private GsonObjectParser() {
        this.parser = new GsonBuilder().create();
    }


    @Override
    public String parseToJson(Object o) {
        return parser.toJson(o);
    }

    @Override
    public Object parseFromJson(String s, Class<?> clas) {
        return parser.fromJson(s, clas);
    }

    public String listToJson(String key, List<?> list) {
        JsonArray jsonArray = parser.toJsonTree(list).getAsJsonArray();
        JsonObject json = new JsonObject();
        json.add(key, jsonArray);
        return json.toString();
    }
}
