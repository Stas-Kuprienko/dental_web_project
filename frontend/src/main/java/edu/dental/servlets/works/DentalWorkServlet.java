package edu.dental.servlets.works;

import edu.dental.beans.DentalWork;
import edu.dental.control.Administrator;
import edu.dental.control.DentalWorkService;
import edu.dental.service.WebUtility;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import stas.exceptions.HttpWebException;
import stas.http_tools.RestRequestIDReader;
import stas.utilities.LoggerKit;

import java.io.IOException;
import java.util.logging.Level;

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
    private static final String dentalWorksListURL = "/main/dental-works";

    private LoggerKit loggerKit;
    private DentalWorkService dentalWorkService;
    private RestRequestIDReader restRequestReader;


    @Override
    public void init() throws ServletException {
        this.loggerKit = WebUtility.INSTANCE.loggerKit();
        this.dentalWorkService = Administrator.getInstance().getDentalWorksService();
        this.restRequestReader = new RestRequestIDReader("/main/dental-work");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DentalWork dentalWork = (DentalWork) request.getAttribute(WebUtility.INSTANCE.attribWork);
        if (dentalWork == null) {
            try {
                HttpSession session = request.getSession();
                int id = getId(request);
                dentalWork = dentalWorkService.getDentalWorkById(session, id);
                request.setAttribute(WebUtility.INSTANCE.attribWork, dentalWork);
                request.getRequestDispatcher(dentalWorkPageURL).forward(request, response);
            } catch (HttpWebException e) {
                e.errorRedirect(WebUtility.INSTANCE.errorPageURL, request, response);
            }
        }
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
            } catch (HttpWebException e) {
                e.errorRedirect(WebUtility.INSTANCE.errorPageURL, request, response);
            }
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            HttpSession session = request.getSession();

            int id = getId(request);
            String field = request.getParameter(fieldParam);
            String value = request.getParameter(valueParam);
            String quantity = request.getParameter(quantityParam);

            DentalWork dentalWork = dentalWorkService.updateDentalWork(session, id, field, value, quantity);
            if (dentalWork.getReportId() == 0) {
                dentalWorkService.updateDentalWorkList(session, dentalWork);
            }
            request.setAttribute(WebUtility.INSTANCE.attribWork, dentalWork);
            request.getRequestDispatcher(dentalWorkPageURL).forward(request, response);
        } catch (HttpWebException e) {
            e.errorRedirect(WebUtility.INSTANCE.errorPageURL, request, response);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        try {
            int id = getId(request);
            String product = request.getParameter(productParam);
            if (product == null) {
                dentalWorkService.deleteDentalWorkFromList(session, id);
                request.getRequestDispatcher(dentalWorksListURL).forward(request, response);
            } else {
                DentalWork dentalWork = dentalWorkService.removeProductFromDentalWork(session, id, product);
                request.setAttribute(WebUtility.INSTANCE.attribWork, dentalWork);
                request.getRequestDispatcher(dentalWorkPageURL).forward(request, response);
            }
        } catch (HttpWebException e) {
            e.errorRedirect(WebUtility.INSTANCE.errorPageURL, request, response);
        }
    }


    private int getId(HttpServletRequest request) throws HttpWebException {
        int id = restRequestReader.getId(request.getRequestURI());
        if (id < 0) {
            String parameterId = request.getParameter(idParam);
            if (parameterId != null) {
                id = Integer.parseInt(parameterId);
            } else {
                IllegalArgumentException e = new IllegalArgumentException(request.getRequestURI());
                loggerKit.doLog(this.getClass().getSuperclass(), e, Level.WARNING);
                throw new HttpWebException(HttpWebException.ERROR.BAD_REQUEST);
            }
        }
        return id;
    }

    private void chooseMethod(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String method = request.getParameter("method");
        if (method.equals("put")) {
            doPut(request, response);
        } else if (method.equals("delete")) {
            doDelete(request, response);
        } else {
            NoSuchMethodException e = new NoSuchMethodException(method);
            loggerKit.doLog(this.getClass().getSuperclass(), e, Level.SEVERE);
            new HttpWebException(HttpWebException.ERROR.NOT_ALLOWED)
                    .errorRedirect(WebUtility.INSTANCE.errorPageURL, request, response);
        }
    }
}