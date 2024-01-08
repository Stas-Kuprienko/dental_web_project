package edu.dental.servlets.control;

import edu.dental.WebAPI;
import edu.dental.beans.UserDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/log-in")
public class Authorization extends HttpServlet {

    public final String paramEmail = "email";
    public final String paramPassword = "password";


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter(paramEmail);
        String password = request.getParameter(paramPassword);
        if (email == null || password == null) {
            request.getRequestDispatcher("/").forward(request, response);
        } else {
            UserDto user = Reception.getInstance().getByLogin(email, password);

            request.getSession().setAttribute(WebAPI.INSTANCE.sessionUser, user.id());
            request.getSession().setAttribute(WebAPI.INSTANCE.sessionToken, user.jwt());

            request.getRequestDispatcher("/main").forward(request, response);
        }
    }
}
