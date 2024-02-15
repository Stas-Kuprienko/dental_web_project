package edu.dental.servlets.account;

import edu.dental.HttpWebException;
import edu.dental.control.Administrator;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/new-user")
public class Registration extends HttpServlet {

    private static final String paramName = "name";
    private static final String paramEmail = "email";
    private static final String paramPassword = "password";
    private static final String signUpPageURL = "/sign-up";

    private Administrator administrator;

    @Override
    public void init() throws ServletException {
        this.administrator = Administrator.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher(signUpPageURL).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        String name = request.getParameter(paramName);
        String email = request.getParameter(paramEmail);
        String password = request.getParameter(paramPassword);
        if (isCorrect(name, email, password)) {
            try {
                administrator.signUp(session, name, email, password);
                request.getRequestDispatcher("/main").forward(request, response);
            } catch (HttpWebException e) {
                e.errorRedirect(request, response);
            }
        } else {
            request.getRequestDispatcher(signUpPageURL).forward(request, response);
        }
    }


    private boolean isCorrect(String name, String email, String password) {
        return name != null && !name.isEmpty()
                && email != null && !email.isEmpty()
                && password != null && !password.isEmpty();
    }
}