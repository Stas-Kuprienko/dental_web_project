package edu.dental.web.reports;

//import edu.dental.domain.records.WorkRecordBook;
//import edu.dental.domain.reports.ReportService;
//import edu.dental.domain.reports.ReportServiceException;
//import edu.dental.domain.utils.DatesTool;
//import edu.dental.web.Repository;
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
//        try {
//            OutputStream output = response.getOutputStream();
//            response.setContentType("application/msword");
//            String[] yearAndMonth = DatesTool.getYearAndMonth();
//            String fileName = yearAndMonth[1] + "_" + yearAndMonth[0] + ReportService.getInstance().getFileFormat();
//            response.setHeader("Content-Disposition","attachment; filename=\"" + fileName + "\"");
//            saveReport((String) request.getSession().getAttribute("user"), output);
//        } catch (ReportServiceException e) {
//            response.sendError(500);
//        }
    }


//    private void saveReport(String login, OutputStream output) throws ReportServiceException {
//        ReportService reportService = ReportService.getInstance();
//        WorkRecordBook recordBook = Repository.getInstance().getRecordBook(login);
//            reportService.saveReportToFile(output, recordBook.getMap().keysToArray(), recordBook.getList());
//    }
}
