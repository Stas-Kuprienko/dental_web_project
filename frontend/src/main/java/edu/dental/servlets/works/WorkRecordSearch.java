package edu.dental.servlets.works;

import edu.dental.APIResponseException;
import edu.dental.service.WebUtility;
import edu.dental.beans.DentalWork;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/main/dental-works/search")
public class WorkRecordSearch extends HttpServlet {

    public final String recordSearchUrl = "main/dental-works/search";
    public final String patientParam = "patient";
    public final String clinicParam = "clinic";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String jwt = (String) session.getAttribute(WebUtility.INSTANCE.sessionToken);
        String patient = request.getParameter(patientParam);
        String clinic = request.getParameter(clinicParam);

        try {
            WebUtility.QueryFormer queryFormer = new WebUtility.QueryFormer();
            queryFormer.add(patientParam, patient);
            queryFormer.add(clinicParam, clinic);
            String requestBody = queryFormer.form();
            String json = WebUtility.INSTANCE.requestSender().sendHttpPostRequest(jwt, recordSearchUrl, requestBody);
            DentalWork[] works = WebUtility.INSTANCE.parseFromJson(json, DentalWork[].class);
            request.setAttribute("works", works);
            request.getRequestDispatcher("/main/work-list/page").forward(request, response);
        } catch (APIResponseException e) {
            response.sendError(e.CODE, e.MESSAGE);
        }
    }
}
