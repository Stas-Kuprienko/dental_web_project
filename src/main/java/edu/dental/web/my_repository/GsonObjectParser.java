package edu.dental.web.my_repository;

import com.google.gson.Gson;
import edu.dental.web.JsonObjectParser;

public class GsonObjectParser implements JsonObjectParser {

    private final Gson parser;

    private GsonObjectParser() {
        this.parser = new Gson();
    }


    @Override
    public String parseToJson(Object o) {
        return parser.toJson(o);
    }

    @Override
    public Object parseFromJson(String s, Class<?> clas) {
        return parser.fromJson(s, clas);
    }
}
