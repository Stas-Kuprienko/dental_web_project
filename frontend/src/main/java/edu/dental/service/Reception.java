package edu.dental.service;

import edu.dental.WebUtility;
import edu.dental.beans.DentalWork;
import edu.dental.beans.ProductMap;
import edu.dental.beans.UserDto;

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
        WebUtility.QueryFormer queryFormer = new WebUtility.QueryFormer();

        queryFormer.add(paramEmail, email);
        queryFormer.add(paramPassword, password);
        String requestParameters = queryFormer.form();

        return get(requestParameters);
    }

    public UserDto getByToken(String token) throws IOException {
        WebUtility.QueryFormer queryFormer = new WebUtility.QueryFormer();

        queryFormer.add(paramToken, token);
        String requestParameters = queryFormer.form();

        return get(requestParameters);
    }

    private UserDto get(String requestParameters) throws IOException {
        String jsonUser = WebUtility.INSTANCE.requestSender().sendHttpPostRequest(logInUrl, requestParameters);
        UserDto user = WebUtility.INSTANCE.parseFromJson(jsonUser, UserDto.class);

        String jsonWorks = WebUtility.INSTANCE.requestSender().sendHttpGetRequest(user.jwt(), dentalWorkListUrl);
        List<DentalWork> works = List.of(WebUtility.INSTANCE.parseFromJson(jsonWorks, DentalWork[].class));

        String jsonMap = WebUtility.INSTANCE.requestSender().sendHttpGetRequest(user.jwt(), productMapUrl);
        List<ProductMap.Item> items = List.of(WebUtility.INSTANCE.parseFromJson(jsonMap, ProductMap.Item[].class));
        ProductMap map = new ProductMap(items);

        WebRepository.INSTANCE.setAccount(user, works, map);
        return user;
    }


    public static synchronized Reception getInstance() {
        return instance;
    }
}
