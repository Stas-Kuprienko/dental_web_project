package edu.dental.web.my_repository;

import com.google.gson.Gson;
import edu.dental.domain.entities.DentalWork;
import edu.dental.domain.entities.dto.DentalWorkDTO;
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

    @Override
    public String addNewWork(String json, DentalWork dw) {
        DentalWorkDTO[] workDTOs = (DentalWorkDTO[]) parseFromJson(json, DentalWorkDTO[].class);
        DentalWorkDTO dto = new DentalWorkDTO(dw);
        DentalWorkDTO[] result = new DentalWorkDTO[workDTOs.length + 1];
        System.arraycopy(workDTOs, 0, result, 0, workDTOs.length);
        result[workDTOs.length] = dto;
        return parseToJson(result);
    }
}
