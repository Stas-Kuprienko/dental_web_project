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
import java.util.List;

@WebServlet("/main/work-list/sort")
public class WorkSorting extends HttpServlet {

    public final String sortUrl = "/main/work-list/sort";
    public final String parameters = "?year=%s&month=%s";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = (int) request.getSession().getAttribute(WebAPI.INSTANCE.sessionAttribute);
        String jwt = WebRepository.INSTANCE.getToken(userId);
        int year = request.getParameter("year") != null ? Integer.parseInt(request.getParameter("year")) :
                (int) request.getAttribute("year");
        int month = request.getParameter("month") != null ? Integer.parseInt(request.getParameter("month")) :
                (int) request.getAttribute("month");
        String jsonWorks = WebAPI.INSTANCE.requestSender().sendHttpGetRequest(jwt, sortUrl + String.format(parameters, year, month));
        List<DentalWork> works = List.of(JsonObjectParser.parser.fromJson(jsonWorks, DentalWork[].class));
        WebRepository.INSTANCE.setWorks(userId, works);
        request.getRequestDispatcher("/main/work-list").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
