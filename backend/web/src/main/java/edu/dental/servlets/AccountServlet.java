package edu.dental.servlets;

import edu.dental.WebAPI;
import edu.dental.domain.authentication.AuthenticationException;
import edu.dental.domain.authentication.Authenticator;
import edu.dental.dto.UserDto;
import edu.dental.entities.User;
import edu.dental.service.Repository;
import edu.dental.service.security.AuthenticationService;
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
            } catch (AuthenticationException e) {
                response.sendError(e.cause.code);
            }
        }
        }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = (int) request.getAttribute(WebAPI.INSTANCE.paramUser);
        User user = Repository.getInstance().getUser(userId);
        try {
            Authenticator.deleteUser(user);
            Repository.getInstance().delete(userId);
            //TODO
            response.setStatus(200);
        } catch (AuthenticationException e) {
            response.sendError(e.cause.code);
        }
    }


    private String update(int userId, String field, String value) throws AuthenticationException {
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

    private UserDto setName(User user, String name) throws AuthenticationException {
        String oldValue = user.getName();
        user.setName(name);
        try {
            Authenticator.updateUser(user);
            return UserDto.parse(user);
        } catch (AuthenticationException e) {
            user.setName(oldValue);
            throw e;
        }
    }


    private UserDto setEmail(User user, String email) throws AuthenticationException {
        String oldValue = user.getEmail();
        user.setEmail(email);
        try {
            Authenticator.updateUser(user);
            return UserDto.parse(user);
        } catch (AuthenticationException e) {
            user.setEmail(oldValue);
            throw e;
        }
    }

    private UserDto setPassword(User user, String password) throws AuthenticationException {
        if (!AuthenticationService.updatePassword(user, password)) {
           throw new AuthenticationException(AuthenticationException.Causes.ERROR);
        } else {
            return UserDto.parse(user);
        }
    }
}