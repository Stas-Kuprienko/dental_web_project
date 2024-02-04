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

@WebServlet("/main/dental-works/sort")
public class DentalWorkListSorting extends HttpServlet {

    private static final String yearParam = "year";
    private static final String monthParam = "month";
    private static final String dentalWorksListURL = "/main/dental-works";

    private DentalWorksListService dentalWorksListService;

    @Override
    public void init() throws ServletException {
        this.dentalWorksListService = Administrator.getInstance().getDentalWorksListService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        int year = request.getParameter(yearParam) != null ? Integer.parseInt(request.getParameter(yearParam)) :
                (int) request.getAttribute(yearParam);
        int month = request.getParameter(monthParam) != null ? Integer.parseInt(request.getParameter(monthParam)) :
                (int) request.getAttribute(monthParam);
        try {
            dentalWorksListService.sort(session, year, month);
            request.getRequestDispatcher(dentalWorksListURL).forward(request, response);
        } catch (APIResponseException e) {
            response.sendError(e.CODE, e.MESSAGE);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        int year = request.getParameter(yearParam) != null ? Integer.parseInt(request.getParameter(yearParam)) :
                (int) request.getAttribute(yearParam);
        int month = request.getParameter(monthParam) != null ? Integer.parseInt(request.getParameter(monthParam)) :
                (int) request.getAttribute(monthParam);
        try {
            DentalWork[] works = dentalWorksListService.setStatus(session, year, month);
            request.setAttribute(WebUtility.INSTANCE.attribWorks, works);
            request.getRequestDispatcher(dentalWorksListURL).forward(request, response);
        } catch (APIResponseException e) {
            response.sendError(e.CODE, e.MESSAGE);
        }
    }
}