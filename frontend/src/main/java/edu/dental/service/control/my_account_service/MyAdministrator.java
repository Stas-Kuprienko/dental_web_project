package edu.dental.service.control.my_account_service;

import edu.dental.APIResponseException;
import edu.dental.beans.DentalWork;
import edu.dental.beans.ProductMap;
import edu.dental.beans.UserBean;
import edu.dental.service.WebUtility;
import edu.dental.service.control.Administrator;
import edu.dental.service.control.DentalWorksService;
import edu.dental.service.control.ProductMapService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public final class MyAdministrator implements Administrator {

    private static final String signUpUrl = "sign-up";
    private static final String logInUrl = "log-in";
    private static final String dentalWorkListUrl = "main/dental-works";
    private static final String productMapUrl = "main/product-map";
    private static final String paramName = "name";
    private static final String paramEmail = "email";
    private static final String paramPassword = "password";

    private final DentalWorksService dentalWorksService;
    private final ProductMapService productMapService;
    private final WebUtility.HttpRequestSender httpRequestSender;


    private MyAdministrator() {
        this.dentalWorksService = new MyDentalWorksService();
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

            session.setAttribute(WebUtility.INSTANCE.sessionUser, user.getId());
            session.setAttribute(WebUtility.INSTANCE.sessionWorks, new DentalWork[]{});
            session.setAttribute(WebUtility.INSTANCE.sessionMap, new ProductMap.Item[]{});
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

        session.setAttribute(WebUtility.INSTANCE.sessionUser, user.getId());
        session.setAttribute(WebUtility.INSTANCE.sessionToken, user.getJwt());

        setWorkList(session);
        setProductMap(session);
    }

    @Override
    public DentalWork createWork(HttpSession session, String patient, String clinic, String product, int quantity, String complete) throws IOException, APIResponseException {
        return null;
    }

    @Override
    public DentalWork updateDentalWork(HttpSession session, int id, String field, String value, String quantity) throws IOException, APIResponseException, ServletException {
        return null;
    }

    @Override
    public DentalWork getDentalWorkById(HttpSession session, int id) throws IOException, APIResponseException {
        return null;
    }

    @Override
    public void updateDentalWorkList(HttpSession session, DentalWork dw) {

    }

    @Override
    public void deleteDentalWorkFromList(HttpSession session, int id) throws IOException, APIResponseException {

    }

    @Override
    public DentalWork removeProductFromDentalWork(HttpSession session, int id, String product) throws IOException, APIResponseException {
        return null;
    }

    @Override
    public void createProductItem(HttpSession session, String title, int price) {

    }

    @Override
    public void updateProductItem(HttpSession session, int id, String title, int price) {

    }

    @Override
    public void deleteProductItem(HttpSession session, int id, String title) {

    }


    @Override
    public DentalWorksService getDentalWorksService() {
        return dentalWorksService;
    }


    private void setWorkList(HttpSession session) throws APIResponseException {
        String token = (String) session.getAttribute(WebUtility.INSTANCE.sessionToken);

        String jsonWorks;
        try {
            jsonWorks = httpRequestSender.sendHttpGetRequest(token, dentalWorkListUrl);
        } catch (IOException e) {
            throw new APIResponseException(500, e.getMessage());
        }
        DentalWork[] works = WebUtility.INSTANCE.parseFromJson(jsonWorks, DentalWork[].class);

        session.setAttribute(WebUtility.INSTANCE.sessionWorks, works);
    }

    private void setProductMap(HttpSession session) throws APIResponseException {
        String token = (String) session.getAttribute(WebUtility.INSTANCE.sessionToken);

        String jsonMap;
        try {
            jsonMap = WebUtility.INSTANCE.requestSender().sendHttpGetRequest(token, productMapUrl);
        } catch (IOException e) {
            throw new APIResponseException(500, e.getMessage());
        }
        ProductMap.Item[] map = WebUtility.INSTANCE.parseFromJson(jsonMap, ProductMap.Item[].class);
        session.setAttribute(WebUtility.INSTANCE.sessionMap, map);
    }
}
