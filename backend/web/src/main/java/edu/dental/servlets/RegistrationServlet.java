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

@WebServlet("/sign-up")
public class RegistrationServlet extends HttpServlet {

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
            UserDto user = AuthenticationService.registration(name, email, password);
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
