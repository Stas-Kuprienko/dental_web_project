package edu.dental.servlets;

import edu.dental.domain.authentication.AuthenticationException;
import edu.dental.dto.UserDto;
import edu.dental.service.AuthenticationService;
import edu.dental.service.JsonObjectParser;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/log-in")
public class AuthorizationServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        try {
            UserDto user = AuthenticationService.authorization(email, password);
            String jsonUser = JsonObjectParser.getInstance().parseToJson(user);
            response.setContentType("text/plain");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().print(jsonUser);
            response.getWriter().flush();
        } catch (AuthenticationException e) {
            //TODO
            response.sendError(400, String.valueOf(e.causes));
        }
    }
}
