package edu.dental.servlets.works;

import edu.dental.WebAPI;
import edu.dental.beans.DentalWork;
import edu.dental.service.JsonObjectParser;
import edu.dental.service.WebRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/main/work-list")
public class WorkListServlet extends HttpServlet {

    public final String dentalWorksUrl = "/main/dental-works";
    public final String parameters = "?year=%s&month=%s";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = (int) request.getSession().getAttribute(WebAPI.INSTANCE.sessionAttribute);
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

        String jsonWorks = WebAPI.INSTANCE.requestSender()
                .sendHttpGetRequest(jwt, dentalWorksUrl + String.format(parameters, year, month));
        DentalWork[] works = JsonObjectParser.parser.fromJson(jsonWorks, DentalWork[].class);
        request.setAttribute("works", works);
        request.setAttribute("year-month", year_month);
    }
}
