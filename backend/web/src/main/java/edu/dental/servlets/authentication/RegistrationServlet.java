package edu.dental.servlets.authentication;

import edu.dental.database.DatabaseException;
import edu.dental.dto.UserDto;
import edu.dental.security.AuthenticationService;
import edu.dental.service.JsonObjectParser;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/sign-up")
public class RegistrationServlet extends HttpServlet {

    private AuthenticationService authenticationService;
    private JsonObjectParser jsonParser;

    @Override
    public void init() throws ServletException {
        this.authenticationService = AuthenticationService.getInstance();
        this.jsonParser = JsonObjectParser.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendError(405);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        try {
            UserDto user = authenticationService.registration(name, email, password);
            String jsonUser = jsonParser.parseToJson(user);
            response.setContentType("text/plain");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().print(jsonUser);
            response.getWriter().flush();
        } catch (DatabaseException e) {
            response.sendError(500);
        }
    }
}
