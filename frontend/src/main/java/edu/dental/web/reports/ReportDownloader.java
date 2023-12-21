package edu.dental.web.reports;

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
            String fileName = "";
            response.setHeader("Content-Disposition","attachment; filename=\"" + fileName + "\"");
//            saveReport((String) request.getSession().getAttribute("user"), output);
    }


//    private void saveReport(String login, OutputStream output) throws ReportServiceException {
//        ReportService reportService = ReportService.getInstance();
//        WorkRecordBook recordBook = Repository.getInstance().getRecordBook(login);
//            reportService.saveReportToFile(output, recordBook.getMap().keysToArray(), recordBook.getList());
//    }
}
