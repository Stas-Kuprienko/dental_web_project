package edu.dental.servlets.works;

import edu.dental.WebUtility;
import edu.dental.beans.DentalWork;
import edu.dental.service.WebRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/main/dental-works/search")
public class WorkRecordSearch extends HttpServlet {

    public final String recordSearchUrl = "main/dental-works/search";
    public final String patientParam = "patient";
    public final String clinicParam = "clinic";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = (int) request.getSession().getAttribute(WebUtility.INSTANCE.sessionUser);
        String jwt = WebRepository.INSTANCE.getToken(userId);
        String patient = request.getParameter(patientParam);
        String clinic = request.getParameter(clinicParam);

        WebUtility.QueryFormer queryFormer = new WebUtility.QueryFormer();
        queryFormer.add(patientParam, patient);
        queryFormer.add(clinicParam, clinic);
        String requestBody = queryFormer.form();
        String json = WebUtility.INSTANCE.requestSender().sendHttpPostRequest(jwt, recordSearchUrl, requestBody);
        DentalWork[] works = WebUtility.INSTANCE.parseFromJson(json, DentalWork[].class);
        String[] map = WebRepository.INSTANCE.getMap(userId).getKeys();
        request.setAttribute("works", works);
        request.setAttribute("map", map);
        request.getRequestDispatcher("/main/work-list/page").forward(request, response);
    }
}
