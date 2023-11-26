package edu.dental.web.servlets.control;

import edu.dental.domain.APIManager;
import edu.dental.domain.authentication.AuthenticationException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/log-in")
public class Authorization extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        if (email != null && password != null) {
            try {
                APIManager.instance().getRepository().logIn(email, password);
                request.getSession().setAttribute("user", email);
                request.getRequestDispatcher("/main").forward(request, response);
            } catch (AuthenticationException e) {
                //TODO
                if (e.causes != null) {
                    request.getRequestDispatcher("/?error=" + e.getMessage());
                } else {
                    request.getRequestDispatcher("/error");
                }
            }
        }
    }
}
