package edu.dental.servlets.authentication;

import edu.dental.dto.UserDto;
import edu.dental.WebException;
import edu.dental.security.AuthenticationService;
import edu.dental.service.tools.JsonObjectParser;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/log-in")
public class AuthorizationServlet extends HttpServlet {

    private AuthenticationService authenticationService;
    private JsonObjectParser jsonObjectParser;

    @Override
    public void init() throws ServletException {
        this.authenticationService = AuthenticationService.getInstance();
        this.jsonObjectParser = JsonObjectParser.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendError(405);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserDto user;
        try {
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            user = authenticationService.authorization(email, password);
            String jsonUser = jsonObjectParser.parseToJson(user);
            response.setContentType("text/plain");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().print(jsonUser);
            response.getWriter().flush();
        } catch (WebException e) {
            response.sendError(e.cause.code);
        }
    }
}