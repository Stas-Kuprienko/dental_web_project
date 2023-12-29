package edu.dental.servlets.reports;

import edu.dental.WebAPI;
import edu.dental.service.WebRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/main/reports/download")
public class ReportDownloader extends HttpServlet {

    public final String reportsDownloadUrl = "main/reports/download";
    public final String fileFormat = "test.xlsx";


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = (int) request.getSession().getAttribute(WebAPI.INSTANCE.sessionAttribute);
        String jwt = WebRepository.INSTANCE.getToken(userId);
        response.setContentType("application/msword");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileFormat + "\"");
        WebAPI.INSTANCE.requestSender().download(jwt, reportsDownloadUrl, response.getOutputStream());
    }
}
