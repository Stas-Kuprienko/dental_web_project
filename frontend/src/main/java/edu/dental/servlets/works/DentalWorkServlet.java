package edu.dental.servlets.works;

import edu.dental.APIResponseException;
import edu.dental.WebUtility;
import edu.dental.beans.DentalWork;
import edu.dental.beans.ProductMap;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

@WebServlet("/main/dental-work")
public class DentalWorkServlet extends HttpServlet {

    public final String dentalWorkUrl = "main/dental-work";
    public final String idParam = "id";
    public final String patientParam = "patient";
    public final String clinicParam = "clinic";
    public final String productParam = "product";
    public final String quantityParam = "quantity";
    public final String completeParam = "complete";
    public final String fieldParam = "field";
    public final String valueParam = "value";


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DentalWork dentalWork = (DentalWork) request.getAttribute("work");
        if (dentalWork == null) {
            try {
                setWorkToRequest(request);
            } catch (APIResponseException e) {
                response.sendError(e.CODE, e.MESSAGE);
            }
        }
        ProductMap map = (ProductMap) request.getSession().getAttribute(WebUtility.INSTANCE.sessionMap);
        request.setAttribute(WebUtility.INSTANCE.sessionMap, map.getKeys());

        request.getRequestDispatcher("/main/dental-work/page").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String method = request.getParameter("method");
            if (method != null) {
                if (method.equals("put")) {
                    doPut(request, response);
                } else if (method.equals("delete")) {
                    doDelete(request, response);
                }
                response.sendError(400);
            } else {
                newDentalWork(request);
                doGet(request, response);
            }
        } catch (APIResponseException e) {
            response.sendError(e.CODE, e.MESSAGE);
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            editWork(request);
            doGet(request, response);
        } catch (NullPointerException e) {
            request.getRequestDispatcher("/main/work-list").forward(request, response);
        } catch (APIResponseException e) {
            response.sendError(e.CODE, e.MESSAGE);
        }
    }
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter(idParam));
            String product = request.getParameter(productParam);
            if (product != null) {
                deleteProductFromWork(request.getSession(), id, product);
                request.setAttribute(idParam, id);
                doGet(request, response);
            } else {
                deleteWorkRecord(request.getSession(), id);
                request.getRequestDispatcher("/main/work-list").forward(request, response);
            }
        } catch (APIResponseException e) {
            response.sendError(e.CODE, e.MESSAGE);
        }
    }


    private void setWorkToRequest(HttpServletRequest request) throws IOException, APIResponseException {
        int workId = request.getParameter(idParam) != null ? Integer.parseInt(request.getParameter(idParam)) :
                (int) request.getAttribute(idParam);
        HttpSession session = request.getSession();
        DentalWork work;
        try {
            DentalWork[] works = (DentalWork[]) session.getAttribute(WebUtility.INSTANCE.sessionWorks);
            work = Arrays.stream(works).filter(dw -> dw.getId() == workId).findAny().orElseThrow();
        } catch (NoSuchElementException e) {
            WebUtility.QueryFormer queryFormer = new WebUtility.QueryFormer();
            queryFormer.add(idParam, workId);
            String requestParam = queryFormer.form();

            String jwt = (String) session.getAttribute(WebUtility.INSTANCE.sessionToken);
            String json = WebUtility.INSTANCE.requestSender().sendHttpGetRequest(jwt, dentalWorkUrl + "?" + requestParam);
            work = WebUtility.INSTANCE.parseFromJson(json, DentalWork.class);
        }
        request.setAttribute("work", work);
    }

    private void newDentalWork(HttpServletRequest request) throws IOException, APIResponseException {
        String patient = request.getParameter(patientParam);
        String clinic = request.getParameter(clinicParam);
        String product = request.getParameter(productParam);
        int quantity = Integer.parseInt(request.getParameter(quantityParam));
        LocalDate complete = LocalDate.parse(request.getParameter(completeParam));

        int id = createDentalWork(request.getSession(), patient, clinic, product, quantity, complete);
        request.setAttribute(idParam, id);
    }

    private int createDentalWork(HttpSession session, String patient, String clinic, String product, int quantity, LocalDate complete) throws IOException, APIResponseException {
        String jwt = (String) session.getAttribute(WebUtility.INSTANCE.sessionToken);
        DentalWork dentalWork;
        WebUtility.QueryFormer queryFormer = new WebUtility.QueryFormer();
        queryFormer.add(patientParam, patient);
        queryFormer.add(clinicParam, clinic);
        queryFormer.add(productParam, product);
        queryFormer.add(quantityParam, quantity);
        queryFormer.add(completeParam, complete);
        String requestParam = queryFormer.form();

        String json = WebUtility.INSTANCE.requestSender().sendHttpPostRequest(jwt, dentalWorkUrl, requestParam);

        dentalWork = WebUtility.INSTANCE.parseFromJson(json, DentalWork.class);
        updateDentalWorks(session, dentalWork);
        return dentalWork.getId();
    }

    private void editWork(HttpServletRequest request) throws IOException, NullPointerException, APIResponseException {
        HttpSession session = request.getSession();
        String jwt = (String) session.getAttribute(WebUtility.INSTANCE.sessionToken);

        int id = Integer.parseInt(request.getParameter(idParam));
        String field = request.getParameter(fieldParam);
        String value = request.getParameter(valueParam);
        String requestParam = buildRequestParameters(request, id, field, value);
        String json = WebUtility.INSTANCE.requestSender().sendHttpPutRequest(jwt, dentalWorkUrl, requestParam);

        DentalWork dentalWork = WebUtility.INSTANCE.parseFromJson(json, DentalWork.class);
        if (dentalWork.getReportId() == 0) {
            updateDentalWorks(session, dentalWork);
        }
        request.setAttribute("work", dentalWork);
    }

    private String buildRequestParameters(HttpServletRequest request, int id, String field, String value) throws NullPointerException {
        WebUtility.QueryFormer queryFormer = new WebUtility.QueryFormer();
        queryFormer.add(idParam, id);
        queryFormer.add(fieldParam, field);
        queryFormer.add(valueParam, value);
        if (field.equals(productParam)) {
            if (!(value == null || value.isEmpty())) {
                int quantity = Integer.parseInt(request.getParameter(quantityParam));
                queryFormer.add(quantityParam, quantity);
            } else {
                throw new NullPointerException();
            }
        }
        return queryFormer.form();
    }

    private void deleteProductFromWork(HttpSession session, int id, String product) throws IOException, APIResponseException {
        String jwt = (String) session.getAttribute(WebUtility.INSTANCE.sessionToken);
        WebUtility.QueryFormer queryFormer = new WebUtility.QueryFormer();
        queryFormer.add(idParam, id);
        queryFormer.add(productParam, product);
        DentalWork dentalWork;

        String json = WebUtility.INSTANCE.requestSender().sendHttpDeleteRequest(jwt, dentalWorkUrl, queryFormer.form());

        dentalWork = WebUtility.INSTANCE.parseFromJson(json, DentalWork.class);
        updateDentalWorks(session, dentalWork);
    }

    private void deleteWorkRecord(HttpSession session, int id) throws IOException, APIResponseException {
        String jwt = (String) session.getAttribute(WebUtility.INSTANCE.sessionToken);
        WebUtility.QueryFormer queryFormer = new WebUtility.QueryFormer();
        queryFormer.add(idParam, id);

        WebUtility.INSTANCE.requestSender().sendHttpDeleteRequest(jwt, dentalWorkUrl, queryFormer.form());

        removeDentalWorkFromList(session, id);
    }

    private void updateDentalWorks(HttpSession session, DentalWork dw) {
        DentalWork[] works = (DentalWork[]) session.getAttribute(WebUtility.INSTANCE.sessionWorks);
        ArrayList<DentalWork> workList = new ArrayList<>(List.of(works));
        workList.stream().filter(e -> e.getId() == dw.getId()).findAny().ifPresent(workList::remove);
        workList.add(dw);
        works = workList.toArray(works);
        session.setAttribute(WebUtility.INSTANCE.sessionWorks, works);
    }

    private void removeDentalWorkFromList(HttpSession session, int id) {
        DentalWork[] works = (DentalWork[]) session.getAttribute(WebUtility.INSTANCE.sessionWorks);
        ArrayList<DentalWork> workList = new ArrayList<>(List.of(works));
        workList.stream().filter(dw -> dw.getId() == id).findAny().ifPresent(workList::remove);
        works = workList.toArray(works);
        session.setAttribute(WebUtility.INSTANCE.sessionWorks, works);
    }
}
