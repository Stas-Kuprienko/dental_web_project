package edu.dental.web.servlets.main;

import edu.dental.domain.Action;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;

@WebServlet("/main/save-work")
public class DentalWorkStoring extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String patient = request.getParameter("patient");
        String clinic = request.getParameter("clinic");
        String product = request.getParameter("product");
        int quantity = Integer.parseInt(request.getParameter("quantity"));
        LocalDate complete = LocalDate.parse(request.getParameter("complete"));

        String login = (String) request.getSession().getAttribute("user");
        try {
            Action.newWorkRecord(login, patient, clinic, product, quantity, complete);
        } catch (Action.ActionException e) {
            //TODO logger
            response.sendError(e.CODE);
            return;
        }
        request.getRequestDispatcher("/main").forward(request, response);
    }
}
