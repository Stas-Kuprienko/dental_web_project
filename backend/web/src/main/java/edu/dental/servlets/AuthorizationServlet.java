package edu.dental.servlets;

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

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendError(405);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserDto user;
        String token = request.getParameter("token");
        try {
            if (token == null || token.isEmpty()) {
                String email = request.getParameter("email");
                String password = request.getParameter("password");
                user = AuthenticationService.authorization(email, password);
            } else {
                user = AuthenticationService.getUserDto(token);
            }
            String jsonUser = JsonObjectParser.getInstance().parseToJson(user);
            response.setContentType("text/plain");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().print(jsonUser);
            response.getWriter().flush();
        } catch (WebException e) {
            response.sendError(e.code.i);
        }
    }
}
