package edu.dental.web.servlets.basic;

import edu.dental.database.DBService;
import edu.dental.database.DatabaseException;
import edu.dental.domain.APIManager;
import edu.dental.domain.entities.User;
import edu.dental.domain.records.WorkRecordBook;
import edu.dental.web.Repository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/new-user")
public class NewUserSaver extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user;
        WorkRecordBook recordBook;

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        HttpSession session = request.getSession();
        if (session.getAttribute("user") == null) {
            request.getRequestDispatcher("/sign-up").forward(request, response);
        } else {
            try {
                user = new User(name, email, password);
                DBService dbService = APIManager.instance().getDBService();
                if (!dbService.getUserDAO().put(user)) {
                    request.getRequestDispatcher("/enter").forward(request, response);
                }
                recordBook = APIManager.instance().getWorkRecordBook();
                session.setAttribute("user", user.getEmail());
                Repository.put(user, recordBook);
                request.getRequestDispatcher("/app/main").forward(request, response);
            } catch (DatabaseException e) {
                request.getRequestDispatcher("/enter").forward(request, response);
            }
        }
    }
}