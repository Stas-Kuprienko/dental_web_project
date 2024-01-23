package edu.dental.servlets.works;

import edu.dental.APIResponseException;
import edu.dental.WebUtility;
import edu.dental.beans.DentalWork;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDate;

@WebServlet("/main/work-list/sort")
public class WorkSorting extends HttpServlet {

    public final String sortUrl = "/main/work-list/sort";
    public final String parameters = "?year=%s&month=%s";
    public final String yearParam = "year";
    public final String monthParam = "month";


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String jwt = (String) session.getAttribute(WebUtility.INSTANCE.sessionToken);
        int year = request.getParameter(yearParam) != null ? Integer.parseInt(request.getParameter(yearParam)) :
                (int) request.getAttribute(yearParam);
        int month = request.getParameter(monthParam) != null ? Integer.parseInt(request.getParameter(monthParam)) :
                (int) request.getAttribute(monthParam);
        String jsonWorks;
        try {
            jsonWorks = WebUtility.INSTANCE.requestSender().sendHttpGetRequest(jwt, sortUrl + String.format(parameters, year, month));
        } catch (APIResponseException e) {
            throw new RuntimeException(e);
        }
        DentalWork[] works = WebUtility.INSTANCE.parseFromJson(jsonWorks, DentalWork[].class);
        session.setAttribute(WebUtility.INSTANCE.sessionWorks, works);
        request.getRequestDispatcher("/main/work-list").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String jwt = (String) session.getAttribute(WebUtility.INSTANCE.sessionToken);

        int year = request.getParameter(yearParam) != null ? Integer.parseInt(request.getParameter(yearParam)) :
                (int) request.getAttribute(yearParam);
        int month = request.getParameter(monthParam) != null ? Integer.parseInt(request.getParameter(monthParam)) :
                (int) request.getAttribute(monthParam);

        try {
            DentalWork[] works = executeRequest(jwt, year, month);
            if (isCurrent(year, month)) {
                session.setAttribute(WebUtility.INSTANCE.sessionWorks, works);
            }
            request.setAttribute("works", works);
            request.getRequestDispatcher("/main/work-list").forward(request, response);
        } catch (APIResponseException e) {
            response.sendError(e.CODE, e.MESSAGE);
        }
    }


    private DentalWork[] executeRequest(String jwt, int year, int month) throws IOException, APIResponseException {
        WebUtility.QueryFormer queryFormer = new WebUtility.QueryFormer();
        queryFormer.add(yearParam, year);
        queryFormer.add(monthParam, month);
        String requestParams = queryFormer.form();
        String json = WebUtility.INSTANCE.requestSender().sendHttpPostRequest(jwt, sortUrl, requestParams);
        return WebUtility.INSTANCE.parseFromJson(json, DentalWork[].class);
    }

    private boolean isCurrent(int year, int month) {
        LocalDate now = LocalDate.now();
        return (year == now.getYear() && month == now.getMonthValue());
    }
}