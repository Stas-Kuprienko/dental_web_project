package edu.dental.servlets.reports;

import edu.dental.APIResponseException;
import edu.dental.WebUtility;
import edu.dental.beans.DentalWork;
import edu.dental.beans.Product;
import edu.dental.beans.ProfitRecord;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDate;

@WebServlet("/main/profit")
public class ProfitCounter extends HttpServlet {

    public final String profitCountUrl = "/main/profit";
    public final String fileFormat = ".xlsx";


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String jwt = (String) request.getSession().getAttribute(WebUtility.INSTANCE.sessionToken);
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
            String year = request.getParameter("year");
            String month = request.getParameter("month");
            ProfitRecord[] records = getProfitRecords(request.getSession(), year, month);
            request.setAttribute("profit", records);
            request.getRequestDispatcher("/main/profit/page").forward(request, response);
        } catch (APIResponseException e) {
            response.sendError(e.CODE, e.MESSAGE);
        }
    }


    private ProfitRecord[] getProfitRecords(HttpSession session, String year, String month) throws IOException, APIResponseException {
        if (year == null || year.isEmpty() && month == null || month.isEmpty()) {
            return countAll(session);
        } else {
            LocalDate now = LocalDate.now();
            if (Integer.parseInt(year) == now.getYear() && Integer.parseInt(month) == now.getMonthValue()) {
                return countCurrent(session);
            } else {
                return countAnother(session, year, month);
            }
        }
    }

    private ProfitRecord[] countCurrent(HttpSession session) {
        DentalWork[] works = (DentalWork[]) session.getAttribute(WebUtility.INSTANCE.sessionWorks);
        int amount = 0;
        for (DentalWork dw : works) {
            for (Product p : dw.getProducts()) {
                amount += p.getPrice() * p.getQuantity();
            }
        }
        LocalDate now = LocalDate.now();
        String month = now.getMonth().toString().toLowerCase();
        ProfitRecord record = new ProfitRecord(now.getYear(), month, amount);
        return new ProfitRecord[] {record};
    }

    private ProfitRecord[] countAll(HttpSession session) throws IOException, APIResponseException {
        String jwt = (String) session.getAttribute(WebUtility.INSTANCE.sessionToken);
        String json = WebUtility.INSTANCE.requestSender().sendHttpPostRequest(jwt, profitCountUrl, "");
        return WebUtility.INSTANCE.parseFromJson(json, ProfitRecord[].class);
    }

    private ProfitRecord[] countAnother(HttpSession session, String year, String month) throws IOException, APIResponseException {
        String jwt = (String) session.getAttribute(WebUtility.INSTANCE.sessionToken);
        WebUtility.QueryFormer queryFormer = new WebUtility.QueryFormer();
        queryFormer.add("year", year);
        queryFormer.add("month", month);
        String requestParam = queryFormer.form();
        String json = WebUtility.INSTANCE.requestSender().sendHttpPostRequest(jwt, profitCountUrl, requestParam);
        return WebUtility.INSTANCE.parseFromJson(json, ProfitRecord[].class);
    }
}
