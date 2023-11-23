package edu.dental.web.servlets.basic;

import edu.dental.web.Repository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/enter")
public class Enter extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String user = (String) session.getAttribute("user");
        if (user != null) {
            try {
                Repository.get(user);
                request.getRequestDispatcher("/app/main").forward(request, response);
            } catch (NullPointerException e) {
                response.getWriter().write(page);
            }
        } else {
            response.getWriter().write(page);
        }
    }

    private static final String page = """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <title>DENTAL MECHANIC SERVICE</title>
                <link rel="stylesheet" type="text/css" href="css/style.css">
            </head>
            <nav class="menu">
                <header><strong>DENTAL MECHANIC SERVICE</strong></header>
            </nav>
            <body>
            <section>
            <h2>Input your email and password please:</h2>
            <form action="/dental/log-in" method="post">
                <label for="email">email:</label><br>
                <input type="text" id="email" name="email" value=""><br>
                <label for="password">password:</label><br>
                <input type="password" id="password" name="password" value=""><br><br>
                <button type="submit" style="font-size: 18px; width: 90;">LOG IN</button>
            </form>
            <br><br>
            <form action="/dental/sign-up">
                <button type="submit" style="font-size: 14px; width: 80;">SIGN UP</button>
            </form>
            </section>
            </body>
            </html>
            """;
}
