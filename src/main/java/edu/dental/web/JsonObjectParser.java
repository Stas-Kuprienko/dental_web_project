package edu.dental.web;

import edu.dental.domain.APIManager;
import edu.dental.domain.entities.DentalWork;

public interface JsonObjectParser {

    static JsonObjectParser getInstance() {
        return APIManager.INSTANCE.getJsonParser();
    }

    String parseToJson(Object o);

    Object parseFromJson(String s, Class<?> clas);

    String addNewWork(String json, DentalWork dw);
}
