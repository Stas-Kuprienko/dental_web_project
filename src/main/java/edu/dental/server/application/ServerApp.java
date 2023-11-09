package edu.dental.server.application;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

public class ServerApp extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter writer = response.getWriter();
        writer.write(enter);
    }


    private static final String enter = """
            <!DOCTYPE html>
            <html>
            <body>
            <h2>Input your email and password please:</h2>
            <form action="/dental/welcome">
              <label for="email">email:</label><br>
              <input type="text" id="email" name="email" value=""><br>
              <label for="password">password:</label><br>
              <input type="text" id="password" name="password" value=""><br><br>
              <input type="submit" value="enter">
            </form>
            </body>
            </html>
            """;
}
