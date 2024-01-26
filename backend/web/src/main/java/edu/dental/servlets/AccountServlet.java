package edu.dental.servlets;

import edu.dental.domain.user.AccountException;
import edu.dental.domain.user.UserService;
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

    private UserService userService;
    private Repository repository;
    private JsonObjectParser jsonObjectParser;
    private AuthenticationService authenticationService;

    @Override
    public void init() throws ServletException {
        this.userService = UserService.getInstance();
        this.repository = Repository.getInstance();
        this.jsonObjectParser = JsonObjectParser.getInstance();
        this.authenticationService = AuthenticationService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = (int) request.getAttribute(Repository.paramUser);
        UserDto dto = new UserDto(repository.getUser(userId));
        String json = jsonObjectParser.parseToJson(dto);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().print(json);
        response.getWriter().flush();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = (int) request.getAttribute(Repository.paramUser);
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
                response.sendError(e.cause.code, e.message);
            }
        }
        }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = (int) request.getAttribute(Repository.paramUser);
        User user = repository.getUser(userId);
        try {
            userService.deleteUser(user);
            repository.delete(userId);
            response.setStatus(200);
        } catch (AccountException e) {
            response.sendError(e.cause.code, e.message);
        }
    }


    private String update(int userId, String field, String value) throws WebException {
        User user = repository.getUser(userId);
        UserDto dto;
        switch (field) {
            case nameField -> dto = setName(user, value);
            case emailField -> dto = setEmail(user, value);
            case passwordField -> dto = setPassword(user, value);
            default -> {
                return null;
            }
        }
        return jsonObjectParser.parseToJson(dto);
    }

    private UserDto setName(User user, String name) throws WebException {
        String oldValue = user.getName();
        user.setName(name);
        try {
            userService.update(user);
            return new UserDto(user);
        } catch (AccountException e) {
            user.setName(oldValue);
            throw new WebException(e.cause, e);
        }
    }


    private UserDto setEmail(User user, String email) throws WebException {
        String oldValue = user.getEmail();
        user.setEmail(email);
        try {
            userService.update(user);
            return new UserDto(user);
        } catch (AccountException e) {
            user.setEmail(oldValue);
            throw new WebException(e.cause, e);
        }
    }

    private UserDto setPassword(User user, String password) throws WebException {
        if (authenticationService.updatePassword(user, password)) {
            return new UserDto(user);
        } else {
            throw new WebException(AccountException.CAUSE.BAD_REQUEST, AccountException.MESSAGE.DATABASE_ERROR);
        }
    }
}