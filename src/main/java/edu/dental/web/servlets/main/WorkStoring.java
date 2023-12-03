package edu.dental.web.servlets.main;

import edu.dental.database.DatabaseException;
import edu.dental.database.DatabaseService;
import edu.dental.domain.entities.DentalWork;
import edu.dental.domain.records.WorkRecordBookException;
import edu.dental.web.JsonObjectParser;
import edu.dental.web.Repository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDate;

@WebServlet("/main/save-work")
public class WorkStoring extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String patient = request.getParameter("patient");
        String clinic = request.getParameter("clinic");
        String product = request.getParameter("product");
        int quantity = Integer.parseInt(request.getParameter("quantity"));
        LocalDate complete = LocalDate.parse(request.getParameter("complete"));

        String login = (String) request.getSession().getAttribute("user");
        Repository.Account account = Repository.getInstance().get(login);
        DentalWork dw = null;
        if (product != null && !product.isEmpty()) {
            try {
                dw = account.recordBook().createRecord(patient, clinic, product, quantity, complete);
            } catch (WorkRecordBookException e) {
                request.getRequestDispatcher("/error?").forward(request, response);
            }
        } else {
            dw = account.recordBook().createRecord(patient, clinic);
        }
        try {
            DatabaseService.getInstance().getDentalWorkDAO(account.user()).put(dw);
        } catch (DatabaseException e) {
            request.getRequestDispatcher("/error?").forward(request, response);
        }
        HttpSession session = request.getSession();
        session.setAttribute("works", JsonObjectParser.getInstance().addNewWork((String) session.getAttribute("works"), dw));
        request.getRequestDispatcher("/main").forward(request, response);
    }
}
