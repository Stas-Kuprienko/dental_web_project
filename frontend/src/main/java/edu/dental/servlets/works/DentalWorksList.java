package edu.dental.servlets.works;

import edu.dental.APIResponseException;
import edu.dental.beans.DentalWork;
import edu.dental.control.Administrator;
import edu.dental.control.DentalWorksListService;
import edu.dental.service.WebUtility;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/main/dental-works")
public class DentalWorksList extends HttpServlet {

    private static final String yearMonthParam = "year-month";
    private static final String yearParam = "year";
    private static final String monthParam = "month";
    private static final String dentalWorksPageURL = "/main/dental-works/page";

    private DentalWorksListService dentalWorksListService;

    @Override
    public void init() throws ServletException {
        this.dentalWorksListService = Administrator.getInstance().getDentalWorksListService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String year_month = request.getParameter(yearMonthParam);
        if (year_month != null && !year_month.isEmpty()) {
            try {
                dentalWorksListService.getRequired(year_month, request);
            } catch (APIResponseException e) {
                e.errorRedirect(request, response);
            }
        }
        request.getRequestDispatcher(dentalWorksPageURL).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String method;
        if ((method = request.getParameter("method")) != null && method.equals("put")) {
            doPut(request, response);
        } else {

            HttpSession session = request.getSession();
            int year = request.getParameter(yearParam) != null ? Integer.parseInt(request.getParameter(yearParam)) :
                    (int) request.getAttribute(yearParam);
            int month = request.getParameter(monthParam) != null ? Integer.parseInt(request.getParameter(monthParam)) :
                    (int) request.getAttribute(monthParam);
            try {
                dentalWorksListService.sort(session, year, month);
                request.getRequestDispatcher(dentalWorksPageURL).forward(request, response);
            } catch (APIResponseException e) {
                e.errorRedirect(request, response);
            }
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        int year = request.getParameter(yearParam) != null ? Integer.parseInt(request.getParameter(yearParam)) :
                (int) request.getAttribute(yearParam);
        int month = request.getParameter(monthParam) != null ? Integer.parseInt(request.getParameter(monthParam)) :
                (int) request.getAttribute(monthParam);
        try {
            DentalWork[] works = dentalWorksListService.setStatus(session, year, month);
            request.setAttribute(WebUtility.INSTANCE.attribWorks, works);
            request.setAttribute(yearMonthParam, year + "-" + month);
            request.getRequestDispatcher(dentalWorksPageURL).forward(request, response);
        } catch (APIResponseException e) {
            e.errorRedirect(request, response);
        }
    }
}