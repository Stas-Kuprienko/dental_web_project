package edu.dental.servlets.control;

import edu.dental.WebAPI;
import edu.dental.beans.DentalWork;
import edu.dental.beans.ProductMap;
import edu.dental.beans.UserDto;
import edu.dental.service.HttpRequestSender;
import edu.dental.service.WebRepository;

import java.io.IOException;
import java.util.List;

public final class Reception {

    static {
        instance = new Reception();
    }
    private Reception() {}
    private static final Reception instance;

    public final String paramEmail = "email";
    public final String paramPassword = "password";
    public final String paramToken = "token";
    public final String logInUrl = "log-in";
    public final String dentalWorkListUrl = "main/dental-works";
    public final String productMapUrl = "main/product-map";


    public UserDto getByLogin(String email, String password) throws IOException {
        HttpRequestSender.QueryFormer queryFormer = new HttpRequestSender.QueryFormer();

        queryFormer.add(paramEmail, email);
        queryFormer.add(paramPassword, password);
        String requestParameters = queryFormer.form();

        return get(requestParameters);
    }

    public UserDto getByToken(String token) throws IOException {
        HttpRequestSender.QueryFormer queryFormer = new HttpRequestSender.QueryFormer();

        queryFormer.add(paramToken, token);
        String requestParameters = queryFormer.form();

        return get(requestParameters);
    }

    private UserDto get(String requestParameters) throws IOException {
        String jsonUser = WebAPI.INSTANCE.requestSender().sendHttpPostRequest(logInUrl, requestParameters);
        UserDto user = WebAPI.INSTANCE.parseFromJson(jsonUser, UserDto.class);

        String jsonWorks = WebAPI.INSTANCE.requestSender().sendHttpGetRequest(user.jwt(), dentalWorkListUrl);
        List<DentalWork> works = List.of(WebAPI.INSTANCE.parseFromJson(jsonWorks, DentalWork[].class));

        String jsonMap = WebAPI.INSTANCE.requestSender().sendHttpGetRequest(user.jwt(), productMapUrl);
        List<ProductMap.Item> items = List.of(WebAPI.INSTANCE.parseFromJson(jsonMap, ProductMap.Item[].class));
        ProductMap map = new ProductMap(items);

        WebRepository.INSTANCE.setAccount(user, works, map);
        return user;
    }


    public static synchronized Reception getInstance() {
        return instance;
    }
}
