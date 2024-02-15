package edu.dental.servlets.reports;

import edu.dental.database.DatabaseException;
import edu.dental.domain.records.WorkRecordBook;
import edu.dental.domain.records.WorkRecordException;
import edu.dental.domain.reports.ReportService;
import edu.dental.domain.reports.ReportException;
import edu.dental.dto.ProfitRecordDto;
import edu.dental.entities.ProfitRecord;
import edu.dental.service.Repository;
import edu.dental.service.JsonObjectParser;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.OutputStream;

@WebServlet("/main/profit")
public class ProfitCountServlet extends HttpServlet {

    private Repository repository;
    private ReportService reportService;
    private JsonObjectParser jsonObjectParser;

    @Override
    public void init() throws ServletException {
        this.repository = Repository.getInstance();
        this.reportService = ReportService.getInstance();
        this.jsonObjectParser = JsonObjectParser.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        OutputStream output = response.getOutputStream();
        int userId = (int) request.getAttribute(Repository.paramUser);
        response.setContentType("application/msword");
        String fileFormat = reportService.getFileFormat();
        String fileName = "profit_list";
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + fileFormat + "\"");
        try {
            WorkRecordBook recordBook = repository.getRecordBook(userId);
            ProfitRecord[] records = recordBook.countAllProfits();
            reportService.writeProfitToOutput(records, output);
        } catch (ReportException | DatabaseException e) {
            response.sendError(500);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = (int) request.getAttribute(Repository.paramUser);
        String year = request.getParameter("year");
        String month = request.getParameter("month");
        try {
            ProfitRecordDto[] records = getProfitRecords(userId, year, month);
            String json = jsonObjectParser.parseToJson(records);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().print(json);
            response.getWriter().flush();
        } catch (WorkRecordException | DatabaseException e) {
            response.sendError(500);
        }
    }


    private ProfitRecordDto[] getProfitRecords(int userId, String year, String month) throws DatabaseException, WorkRecordException {
        WorkRecordBook recordBook = repository.getRecordBook(userId);
        if (year == null || year.isEmpty() && month == null || month.isEmpty()) {

            return ProfitRecordDto.parseArray(recordBook.countAllProfits());
        } else {
            ProfitRecordDto dto = new ProfitRecordDto
                    (recordBook.countProfitForMonth(Integer.parseInt(year), Integer.parseInt(month)));

            return new ProfitRecordDto[] {dto};
        }
    }
}
