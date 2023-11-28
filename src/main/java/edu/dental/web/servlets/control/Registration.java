package edu.dental.web.servlets.control;

import edu.dental.database.DatabaseException;
import edu.dental.domain.APIManager;
import edu.dental.domain.entities.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/new-user")
public class Registration extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/sign-up").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if (name != null && email != null && password != null) {
            try {
                User user = APIManager.INSTANCE.getRepository().signUp(name, email, password);
                request.getSession().setAttribute("user", user.getEmail());
                request.getRequestDispatcher("/main").forward(request, response);
            } catch (DatabaseException e) {
                //TODO
                request.getRequestDispatcher("/error").forward(request, response);
            }
        }
    }
}