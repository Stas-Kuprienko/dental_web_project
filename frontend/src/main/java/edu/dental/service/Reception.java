package edu.dental.service;

import edu.dental.APIResponseException;
import edu.dental.WebUtility;
import edu.dental.beans.UserDto;
import edu.dental.service.my_account_manager.MyAccountManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public final class Reception {

    static {
        instance = new Reception();
    }
    private Reception() {
        this.accountManager = new MyAccountManager();
    }
    private static final Reception instance;

    private static final String logInUrl = "log-in";
    private static final String paramEmail = "email";
    private static final String paramPassword = "password";

    private final AccountManager accountManager;


    public void authenticateByLogin(HttpServletRequest request) throws IOException, APIResponseException {
        String email = request.getParameter(paramEmail);
        String password = request.getParameter(paramPassword);

        if (email == null || password == null) {
            throw new APIResponseException(400, "argument is null");
        }
        WebUtility.QueryFormer queryFormer = new WebUtility.QueryFormer();

        queryFormer.add(paramEmail, email);
        queryFormer.add(paramPassword, password);
        String requestParameters = queryFormer.form();

        UserDto user =  getUserDto(requestParameters);

        HttpSession session = request.getSession(true);

        session.setAttribute(WebUtility.INSTANCE.sessionUser, user.id());
        session.setAttribute(WebUtility.INSTANCE.sessionToken, user.jwt());

        accountManager.setWorkList(session);
        accountManager.setProductMap(session);
    }

    public UserDto getByToken(String token, HttpSession session) throws IOException, APIResponseException {
        WebUtility.QueryFormer queryFormer = new WebUtility.QueryFormer();

        String paramToken = "token";
        queryFormer.add(paramToken, token);
        String requestParameters = queryFormer.form();

        return getUserDto(requestParameters);
    }

    private UserDto getUserDto(String requestParameters) throws IOException, APIResponseException {
        String jsonUser = WebUtility.INSTANCE.requestSender().sendHttpPostRequest(logInUrl, requestParameters);
        return WebUtility.INSTANCE.parseFromJson(jsonUser, UserDto.class);
    }

    public static synchronized Reception getInstance() {
        return instance;
    }
}
