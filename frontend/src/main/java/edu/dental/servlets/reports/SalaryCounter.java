package edu.dental.servlets.reports;

import edu.dental.WebAPI;
import edu.dental.beans.SalaryRecord;
import edu.dental.service.HttpRequestSender;
import edu.dental.service.JsonObjectParser;
import edu.dental.service.WebRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;

@WebServlet("/main/salary")
public class SalaryCounter extends HttpServlet {

    public final String salaryCountUrl = "/main/salary";
    public final String fileFormat = ".xlsx";


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = (int) request.getSession().getAttribute(WebAPI.INSTANCE.sessionAttribute);
        String jwt = WebRepository.INSTANCE.getToken(userId);
        String fileName = "salary_list";
        response.setContentType("application/msword");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + fileFormat + '\"');
        WebAPI.INSTANCE.requestSender().download(jwt, salaryCountUrl, response.getOutputStream());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = (int) request.getSession().getAttribute(WebAPI.INSTANCE.sessionAttribute);
        String year = request.getParameter("year");
        String month = request.getParameter("month");
        SalaryRecord[] records = getSalaryRecords(userId, year, month);
        request.setAttribute("salary", records);
        request.getRequestDispatcher("/main/salary/page").forward(request, response);
    }


    private SalaryRecord[] getSalaryRecords(int userId, String year, String month) throws IOException {
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

    private SalaryRecord[] countCurrent(int userId) {
        SalaryRecord record = WebRepository.INSTANCE.getSalaryRecord(userId);
        return new SalaryRecord[] {record};
    }

    private SalaryRecord[] countAll(int userId) throws IOException {
        String jwt = WebRepository.INSTANCE.getToken(userId);
        String json = WebAPI.INSTANCE.requestSender().sendHttpPostRequest(jwt, salaryCountUrl, "");
        return JsonObjectParser.parser.fromJson(json, SalaryRecord[].class);
    }

    private SalaryRecord[] countAnother(int userId, String year, String month) throws IOException {
        String jwt = WebRepository.INSTANCE.getToken(userId);
        HttpRequestSender.QueryFormer queryFormer = new HttpRequestSender.QueryFormer();
        queryFormer.add("year", year);
        queryFormer.add("month", month);
        String requestParam = queryFormer.form();
        String json = WebAPI.INSTANCE.requestSender().sendHttpPostRequest(jwt, salaryCountUrl, requestParam);
        return JsonObjectParser.parser.fromJson(json, SalaryRecord[].class);
    }
}
