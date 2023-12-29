package edu.dental.servlets;

import edu.dental.WebAPI;
import edu.dental.domain.records.WorkRecordBook;
import edu.dental.domain.reports.ReportService;
import edu.dental.domain.reports.ReportServiceException;
import edu.dental.service.Repository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.OutputStream;

@WebServlet("/main/reports/download")
public class ReportDownloader extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        OutputStream output = response.getOutputStream();
        response.setContentType("application/msword");
        String fileFormat = ReportService.getInstance().getFileFormat();
        response.setHeader("Content-Disposition", "attachment; filename=\"" + "test" + fileFormat + "\"");
        try {
            int userId = (int) request.getAttribute(WebAPI.INSTANCE.paramUser);
            ReportService reportService = ReportService.getInstance();
            WorkRecordBook recordBook = Repository.getInstance().getRecordBook(userId);
            reportService.saveReportToFile(output, recordBook.getProductMap().keysToArray(), recordBook.getRecords());
        } catch (ReportServiceException e) {
            response.sendError(500);
        }
    }
}
