package edu.dental.servlets;

import edu.dental.WebAPI;
import edu.dental.domain.records.WorkRecordBookException;
import edu.dental.service.Repository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/main/sort")
public class RecordSorting extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = (int) request.getAttribute(WebAPI.INSTANCE.paramUser);
        int month = Integer.parseInt(request.getParameter("month"));
        try {
            Repository.getInstance().getRecordBook(userId).getSorter().doIt(month);
        } catch (WorkRecordBookException e) {
            response.sendError(500);
        }
        request.getRequestDispatcher("/main/dental-works").forward(request, response);
    }
}
