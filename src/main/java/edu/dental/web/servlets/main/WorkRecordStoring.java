package edu.dental.web.servlets.main;

import edu.dental.database.DatabaseException;
import edu.dental.database.DatabaseService;
import edu.dental.domain.entities.DentalWork;
import edu.dental.domain.records.WorkRecordBookException;
import edu.dental.web.Repository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;

@WebServlet("/main/save-work")
public class WorkRecordStoring extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String patient = request.getParameter("patient");
        String clinic = request.getParameter("clinic");
        String product = request.getParameter("product");
        int quantity = Integer.parseInt(request.getParameter("quantity"));
        LocalDate complete = LocalDate.parse(request.getParameter("complete"));

        String login = (String) request.getSession().getAttribute("user");
        Repository.Account account = Repository.getInstance().get(login);
        DentalWork dw;
        if (product != null && !product.isEmpty()) {
            try {
                dw = account.recordBook().createRecord(patient, clinic, product, quantity, complete);
            } catch (WorkRecordBookException e) {
                response.sendError(500);
                return;
            }
        } else {
            dw = account.recordBook().createRecord(patient, clinic);
        }
        try {
            DatabaseService.getInstance().getDentalWorkDAO(account.user()).put(dw);
        } catch (DatabaseException e) {
            response.sendError(500);
            return;
        }
        request.getRequestDispatcher("/main").forward(request, response);
    }
}
