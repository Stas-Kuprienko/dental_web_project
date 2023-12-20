package edu.dental.web.control;

import edu.dental.beans.User;
import edu.dental.service.Repository;
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

//        if (name != null && email != null && password != null) {
//            try {
//                User user = Repository.getInstance().signUp(name, email, password);
//                request.getSession().setAttribute("user", user.getEmail());
//                request.getRequestDispatcher("/main").forward(request, response);
//            } catch (AuthenticationException e) {
//                //TODO
//                request.getRequestDispatcher("/error").forward(request, response);
//            }
//        }
    }
}