package edu.dental.servlets.account;

import edu.dental.WebUtility;
import edu.dental.beans.UserDto;
import edu.dental.service.WebRepository;
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


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/main/account/page").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String method = request.getParameter("method");
        if (method != null && method.equals("delete")) {
            doDelete(request, response);
        } else {
            int userId = (int) request.getSession().getAttribute(WebUtility.INSTANCE.sessionUser);
            String field = request.getParameter(fieldParam);
            String value = request.getParameter(valueParam);
            switch (field) {
                case nameField,
                     emailField,
                     passwordField -> setUserValue(userId, field, value);
                default -> {}
            }
            doGet(request, response);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = (int) request.getSession().getAttribute(WebUtility.INSTANCE.sessionUser);
        String jwt = WebRepository.INSTANCE.getToken(userId);
        try {
            WebUtility.INSTANCE.requestSender().sendHttpDeleteRequest(jwt, accountUrl, "");
            request.getRequestDispatcher("/main/log-out").forward(request, response);
        } catch (Exception e) {
            response.sendError(400);
        }
    }


    private void setUserValue(int userId, String field, String value) throws IOException {
        String jwt = WebRepository.INSTANCE.getToken(userId);

        WebUtility.QueryFormer former = new WebUtility.QueryFormer();
        former.add(fieldParam, field);
        former.add(valueParam, value);
        String requestParam = former.form();

        String json = WebUtility.INSTANCE.requestSender().sendHttpPostRequest(jwt, accountUrl, requestParam);
        UserDto user = WebUtility.INSTANCE.parseFromJson(json, UserDto.class);
        WebRepository.INSTANCE.updateUser(user);
    }
}