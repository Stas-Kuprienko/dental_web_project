package edu.dental.servlets.reports;

import edu.dental.APIResponseException;
import edu.dental.WebUtility;
import edu.dental.beans.ProfitRecord;
import edu.dental.service.WebRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;

@WebServlet("/main/profit")
public class ProfitCounter extends HttpServlet {

    public final String profitCountUrl = "/main/profit";
    public final String fileFormat = ".xlsx";


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int userId = (int) request.getSession().getAttribute(WebUtility.INSTANCE.sessionUser);
            String jwt = WebRepository.INSTANCE.getToken(userId);
            String fileName = "profit_list";
            response.setContentType("application/msword");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + fileFormat + '\"');
            WebUtility.INSTANCE.requestSender().download(jwt, profitCountUrl, response.getOutputStream());
        } catch (APIResponseException e) {
            response.sendError(e.CODE, e.MESSAGE);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int userId = (int) request.getSession().getAttribute(WebUtility.INSTANCE.sessionUser);
            String year = request.getParameter("year");
            String month = request.getParameter("month");
            ProfitRecord[] records = getProfitRecords(userId, year, month);
            request.setAttribute("profit", records);
            request.getRequestDispatcher("/main/profit/page").forward(request, response);
        } catch (APIResponseException e) {
            response.sendError(e.CODE, e.MESSAGE);
        }
    }


    private ProfitRecord[] getProfitRecords(int userId, String year, String month) throws IOException, APIResponseException {
        if (year == null || year.isEmpty() && month == null || month.isEmpty()) {
            return countAll(userId);
        } else {
            LocalDate now = LocalDate.now();
            if (Integer.parseInt(year) == now.getYear() && Integer.parseInt(month) == now.getMonthValue()) {
                return countCurrent(userId);
            } else {
                return countAnother(userId, year, month);
            }
        }
    }

    private ProfitRecord[] countCurrent(int userId) {
        ProfitRecord record = WebRepository.INSTANCE.getProfitRecord(userId);
        return new ProfitRecord[] {record};
    }

    private ProfitRecord[] countAll(int userId) throws IOException, APIResponseException {
        String jwt = WebRepository.INSTANCE.getToken(userId);
        String json = WebUtility.INSTANCE.requestSender().sendHttpPostRequest(jwt, profitCountUrl, "");
        return WebUtility.INSTANCE.parseFromJson(json, ProfitRecord[].class);
    }

    private ProfitRecord[] countAnother(int userId, String year, String month) throws IOException, APIResponseException {
        String jwt = WebRepository.INSTANCE.getToken(userId);
        WebUtility.QueryFormer queryFormer = new WebUtility.QueryFormer();
        queryFormer.add("year", year);
        queryFormer.add("month", month);
        String requestParam = queryFormer.form();
        String json = WebUtility.INSTANCE.requestSender().sendHttpPostRequest(jwt, profitCountUrl, requestParam);
        return WebUtility.INSTANCE.parseFromJson(json, ProfitRecord[].class);
    }
}
