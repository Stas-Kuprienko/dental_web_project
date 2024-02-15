package edu.dental.control.my_account_service;

import edu.dental.HttpWebException;
import edu.dental.beans.DentalWork;
import edu.dental.control.DentalWorksListService;
import edu.dental.service.HttpQueryFormer;
import edu.dental.service.WebUtility;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDate;

public class MyDentalWorksListService implements DentalWorksListService {

    private static final String dentalWorksUrl = "/main/dental-works";
    private static final String sortUrl = "/main/work-list";
    private static final String recordSearchUrl = "main/dental-works/search";
    private static final String parameters = "?year=%s&month=%s";
    private static final String yearParam = "year";
    private static final String monthParam = "month";
    private static final String patientParam = "patient";
    private static final String clinicParam = "clinic";

    MyDentalWorksListService() {}

    @Override
    public void getRequired(String year_month, HttpServletRequest request) throws IOException, HttpWebException {
        String jwt = (String) request.getSession().getAttribute(WebUtility.INSTANCE.attribToken);
        String[] year_month_split = year_month.split("-");
        int year = Integer.parseInt(year_month_split[0]);
        int month = Integer.parseInt(year_month_split[1]);
        if (!isCurrent(year, month)) {
            String jsonWorks = WebUtility.INSTANCE.requestSender()
                    .sendHttpGetRequest(jwt, dentalWorksUrl + String.format(parameters, year, month));
            DentalWork[] works = WebUtility.INSTANCE.parseFromJson(jsonWorks, DentalWork[].class);
            request.setAttribute(WebUtility.INSTANCE.attribWorks, works);
            request.setAttribute("year-month", year_month);
        }
    }

    @Override
    public void sort(HttpSession session, int year, int month) throws IOException, HttpWebException {
        String jwt = (String) session.getAttribute(WebUtility.INSTANCE.attribToken);
        HttpQueryFormer queryFormer = new HttpQueryFormer();
        queryFormer.add(yearParam, year);
        queryFormer.add(monthParam, month);
        String jsonWorks = WebUtility.INSTANCE.requestSender()
                .sendHttpPostRequest(jwt, dentalWorksUrl, queryFormer.form());
        DentalWork[] works = WebUtility.INSTANCE.parseFromJson(jsonWorks, DentalWork[].class);
        session.setAttribute(WebUtility.INSTANCE.attribWorks, works);
    }

    @Override
    public DentalWork[] setStatus(HttpSession session, int year, int month) throws IOException, HttpWebException {
        String jwt = (String) session.getAttribute(WebUtility.INSTANCE.attribToken);
        HttpQueryFormer queryFormer = new HttpQueryFormer();
        queryFormer.add(yearParam, year);
        queryFormer.add(monthParam, month);
        String requestParams = queryFormer.form();
        String json = WebUtility.INSTANCE.requestSender().sendHttpPutRequest(jwt, dentalWorksUrl, requestParams);
        DentalWork[] works = WebUtility.INSTANCE.parseFromJson(json, DentalWork[].class);
        if (isCurrent(year, month)) {
            session.setAttribute(WebUtility.INSTANCE.attribWorks, works);
        }
        return works;
    }

    @Override
    public DentalWork[] search(String jwt, String patient, String clinic) throws IOException, HttpWebException {
        HttpQueryFormer queryFormer = new HttpQueryFormer();
        queryFormer.add(patientParam, patient);
        queryFormer.add(clinicParam, clinic);
        String requestBody = queryFormer.form();
        String json = WebUtility.INSTANCE.requestSender().sendHttpPostRequest(jwt, recordSearchUrl, requestBody);
        return WebUtility.INSTANCE.parseFromJson(json, DentalWork[].class);
    }


    private boolean isCurrent(int year, int month) {
        LocalDate now = LocalDate.now();
        return (month == now.getMonthValue() && year == now.getYear());
    }
}
