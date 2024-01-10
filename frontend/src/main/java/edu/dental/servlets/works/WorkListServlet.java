package edu.dental.servlets.works;

import edu.dental.WebUtility;
import edu.dental.beans.DentalWork;
import edu.dental.service.WebRepository;
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
        int userId = (int) request.getSession().getAttribute(WebUtility.INSTANCE.sessionUser);
        String year_month = request.getParameter("year-month");
        if (year_month == null || year_month.isEmpty()) {
            DentalWork[] works = new DentalWork[]{};
            works = WebRepository.INSTANCE.getWorks(userId).toArray(works);
            request.setAttribute("works", works);
        } else {
            try {
                setRequiredWorkList(userId, year_month, request);
            } catch (Exception e) {
                //TODO
                response.sendError(400);
            }
        }
        String[] map = WebRepository.INSTANCE.getMap(userId).getKeys();
        request.setAttribute("map", map);

        request.getRequestDispatcher("/main/work-list/page").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }


    private void setRequiredWorkList(int userId, String year_month, HttpServletRequest request) throws IOException {
        String jwt = WebRepository.INSTANCE.getToken(userId);
        String[] year_month_split = year_month.split("-");
        String year = year_month_split[0];
        String month = year_month_split[1];
        LocalDate now = LocalDate.now();
        DentalWork[] works;
        if (now.getYear() == Integer.parseInt(year) && now.getMonthValue() == Integer.parseInt(month)) {
            works = new DentalWork[]{};
            works = WebRepository.INSTANCE.getWorks(userId).toArray(works);
        } else {
            String jsonWorks = WebUtility.INSTANCE.requestSender()
                    .sendHttpGetRequest(jwt, dentalWorksUrl + String.format(parameters, year, month));
            works = WebUtility.INSTANCE.parseFromJson(jsonWorks, DentalWork[].class);
        }
        request.setAttribute("works", works);
        request.setAttribute("year-month", year_month);
    }

    private boolean checkYearAndMonth() {
        return false;
    }
}
