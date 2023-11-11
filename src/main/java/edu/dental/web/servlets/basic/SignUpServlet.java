package edu.dental.web.servlets.basic;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class SignUpServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {

    }

    private static final String htmlPage = """
            <!DOCTYPE html>
            <html>
            <style>
                body {
                    background-color: dimGrey;
                }
                body {color:white;}
            </style>
            <body>
            <h2>Input your email and password please:</h2>
            <form action="/dental/new-user" method="post">
                <label for="name">name:</label><br>
                <input type="text" id="name" name="name" value=""><br>
                <label for="email">email:</label><br>
                <input type="text" id="email" name="email" value=""><br>
                <label for="password">password:</label><br>
                <input type="password" id="password" name="password" value=""><br><br>
                <input type="submit" value="enter">
            </form>
            </body>
            </html>
            """;
}
