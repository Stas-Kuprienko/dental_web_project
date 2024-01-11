package edu.dental.service.my_repository;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.dental.dto.*;
import edu.dental.entities.*;
import edu.dental.service.tools.JsonObjectParser;

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

    @Override
    public String parseToDtoThenToJson(Object o) {
        //TODO
        return "";
    }


    private class DtoAdapter <T> {

        private final T object;

        private DtoAdapter(T object) {
            //TODO
            this.object = object;
        }

        private UserDto adapt(User user) {
            return UserDto.parse(user);
        }

        private DentalWorkDto adapt(DentalWork dw) {
            return new DentalWorkDto(dw);
        }

        private ProductMapDto adapt(ProductMap map) {
            return new ProductMapDto(map);
        }

        private ProductDto adapt(Product product) {
            return ProductDto.parse(product);
        }

        private SalaryRecordDto adapt(SalaryRecord sr) {
            return SalaryRecordDto.parse(sr);
        }
    }
}
