package edu.dental.server.application;

import edu.dental.domain.authentication.AuthenticationException;
import edu.dental.domain.authentication.Authenticator;
import edu.dental.domain.entities.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

public class ServerApp extends HttpServlet {

    public User user;

    //http://localhost:8081/dental/?login=stas@email&password=1234

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter writer = response.getWriter();

        String login = request.getParameter("login");
        String password = request.getParameter("password");
        System.out.println(login);
        System.out.println(password);

        try {
            System.out.println(this.user = Authenticator.authenticate(login, password));
        } catch (AuthenticationException e) {
            e.printStackTrace(writer);
        }

        writer.write("<h1>Welcome!</h1>");
        writer.write("<h2>" + user.getName() +"</h2>");
    }

}
