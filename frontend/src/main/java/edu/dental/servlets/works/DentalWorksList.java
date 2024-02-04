package edu.dental.servlets.works;

import edu.dental.APIResponseException;
import edu.dental.control.Administrator;
import edu.dental.control.DentalWorksListService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/main/dental-works")
public class DentalWorksList extends HttpServlet {

    private static final String dentalWorksPageURL = "/main/dental-works/page";

    private DentalWorksListService dentalWorksListService;

    @Override
    public void init() throws ServletException {
        this.dentalWorksListService = Administrator.getInstance().getDentalWorksListService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String year_month = request.getParameter("year-month");
        if (year_month != null && !year_month.isEmpty()) {
            try {
                dentalWorksListService.getRequired(year_month, request);
            } catch (APIResponseException e) {
                response.sendError(e.CODE, e.MESSAGE);
            }
        }
        request.getRequestDispatcher(dentalWorksPageURL).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
