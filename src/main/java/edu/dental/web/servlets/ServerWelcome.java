package edu.dental.web.servlets;

import edu.dental.domain.authentication.AuthenticationException;
import edu.dental.domain.authentication.Authenticator;
import edu.dental.domain.entities.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

public class ServerWelcome extends HttpServlet {

    User user;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter writer = response.getWriter();

        String login = request.getParameter("email");
        String password = request.getParameter("password");

        try {
            user = Authenticator.authenticate(login, password);
            request.getSession().setAttribute("user", user);

        } catch (AuthenticationException e) {
            writer.write("no such user.");
            request.getRequestDispatcher("dental/").forward(request, response);
        }
        writer.write(String.format(actions, user.getName()));
    }

    private static final String actions = """
            <!DOCTYPE html>
            <html>
            <style>
                body {
                    background-color: dimGrey;
                }
                body {color:white;}
            </style>
            <body>
            <h1>Welcome, %s!</h1>
            <h2>Dental mechanic service</h2>
            <form action="/dental/new-product-type" method="post">
                <input type="submit" value="add new product type">
            </form>
            <form action="/dental/new-work">
                <input type="submit" value="add new work">
            </form>
            <form action="/dental/open-work-list">
                <input type="submit" value="open work list">
            </form>
            <form action="/dental/save-report-file">
                <input type="submit" value="save report to file">
            </form>
            </body>
            </html>""";
}
