package edu.dental.service;

import com.google.gson.Gson;

public final class JsonObjectParser {

    private JsonObjectParser() {}
    static {
        parser = new Gson();
    }

    public static final Gson parser;

}
