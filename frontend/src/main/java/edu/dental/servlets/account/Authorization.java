package edu.dental.servlets.account;

import edu.dental.service.WebUtility;
import stas.exceptions.HttpWebException;
import edu.dental.control.Administrator;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/log-in")
public class Authorization extends HttpServlet {

    private static final String emailParam = "email";
    private static final String passwordParam = "password";

    private Administrator administrator;

    @Override
    public void init() throws ServletException {
        this.administrator = Administrator.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        String email = request.getParameter(emailParam);
        String password = request.getParameter(passwordParam);
        if (isCorrect(email, password)) {
            try {
                administrator.signIn(session, email, password);
                request.getRequestDispatcher("/main").forward(request, response);
            } catch (HttpWebException e) {
                e.errorRedirect(WebUtility.INSTANCE.errorPageURL, request, response);
            }
        } else {
            request.getRequestDispatcher("/").forward(request, response);
        }
    }


    private boolean isCorrect(String email, String password) {
        return email != null && !email.isEmpty()
                && password != null && !password.isEmpty();
    }
}