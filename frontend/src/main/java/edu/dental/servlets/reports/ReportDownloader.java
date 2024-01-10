package edu.dental.servlets.reports;

import edu.dental.WebUtility;
import edu.dental.service.WebRepository;
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

    public final String reportsDownloadUrl = "main/reports/download";
    public final String fileFormat = ".xlsx";


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = (int) request.getSession().getAttribute(WebUtility.INSTANCE.sessionUser);
        String jwt = WebRepository.INSTANCE.getToken(userId);
        String year = request.getParameter("year");
        String month = request.getParameter("month");
        String resource = clarifyResource(year, month);
        String fileName = getFileName(year, month);
        response.setContentType("application/msword");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + fileFormat + '\"');
        WebUtility.INSTANCE.requestSender().download(jwt, resource, response.getOutputStream());
    }


    private String clarifyResource(String year, String month) {
        if (year == null || year.isEmpty() && month == null || month.isEmpty()) {
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
        if (year == null || year.isEmpty() && month == null || month.isEmpty()) {
            LocalDate now = LocalDate.now();
            return now.getMonth() + "_" + now.getYear();
        } else {
            return Month.of(Integer.parseInt(month)) + "_" + year;
        }
    }
}
