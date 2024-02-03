package edu.dental.servlets.works;

import edu.dental.APIResponseException;
import edu.dental.beans.DentalWork;
import edu.dental.service.WebUtility;
import edu.dental.control.Administrator;
import edu.dental.control.DentalWorkService;
import edu.http_utils.RestRequestReader;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet({"/main/dental-work", "/main/dental-work/*"})
public class DentalWorkServlet extends HttpServlet {

    private static final String idParam = "id";
    private static final String patientParam = "patient";
    private static final String clinicParam = "clinic";
    private static final String productParam = "product";
    private static final String quantityParam = "quantity";
    private static final String completeParam = "complete";
    private static final String fieldParam = "field";
    private static final String valueParam = "value";
    private static final String dentalWorkPageURL = "/main/dental-work/page";

    private DentalWorkService dentalWorkService;
    private RestRequestReader restRequestReader;


    @Override
    public void init() throws ServletException {
        this.dentalWorkService = Administrator.getInstance().getDentalWorksService();
        this.restRequestReader = new RestRequestReader("/main/dental-work");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DentalWork dentalWork = (DentalWork) request.getAttribute(WebUtility.INSTANCE.attribWork);
        if (dentalWork == null) {
            try {
                HttpSession session = request.getSession();
                int workId;
                workId = restRequestReader.getId(request.getRequestURI());
                if (workId < 0) {
                    String parameterId = request.getParameter(idParam);
                    if (parameterId != null) {
                        workId = Integer.parseInt(parameterId);
                    } else {
                        response.sendError(400, "parameter ID is null");
                        return;
                    }
                }
                dentalWork = dentalWorkService.getDentalWorkById(session, workId);
            } catch (APIResponseException e) {
                response.sendError(e.CODE, e.MESSAGE);
            }
        }
        request.setAttribute(WebUtility.INSTANCE.attribWork, dentalWork);
        request.getRequestDispatcher(dentalWorkPageURL).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getParameter("method") != null) {
            chooseMethod(request, response);
        } else {
            HttpSession session = request.getSession();
            try {
                String patient = request.getParameter(patientParam);
                String clinic = request.getParameter(clinicParam);
                String product = request.getParameter(productParam);
                int quantity = Integer.parseInt(request.getParameter(quantityParam));
                String complete = request.getParameter(completeParam);

                DentalWork dentalWork = dentalWorkService.createWork(session, patient, clinic, product, quantity, complete);
                dentalWorkService.updateDentalWorkList(session, dentalWork);
                request.setAttribute(WebUtility.INSTANCE.attribWork, dentalWork);
                request.getRequestDispatcher(dentalWorkPageURL).forward(request, response);
            } catch (APIResponseException e) {
                response.sendError(e.CODE, e.MESSAGE);
            }
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            HttpSession session = request.getSession();

            int id = Integer.parseInt(request.getParameter(idParam));
            String field = request.getParameter(fieldParam);
            String value = request.getParameter(valueParam);
            String quantity = request.getParameter(quantityParam);

            DentalWork dentalWork = dentalWorkService.updateDentalWork(session, id, field, value, quantity);
            if (dentalWork.getReportId() == 0) {
                dentalWorkService.updateDentalWorkList(session, dentalWork);
            }
            request.setAttribute(WebUtility.INSTANCE.attribWork, dentalWork);
            request.getRequestDispatcher(dentalWorkPageURL).forward(request, response);
        } catch (APIResponseException e) {
            response.sendError(e.CODE, e.MESSAGE);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        try {
            int id = Integer.parseInt(request.getParameter(idParam));
            String product = request.getParameter(productParam);
            if (product == null) {
                dentalWorkService.deleteDentalWorkFromList(session, id);
                request.getRequestDispatcher("/main/work-list").forward(request, response);
            } else {
                DentalWork dentalWork = dentalWorkService.removeProductFromDentalWork(session, id, product);
                request.setAttribute(WebUtility.INSTANCE.attribWork, dentalWork);
                request.getRequestDispatcher(dentalWorkPageURL).forward(request, response);
            }
        } catch (APIResponseException e) {
            response.sendError(e.CODE, e.MESSAGE);
        }
    }


    private void chooseMethod(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String method = request.getParameter("method");
        if (method.equals("put")) {
            doPut(request, response);
        } else if (method.equals("delete")) {
            doDelete(request, response);
        } else {
            response.sendError(405);
        }
    }
}