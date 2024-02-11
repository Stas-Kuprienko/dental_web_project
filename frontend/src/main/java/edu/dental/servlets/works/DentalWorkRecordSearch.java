package edu.dental.servlets.works;

import edu.dental.APIResponseException;
import edu.dental.beans.DentalWork;
import edu.dental.control.Administrator;
import edu.dental.control.DentalWorksListService;
import edu.dental.service.WebUtility;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/main/dental-works/search")
public class DentalWorkRecordSearch extends HttpServlet {

    private static final String patientParam = "patient";
    private static final String clinicParam = "clinic";
    private static final String dentalWorksPageURL = "/main/dental-works/page";

    private DentalWorksListService dentalWorksListService;

    @Override
    public void init() throws ServletException {
        this.dentalWorksListService = Administrator.getInstance().getDentalWorksListService();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String jwt = (String) request.getSession().getAttribute(WebUtility.INSTANCE.attribToken);
        String patient = request.getParameter(patientParam);
        String clinic = request.getParameter(clinicParam);
        try {
            DentalWork[] works = dentalWorksListService.search(jwt, patient, clinic);
            request.setAttribute(WebUtility.INSTANCE.attribWorks, works);
            request.getRequestDispatcher(dentalWorksPageURL).forward(request, response);
        } catch (APIResponseException e) {
            e.errorRedirect(request, response);
        }
    }
}
