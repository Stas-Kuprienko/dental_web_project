package edu.dental.servlets;

import edu.dental.WebAPI;
import edu.dental.domain.reports.MonthlyReport;
import edu.dental.domain.reports.ReportService;
import edu.dental.domain.reports.ReportServiceException;
import edu.dental.dto.DentalWorkDto;
import edu.dental.service.JsonObjectParser;
import edu.dental.service.Repository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.Month;
import java.util.List;

@WebServlet("/main/dental-works")
public class DentalWorkListServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = (int) request.getAttribute(WebAPI.INSTANCE.paramUser);
        String year = request.getParameter("year");
        String month = request.getParameter("month");
        try {
            List<DentalWorkDto> works;
            if (year == null || year.isEmpty() && month == null || month.isEmpty()) {
                works = getCurrents(userId);
            } else {
                int monthValue = Integer.parseInt(month);
                works = getByMonth(userId, year, monthValue);

            }
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


    private List<DentalWorkDto> getCurrents(int userId) throws IOException {
        return Repository.getInstance().getDentalWorkDtoList(userId);
    }

    private List<DentalWorkDto> getByMonth(int userId, String year, int month) throws ReportServiceException {
        ReportService reportService = ReportService.getInstance();
        MonthlyReport monthly = reportService.getReportFromDB(userId, Month.of(month).toString(), year);
        return monthly.dentalWorks().stream().map(DentalWorkDto::new).toList();
    }
}
