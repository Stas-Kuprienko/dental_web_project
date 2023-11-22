package edu.dental.web.servlets.basic;

import edu.dental.database.DBService;
import edu.dental.database.DatabaseException;
import edu.dental.domain.APIManager;
import edu.dental.domain.entities.User;
import edu.dental.domain.records.WorkRecordBook;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public class NewUserSaver extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        User user;
        WorkRecordBook recordBook;

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        try {
            user = new User(name, email, password);
            DBService dbService = APIManager.instance().getDBService();
            if (!dbService.getUserDAO().put(user)) {
                request.getRequestDispatcher("/dental/").forward(request, response);
            }
            recordBook = APIManager.instance().getWorkRecordBook();
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            session.setAttribute("recordBook", recordBook);
            request.getRequestDispatcher("/welcome").forward(request, response);
        } catch (ServletException | IOException e) {
            throw new RuntimeException(e);
        } catch (DatabaseException e) {
            try {
                request.getRequestDispatcher("/enter").forward(request, response);
            } catch (ServletException | IOException ex) {
                //TODO logger
                throw new RuntimeException(ex);
            }
        }
    }
}
