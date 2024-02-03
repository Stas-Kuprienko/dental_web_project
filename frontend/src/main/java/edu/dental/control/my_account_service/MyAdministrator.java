package edu.dental.control.my_account_service;

import edu.dental.APIResponseException;
import edu.dental.beans.DentalWork;
import edu.dental.beans.ProductMap;
import edu.dental.beans.UserBean;
import edu.dental.control.Administrator;
import edu.dental.control.DentalWorkService;
import edu.dental.control.DentalWorksListService;
import edu.dental.service.WebUtility;
import edu.dental.control.ProductMapService;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Arrays;

public final class MyAdministrator implements Administrator {

    private static final String signUpUrl = "sign-up";
    private static final String logInUrl = "log-in";
    private static final String accountUrl = "main/account";
    private static final String dentalWorkListUrl = "main/dental-works";
    private static final String productMapUrl = "main/product-map";
    private static final String paramName = "name";
    private static final String paramEmail = "email";
    private static final String paramPassword = "password";
    private static final String fieldParam = "field";
    private static final String valueParam = "value";

    private final DentalWorksListService dentalWorksListService;
    private final DentalWorkService dentalWorkService;
    private final ProductMapService productMapService;
    private final WebUtility.HttpRequestSender httpRequestSender;


    private MyAdministrator() {
        this.dentalWorksListService = new MyDentalWorksListService();
        this.dentalWorkService = new MyDentalWorkService();
        this.productMapService = new MyProductMapService();
        this.httpRequestSender = WebUtility.INSTANCE.requestSender();
    }


    @Override
    public void signUp(HttpSession session, String name, String email, String password) throws APIResponseException {
        try {
            WebUtility.QueryFormer queryFormer = new WebUtility.QueryFormer();

            queryFormer.add(paramName, name);
            queryFormer.add(paramEmail, email);
            queryFormer.add(paramPassword, password);
            String requestParameters = queryFormer.form();

            String jsonUser = httpRequestSender.sendHttpPostRequest(signUpUrl, requestParameters);
            UserBean user = WebUtility.INSTANCE.parseFromJson(jsonUser, UserBean.class);

            session.setAttribute(WebUtility.INSTANCE.attribToken, user.getJwt());
            session.setAttribute(WebUtility.INSTANCE.attribWorks, new DentalWork[]{});
            session.setAttribute(WebUtility.INSTANCE.attribMap, new ProductMap.Item[]{});
        } catch (IOException e) {
            throw new APIResponseException(500, e.getMessage());
        }
    }

        @Override
    public void signIn(HttpSession session, String email, String password) throws APIResponseException {
        WebUtility.QueryFormer queryFormer = new WebUtility.QueryFormer();

        queryFormer.add(paramEmail, email);
        queryFormer.add(paramPassword, password);
        String requestParameters = queryFormer.form();

        String jsonUser;
        try {
            jsonUser = httpRequestSender.sendHttpPostRequest(logInUrl, requestParameters);
        } catch (IOException e) {
            throw new APIResponseException(400, e.getMessage());
        }
        UserBean user =  WebUtility.INSTANCE.parseFromJson(jsonUser, UserBean.class);

        session.setAttribute(WebUtility.INSTANCE.attribToken, user.getJwt());

        setWorkList(session);
        setProductMap(session);
    }

    @Override
    public UserBean getUser(String token) throws IOException, APIResponseException {
        String json = httpRequestSender.sendHttpGetRequest(token, accountUrl);
        return WebUtility.INSTANCE.parseFromJson(json, UserBean.class);
    }

    @Override
    public UserBean updateUser(String token, String field, String value) throws IOException, APIResponseException {
        WebUtility.QueryFormer former = new WebUtility.QueryFormer();
        former.add(fieldParam, field);
        former.add(valueParam, value);
        String requestParam = former.form();

        String json = WebUtility.INSTANCE.requestSender().sendHttpPostRequest(token, accountUrl, requestParam);
        return WebUtility.INSTANCE.parseFromJson(json, UserBean.class);
    }

    @Override
    public DentalWorksListService getDentalWorksListService() {
        return dentalWorksListService;
    }

    @Override
    public DentalWorkService getDentalWorksService() {
        return dentalWorkService;
    }

    @Override
    public ProductMapService getProductMapService() {
        return productMapService;
    }

    private void setWorkList(HttpSession session) throws APIResponseException {
        String token = (String) session.getAttribute(WebUtility.INSTANCE.attribToken);

        String jsonWorks;
        try {
            jsonWorks = httpRequestSender.sendHttpGetRequest(token, dentalWorkListUrl);
        } catch (IOException e) {
            throw new APIResponseException(500, e.getMessage());
        }
        DentalWork[] works = WebUtility.INSTANCE.parseFromJson(jsonWorks, DentalWork[].class);
        Arrays.sort(works);
        session.setAttribute(WebUtility.INSTANCE.attribWorks, works);
    }

    private void setProductMap(HttpSession session) throws APIResponseException {
        String token = (String) session.getAttribute(WebUtility.INSTANCE.attribToken);

        String jsonMap;
        try {
            jsonMap = WebUtility.INSTANCE.requestSender().sendHttpGetRequest(token, productMapUrl);
        } catch (IOException e) {
            throw new APIResponseException(500, e.getMessage());
        }
        ProductMap.Item[] map = WebUtility.INSTANCE.parseFromJson(jsonMap, ProductMap.Item[].class);
        session.setAttribute(WebUtility.INSTANCE.attribMap, map);
    }
}
