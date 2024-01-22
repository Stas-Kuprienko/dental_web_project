package edu.dental.service.my_repository;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.dental.service.tools.JsonObjectParser;

public class MyJsonObjectParser implements JsonObjectParser {

    private final Gson parser;

    private MyJsonObjectParser() {
        this.parser = new GsonBuilder().create();
    }


    @Override
    public String parseToJson(Object o) {
        return parser.toJson(o);
    }

    @Override
    public <T> T parseFromJson(String s, Class<T> clas) {
        return parser.fromJson(s, clas);
    }
}
