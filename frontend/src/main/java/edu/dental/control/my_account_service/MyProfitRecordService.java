package edu.dental.control.my_account_service;

import edu.dental.HttpWebException;
import edu.dental.beans.DentalWork;
import edu.dental.beans.Product;
import edu.dental.beans.ProfitRecord;
import edu.dental.control.ProfitRecordService;
import edu.dental.service.HttpQueryFormer;
import edu.dental.service.WebUtility;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDate;

public class MyProfitRecordService implements ProfitRecordService {

    private static final String profitCountUrl = "/main/profit";

    MyProfitRecordService() {}

    @Override
    public ProfitRecord[] get(HttpSession session, String year, String month) throws IOException, HttpWebException {
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

    @Override
    public ProfitRecord[] countCurrent(HttpSession session) {
        DentalWork[] works = (DentalWork[]) session.getAttribute(WebUtility.INSTANCE.attribWorks);
        int amount = 0;
        for (DentalWork dw : works) {
            for (Product p : dw.getProducts()) {
                amount += p.getPrice() * p.getQuantity();
            }
        }
        LocalDate now = LocalDate.now();
        String month = now.getMonth().toString().toLowerCase();
        return new ProfitRecord[] {new ProfitRecord(now.getYear(), month, amount)};
    }

    @Override
    public ProfitRecord[] countAll(HttpSession session) throws IOException, HttpWebException {
        String jwt = (String) session.getAttribute(WebUtility.INSTANCE.attribToken);
        String json = WebUtility.INSTANCE.requestSender().sendHttpPostRequest(jwt, profitCountUrl, null);
        return WebUtility.INSTANCE.parseFromJson(json, ProfitRecord[].class);
    }

    @Override
    public ProfitRecord[] countAnother(HttpSession session, String year, String month) throws IOException, HttpWebException {
        String jwt = (String) session.getAttribute(WebUtility.INSTANCE.attribToken);
        HttpQueryFormer queryFormer = new HttpQueryFormer();
        queryFormer.add("year", year);
        queryFormer.add("month", month);
        String requestParam = queryFormer.form();
        String json = WebUtility.INSTANCE.requestSender().sendHttpPostRequest(jwt, profitCountUrl, requestParam);
        return WebUtility.INSTANCE.parseFromJson(json, ProfitRecord[].class);
    }
}
