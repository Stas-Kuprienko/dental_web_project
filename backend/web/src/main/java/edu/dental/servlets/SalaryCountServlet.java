package edu.dental.servlets;

import edu.dental.WebAPI;
import edu.dental.domain.reports.ReportService;
import edu.dental.domain.reports.ReportServiceException;
import edu.dental.dto.SalaryRecordDto;
import edu.dental.service.tools.JsonObjectParser;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.OutputStream;

@WebServlet("/main/salary")
public class SalaryCountServlet extends HttpServlet {

    private ReportService reportService;
    private JsonObjectParser jsonObjectParser;

    @Override
    public void init() throws ServletException {
        this.reportService = ReportService.getInstance();
        this.jsonObjectParser = JsonObjectParser.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        OutputStream output = response.getOutputStream();
        int userId = (int) request.getAttribute(WebAPI.INSTANCE.paramUser);
        response.setContentType("application/msword");
        String fileFormat = reportService.getFileFormat();
        String fileName = "salary_list";
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + fileFormat + "\"");
        try {
            reportService.writeSalariesToOutput(userId, output);
        } catch (ReportServiceException e) {
            response.sendError(500);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = (int) request.getAttribute(WebAPI.INSTANCE.paramUser);
        String year = request.getParameter("year");
        String month = request.getParameter("month");
        try {
            SalaryRecordDto[] records = getSalaryRecords(userId, year, month);
            String json = jsonObjectParser.parseToJson(records);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().print(json);
            response.getWriter().flush();
        } catch (ReportServiceException e) {
            response.sendError(500);
        }
    }


    private SalaryRecordDto[] getSalaryRecords(int userId, String year, String month) throws ReportServiceException {
        if (year == null || year.isEmpty() && month == null || month.isEmpty()) {

            return SalaryRecordDto.parseArray(reportService.countAllSalaries(userId));
        } else {
            SalaryRecordDto dto = SalaryRecordDto
                    .parse(reportService.countSalaryForMonth(userId, year, month));

            return new SalaryRecordDto[] {dto};
        }
    }
}
