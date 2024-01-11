package edu.dental.servlets;

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

    private JsonObjectParser jsonObjectParser;
    private Repository repository;
    private ReportService reportService;

    @Override
    public void init() throws ServletException {
        this.jsonObjectParser = JsonObjectParser.getInstance();
        this.repository = Repository.getInstance();
        this.reportService = ReportService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = (int) request.getAttribute(Repository.paramUser);
        String year = request.getParameter("year");
        String month = request.getParameter("month");
        try {
            List<DentalWorkDto> works = getDentalWorks(userId, year, month);
            String json = jsonObjectParser.parseToJson(works.toArray());
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
            return repository.getDentalWorkDtoList(userId);
        } else {
            int yearInt = Integer.parseInt(year);
            int monthInt = Integer.parseInt(month);
            List<DentalWork> works = reportService.getReportFromDB(userId, monthInt, yearInt);
            return works.stream().map(DentalWorkDto::new).toList();
        }
    }
}