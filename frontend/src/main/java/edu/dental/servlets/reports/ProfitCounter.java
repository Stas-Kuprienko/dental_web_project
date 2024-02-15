package edu.dental.servlets.reports;

import edu.dental.HttpWebException;
import edu.dental.beans.ProfitRecord;
import edu.dental.control.Administrator;
import edu.dental.control.ProfitRecordService;
import edu.dental.service.WebUtility;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/main/profit")
public class ProfitCounter extends HttpServlet {

    private static final String profitPageURL = "/main/profit/page";
    private static final String profitCountUrl = "/main/profit";
    private static final String fileFormat = ".xlsx";
    private static final String fileName = "profit_list";

    private ProfitRecordService profitRecordService;

    @Override
    public void init() throws ServletException {
        this.profitRecordService = Administrator.getInstance().getProfitRecordService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String jwt = (String) request.getSession().getAttribute(WebUtility.INSTANCE.attribToken);
            response.setContentType("application/msword");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + fileFormat + '\"');
            WebUtility.INSTANCE.requestSender().download(jwt, profitCountUrl, response.getOutputStream());
        } catch (HttpWebException e) {
            e.errorRedirect(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String year = request.getParameter("year");
            String month = request.getParameter("month");
            ProfitRecord[] records = profitRecordService.get(request.getSession(), year, month);
            request.setAttribute("profit", records);
            request.getRequestDispatcher(profitPageURL).forward(request, response);
        } catch (HttpWebException e) {
            e.errorRedirect(request, response);
        }
    }
}
