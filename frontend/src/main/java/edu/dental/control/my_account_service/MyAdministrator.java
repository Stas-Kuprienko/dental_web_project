package edu.dental.control.my_account_service;

import stas.exceptions.HttpWebException;
import edu.dental.beans.DentalWork;
import edu.dental.beans.ProductMap;
import edu.dental.beans.UserBean;
import edu.dental.control.*;
import stas.http_tools.HttpQueryFormer;
import edu.dental.service.WebAPIManager;
import edu.dental.service.WebUtility;
import jakarta.servlet.http.HttpSession;
import stas.http_tools.HttpRequester;

import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;

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
    private final ProfitRecordService profitRecordService;
    private final HttpRequester httpRequestSender;


    private MyAdministrator() {
        this.dentalWorksListService = new MyDentalWorksListService();
        this.dentalWorkService = new MyDentalWorkService();
        this.productMapService = new MyProductMapService();
        this.profitRecordService = new MyProfitRecordService();
        this.httpRequestSender = WebUtility.INSTANCE.requestSender();
    }


    @Override
    public void signUp(HttpSession session, String name, String email, String password) throws HttpWebException {
        try {
            HttpQueryFormer queryFormer = new HttpQueryFormer();

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
            WebAPIManager.INSTANCE.getLoggerKit().doLog(this.getClass().getSuperclass(), e, Level.SEVERE);
            throw new HttpWebException(HttpWebException.ERROR.SERVER_ERROR);
        }
    }

        @Override
    public void signIn(HttpSession session, String email, String password) throws HttpWebException {
        HttpQueryFormer queryFormer = new HttpQueryFormer();

        queryFormer.add(paramEmail, email);
        queryFormer.add(paramPassword, password);
        String requestParameters = queryFormer.form();

        String jsonUser;
        try {
            jsonUser = httpRequestSender.sendHttpPostRequest(logInUrl, requestParameters);
        } catch (IOException e) {
            WebAPIManager.INSTANCE.getLoggerKit().doLog(this.getClass().getSuperclass(), e, Level.SEVERE);
            throw new HttpWebException(HttpWebException.ERROR.SERVER_ERROR);
        }
        UserBean user =  WebUtility.INSTANCE.parseFromJson(jsonUser, UserBean.class);

        session.setAttribute(WebUtility.INSTANCE.attribToken, user.getJwt());

        setWorkList(session);
        setProductMap(session);
    }

    @Override
    public UserBean getUser(String token) throws IOException, HttpWebException {
        String json = httpRequestSender.sendHttpGetRequest(token, accountUrl, null);
        return WebUtility.INSTANCE.parseFromJson(json, UserBean.class);
    }

    @Override
    public UserBean updateUser(String token, String field, String value) throws IOException, HttpWebException {
        HttpQueryFormer former = new HttpQueryFormer();
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

    @Override
    public ProfitRecordService getProfitRecordService() {
        return profitRecordService;
    }

    private void setWorkList(HttpSession session) throws HttpWebException {
        String token = (String) session.getAttribute(WebUtility.INSTANCE.attribToken);

        String jsonWorks;
        try {
            jsonWorks = httpRequestSender.sendHttpGetRequest(token, dentalWorkListUrl, null);
        } catch (IOException e) {
            WebAPIManager.INSTANCE.getLoggerKit().doLog(this.getClass().getSuperclass(), e, Level.SEVERE);
            throw new HttpWebException(HttpWebException.ERROR.SERVER_ERROR);
        }
        DentalWork[] works = WebUtility.INSTANCE.parseFromJson(jsonWorks, DentalWork[].class);
        Arrays.sort(works);
        session.setAttribute(WebUtility.INSTANCE.attribWorks, works);
    }

    private void setProductMap(HttpSession session) throws HttpWebException {
        String token = (String) session.getAttribute(WebUtility.INSTANCE.attribToken);

        String jsonMap;
        try {
            jsonMap = WebUtility.INSTANCE.requestSender().sendHttpGetRequest(token, productMapUrl, null);
        } catch (IOException e) {
            WebAPIManager.INSTANCE.getLoggerKit().doLog(this.getClass().getSuperclass(), e, Level.SEVERE);
            throw new HttpWebException(HttpWebException.ERROR.SERVER_ERROR);
        }
        ProductMap.Item[] map = WebUtility.INSTANCE.parseFromJson(jsonMap, ProductMap.Item[].class);
        session.setAttribute(WebUtility.INSTANCE.attribMap, map);
    }
}
