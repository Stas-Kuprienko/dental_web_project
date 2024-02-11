package edu.dental.servlets.reports;

import edu.dental.APIResponseException;
import edu.dental.service.WebUtility;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;

@WebServlet("/main/reports/download")
public class ReportDownloader extends HttpServlet {

    private static final String reportsDownloadUrl = "main/reports/download";
    private static final String fileFormat = ".xlsx";


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String jwt = (String) request.getSession().getAttribute(WebUtility.INSTANCE.attribToken);
        String year = request.getParameter("year");
        String month = request.getParameter("month");
        String resource = clarifyResource(year, month);
        String fileName = getFileName(year, month);
        response.setContentType("application/msword");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + fileFormat + '\"');
        try {
            WebUtility.INSTANCE.requestSender().download(jwt, resource, response.getOutputStream());
        } catch (APIResponseException e) {
            response.sendError(e.CODE, e.MESSAGE);
        }
    }


    private String clarifyResource(String year, String month) {
        if (hasYearAndMonth(year, month)) {
            return reportsDownloadUrl;
        } else {
            LocalDate now = LocalDate.now();
            if (Integer.parseInt(year) == now.getYear() && Integer.parseInt(month) == now.getMonthValue()) {
                return reportsDownloadUrl;
            } else {
                WebUtility.QueryFormer queryFormer = new WebUtility.QueryFormer();
                queryFormer.add("year", year);
                queryFormer.add("month", month);
                return reportsDownloadUrl + "?" + queryFormer.form();
            }
        }
    }

    private String getFileName(String year, String month) {
        if (hasYearAndMonth(year, month)) {
            LocalDate now = LocalDate.now();
            return now.getMonth() + "_" + now.getYear();
        } else {
            return Month.of(Integer.parseInt(month)) + "_" + year;
        }
    }

    private boolean hasYearAndMonth(String year, String month) {
        return (year == null || year.isEmpty()
                && month == null || month.isEmpty());
    }
}
