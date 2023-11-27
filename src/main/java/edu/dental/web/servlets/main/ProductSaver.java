package edu.dental.web.servlets.main;

import edu.dental.database.DatabaseService;
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

public class ProductSaver extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        User user = (User) session.getAttribute("user");
        WorkRecordBook recordBook = (WorkRecordBook) session.getAttribute("recordBook");

        String title = request.getParameter("title");
        int price = Integer.parseInt(request.getParameter("price"));

        DatabaseService databaseService = APIManager.instance().getDatabaseService();
        try {
            int id = databaseService.getProductMapDAO(user).put(title, price);
            recordBook.getMap().put(title, price, id);
            request.setAttribute("user", user);
            request.getRequestDispatcher("/welcome").forward(request, response);
        } catch (DatabaseException e) {
            System.out.println("Error!");
            request.getRequestDispatcher("/welcome").forward(request, response);
        }
    }
}
