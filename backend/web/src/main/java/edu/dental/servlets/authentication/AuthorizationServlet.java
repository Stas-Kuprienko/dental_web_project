package edu.dental.servlets.authentication;

import edu.dental.database.DatabaseException;
import edu.dental.dto.UserDto;
import edu.dental.security.AuthenticationService;
import edu.dental.security.WebSecurityException;
import edu.dental.service.JsonObjectParser;
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
        } catch (DatabaseException e) {
            response.sendError(500);
        } catch (WebSecurityException e) {
            response.sendError(e.code, e.getMessage());
        }
    }
}
