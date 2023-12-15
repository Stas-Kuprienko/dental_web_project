package edu.dental.web.servlets.main;

import edu.dental.database.DatabaseException;
import edu.dental.database.DatabaseService;
import edu.dental.database.dao.DentalWorkDAO;
import edu.dental.domain.entities.DentalWork;
import edu.dental.domain.entities.User;
import edu.dental.domain.records.WorkRecordBook;
import edu.dental.web.Repository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/main/work-list/sorting")
public class WorkSorting extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String user = (String) request.getSession().getAttribute("user");
        try {
            workSorting(user);
            request.getRequestDispatcher("/main/work-list").forward(request, response);
        } catch (DatabaseException e) {
            response.sendError(500);
        }
    }

    private void workSorting(String login) throws DatabaseException {
        User user = Repository.getInstance().getUser(login);
        WorkRecordBook recordBook = Repository.getInstance().getRecordBook(login);
        DentalWorkDAO workDAO = DatabaseService.getInstance().getDentalWorkDAO(user);
        List<DentalWork> closedWorks = recordBook.sorting();
        workDAO.setFieldValue(closedWorks, "status", "CLOSED");
        workDAO.setReportId(closedWorks);
    }
}
