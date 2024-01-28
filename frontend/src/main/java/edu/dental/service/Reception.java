package edu.dental.service;

import edu.dental.APIResponseException;
import edu.dental.beans.UserBean;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public final class Reception {

    static {
        instance = new Reception();
    }
    private Reception() {
        this.dentalWorksService = DentalWorksService.getInstance();
        this.productMapService = ProductMapService.getInstance();
    }
    private static final Reception instance;

    private static final String logInUrl = "log-in";
    private static final String paramEmail = "email";
    private static final String paramPassword = "password";

    private final DentalWorksService dentalWorksService;
    private final ProductMapService productMapService;


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

        UserBean user =  getUserDto(requestParameters);

        HttpSession session = request.getSession(true);

        session.setAttribute(WebUtility.INSTANCE.sessionUser, user.getId());
        session.setAttribute(WebUtility.INSTANCE.sessionToken, user.getJwt());

        dentalWorksService.setWorkList(session);
        productMapService.setProductMap(session);
    }

    private UserBean getUserDto(String requestParameters) throws IOException, APIResponseException {
        String jsonUser = WebUtility.INSTANCE.requestSender().sendHttpPostRequest(logInUrl, requestParameters);
        return WebUtility.INSTANCE.parseFromJson(jsonUser, UserBean.class);
    }

    public static synchronized Reception getInstance() {
        return instance;
    }
}
