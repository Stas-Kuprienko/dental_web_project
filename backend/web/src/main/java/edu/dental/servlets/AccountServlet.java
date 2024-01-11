package edu.dental.servlets;

import edu.dental.WebAPI;
import edu.dental.domain.account.AccountException;
import edu.dental.domain.account.AccountService;
import edu.dental.dto.UserDto;
import edu.dental.entities.User;
import edu.dental.service.Repository;
import edu.dental.WebException;
import edu.dental.security.AuthenticationService;
import edu.dental.service.tools.JsonObjectParser;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/main/account")
public class AccountServlet extends HttpServlet {

    public final String nameField = "name";
    public final String emailField = "email";
    public final String passwordField = "password";
    public final String fieldParam = "field";
    public final String valueParam = "value";


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendError(405);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = (int) request.getAttribute(WebAPI.INSTANCE.paramUser);
        String field = request.getParameter(fieldParam);
        String value = request.getParameter(valueParam);
        if (field == null || value == null || field.isEmpty() || value.isEmpty()) {
            response.sendError(400);
        } else {
            try {
                String json = update(userId, field, value);
                if (json == null) {
                    response.sendError(400);
                } else {
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().print(json);
                    response.getWriter().flush();
                }
            } catch (WebException e) {
                response.sendError(e.code.i);
            }
        }
        }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = (int) request.getAttribute(WebAPI.INSTANCE.paramUser);
        User user = Repository.getInstance().getUser(userId);
        try {
            AccountService.getInstance().deleteUser(user);
            Repository.getInstance().delete(userId);
            //TODO
            response.setStatus(200);
        } catch (AccountException e) {
            response.sendError(500);
        }
    }


    private String update(int userId, String field, String value) throws WebException {
        User user = Repository.getInstance().getUser(userId);
        UserDto dto;
        switch (field) {
            case nameField -> dto = setName(user, value);
            case emailField -> dto = setEmail(user, value);
            case passwordField -> dto = setPassword(user, value);
            default -> {
                return null;
            }
        }
        return JsonObjectParser.getInstance().parseToJson(dto);
    }

    private UserDto setName(User user, String name) throws WebException {
        String oldValue = user.getName();
        user.setName(name);
        try {
            AccountService.getInstance().update(user);
            return UserDto.parse(user);
        } catch (AccountException e) {
            user.setName(oldValue);
            //TODO
            throw new WebException(e.getMessage(), WebException.CODE.SERVER_ERROR);
        }
    }


    private UserDto setEmail(User user, String email) throws WebException {
        String oldValue = user.getEmail();
        user.setEmail(email);
        try {
            AccountService.getInstance().update(user);
            return UserDto.parse(user);
        } catch (AccountException e) {
            user.setEmail(oldValue);
            //TODO
            throw new WebException(e.getMessage(), WebException.CODE.SERVER_ERROR);
        }
    }

    private UserDto setPassword(User user, String password) throws WebException {
        if (!AuthenticationService.getInstance().updatePassword(user, password)) {
            //TODO
           throw new WebException(WebException.CODE.BAD_REQUEST);
        } else {
            return UserDto.parse(user);
        }
    }
}