package edu.dental.servlets.works;

import edu.dental.APIResponseException;
import edu.dental.service.WebUtility;
import edu.dental.beans.DentalWork;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;

@WebServlet("/main/work-list")
public class WorkListServlet extends HttpServlet {

    public final String dentalWorksUrl = "/main/dental-works";
    public final String parameters = "?year=%s&month=%s";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String year_month = request.getParameter("year-month");
        if (year_month != null && !year_month.isEmpty()) {
            try {
                setRequiredWorkList(year_month, request);
            } catch (APIResponseException e) {
                response.sendError(e.CODE, e.MESSAGE);
            }
        }
        request.getRequestDispatcher("/main/work-list/page").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }


    private void setRequiredWorkList(String year_month, HttpServletRequest request) throws IOException, APIResponseException {
        String jwt = (String) request.getSession().getAttribute(WebUtility.INSTANCE.sessionToken);
        String[] year_month_split = year_month.split("-");
        int year = Integer.parseInt(year_month_split[0]);
        int month = Integer.parseInt(year_month_split[1]);
        DentalWork[] works;
        if (isCurrentMonth(year, month)) {
            String jsonWorks = WebUtility.INSTANCE.requestSender()
                    .sendHttpGetRequest(jwt, dentalWorksUrl + String.format(parameters, year, month));
            works = WebUtility.INSTANCE.parseFromJson(jsonWorks, DentalWork[].class);
            request.setAttribute(WebUtility.INSTANCE.sessionWorks, works);
            request.setAttribute("year-month", year_month);
        }
    }

    private boolean isCurrentMonth(int year, int month) {
        LocalDate now = LocalDate.now();
        int currentYear = now.getYear();
        int currentMonth = now.getMonthValue();
        return month != currentMonth || currentYear != year;
    }
}
