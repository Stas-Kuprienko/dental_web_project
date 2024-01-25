package edu.dental.servlets.account;

import edu.dental.APIResponseException;
import edu.dental.service.WebUtility;
import edu.dental.beans.UserDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/main/account")
public class AccountServlet extends HttpServlet {

    public final String accountUrl = "main/account";
    public final String fieldParam = "field";
    public final String valueParam = "value";
    public final String nameField = "name";
    public final String emailField = "email";
    public final String passwordField = "password";

    private WebUtility.HttpRequestSender httpRequestSender;


    @Override
    public void init() throws ServletException {
        this.httpRequestSender = WebUtility.INSTANCE.requestSender();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            UserDto user = getUser((String) request.getSession().getAttribute(WebUtility.INSTANCE.sessionToken));
            request.setAttribute(WebUtility.INSTANCE.sessionUser, user);
            request.getRequestDispatcher("/main/account/page").forward(request, response);
        } catch (APIResponseException e) {
            response.sendError(e.CODE, e.MESSAGE);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String method = request.getParameter("method");
        if (method != null && method.equals("delete")) {
            doDelete(request, response);
        } else {
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String token = (String) request.getSession().getAttribute(WebUtility.INSTANCE.sessionToken);
            String field = request.getParameter(fieldParam);
            String value = request.getParameter(valueParam);
            UserDto user = setUserValue(token, field, value);
            request.setAttribute(WebUtility.INSTANCE.sessionUser, user);
            request.getRequestDispatcher("/main/account/page").forward(request, response);
        } catch (APIResponseException e) {
            response.sendError(e.CODE, e.MESSAGE);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String jwt = (String) request.getSession().getAttribute(WebUtility.INSTANCE.sessionToken);
        try {
            WebUtility.INSTANCE.requestSender().sendHttpDeleteRequest(jwt, accountUrl, null);
            request.getRequestDispatcher("/main/log-out").forward(request, response);
        } catch (Exception e) {
            response.sendError(400);
        }
    }


    private UserDto setUserValue(String token, String field, String value) throws IOException, APIResponseException {
        WebUtility.QueryFormer former = new WebUtility.QueryFormer();
        former.add(fieldParam, field);
        former.add(valueParam, value);
        String requestParam = former.form();

        String json = WebUtility.INSTANCE.requestSender().sendHttpPostRequest(token, accountUrl, requestParam);
        return WebUtility.INSTANCE.parseFromJson(json, UserDto.class);
    }

    private UserDto getUser(String token) throws IOException, APIResponseException {
        String json = httpRequestSender.sendHttpGetRequest(token, accountUrl);
        return WebUtility.INSTANCE.parseFromJson(json, UserDto.class);
    }
}