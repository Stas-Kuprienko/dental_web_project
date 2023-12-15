package edu.dental.web.servlets.reports;

import edu.dental.domain.Action;
import edu.dental.domain.reports.ReportService;
import edu.dental.domain.utils.DatesTool;
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
        try {
            OutputStream output = response.getOutputStream();
            response.setContentType("application/msword");
            String[] yearAndMonth = DatesTool.getYearAndMonth();
            String fileName = yearAndMonth[1] + "_" + yearAndMonth[0] + ReportService.getInstance().getFileFormat();
            response.setHeader("Content-Disposition","attachment; filename=\"" + fileName + "\"");
            Action.saveReport((String) request.getSession().getAttribute("user"), output);
        } catch (Action.ActionException e) {
            response.sendError(e.CODE);
        }
    }
}
