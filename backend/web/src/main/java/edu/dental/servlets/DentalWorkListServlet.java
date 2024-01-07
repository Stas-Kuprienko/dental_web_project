package edu.dental.servlets;

import edu.dental.WebAPI;
import edu.dental.domain.reports.ReportService;
import edu.dental.domain.reports.ReportServiceException;
import edu.dental.dto.DentalWorkDto;
import edu.dental.entities.DentalWork;
import edu.dental.service.tools.JsonObjectParser;
import edu.dental.service.Repository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/main/dental-works")
public class DentalWorkListServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = (int) request.getAttribute(WebAPI.INSTANCE.paramUser);
        String year = request.getParameter("year");
        String month = request.getParameter("month");
        try {
            List<DentalWorkDto> works = getDentalWorks(userId, year, month);
            String json = JsonObjectParser.getInstance().parseToJson(works.toArray());
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().print(json);
            response.getWriter().flush();
        } catch (ReportServiceException e) {
            //TODO
            response.sendError(500);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }


    private List<DentalWorkDto> getDentalWorks(int userId, String year, String month) throws ReportServiceException {
        if (year == null || year.isEmpty() && month == null || month.isEmpty()) {
            return Repository.getInstance().getDentalWorkDtoList(userId);
        } else {
            int yearInt = Integer.parseInt(year);
            int monthInt = Integer.parseInt(month);
            ReportService reportService = ReportService.getInstance();
            List<DentalWork> works = reportService.getReportFromDB(userId, monthInt, yearInt);
            return works.stream().map(DentalWorkDto::new).toList();
        }
    }
}