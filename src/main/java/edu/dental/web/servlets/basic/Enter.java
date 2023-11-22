package edu.dental.web.servlets.basic;

import edu.dental.database.DatabaseException;
import edu.dental.domain.APIManager;
import edu.dental.domain.authentication.Authenticator;
import edu.dental.domain.entities.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public class Enter extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user != null) {
            try {
                User user1 = APIManager.instance().getDBService().getUserDAO().get(user.getId());
                if (user.getEmail().equals(user1.getEmail())) {
                    if (Authenticator.verification(user, user1.getPassword())) {
                        request.getRequestDispatcher("/welcome").forward(request, response);
                    }
                }
            } catch (DatabaseException ignored) {
                response.getWriter().write(page);
            }
        }
        response.getWriter().write(page);
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
                <a href="/dental/new-work">NEW WORK</a>
                <a href="dental/work-list">WORK LIST</a>
                <a href="/dental/new-product">PRODUCT MAP</a>
                <a href="/dental/product-map">REPORTS</a>
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
