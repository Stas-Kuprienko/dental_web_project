package edu.dental.servlets.works;

import edu.dental.WebAPI;
import edu.dental.beans.DentalWork;
import edu.dental.beans.ProductMap;
import edu.dental.service.HttpRequestSender;
import edu.dental.service.WebRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
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
        int userId = (int) request.getSession().getAttribute(WebAPI.INSTANCE.sessionUser);
        setWorkToRequest(request, userId);
        ProductMap map = WebRepository.INSTANCE.getMap(userId);
        request.setAttribute("map", map.getKeys());
        request.getRequestDispatcher("/main/dental-work/page").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            editWork(request);
        } catch (NullPointerException e) {
            response.sendError(400);
        }
        doGet(request, response);
    }
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = (int) request.getSession().getAttribute(WebAPI.INSTANCE.sessionUser);
        int id = Integer.parseInt(request.getParameter(idParam));
        String product = request.getParameter(productParam);
        if (product != null) {
            deleteProductFromWork(userId, id, product);
            request.setAttribute(idParam, id);
            doGet(request, response);
        } else {
            deleteWorkRecord(userId, id);
            request.getRequestDispatcher("/main/work-list").forward(request, response);
        }
    }


    private void setWorkToRequest(HttpServletRequest request, int userId) throws IOException {
        int workId = request.getParameter(idParam) != null ? Integer.parseInt(request.getParameter(idParam)) :
                (int) request.getAttribute(idParam);
        DentalWork work;
        try {
            work = WebRepository.INSTANCE.getWorks(userId).stream().filter(dw -> dw.id() == workId).findAny().orElseThrow();
        } catch (NoSuchElementException e) {
            HttpRequestSender.QueryFormer queryFormer = new HttpRequestSender.QueryFormer();
            queryFormer.add(idParam, workId);
            String requestParam = queryFormer.form();

            String jwt = WebRepository.INSTANCE.getToken(userId);
            String json = WebAPI.INSTANCE.requestSender().sendHttpGetRequest(jwt, dentalWorkUrl + "?" + requestParam);
            work = WebAPI.INSTANCE.parseFromJson(json, DentalWork.class);
        }
        request.setAttribute("work", work);
    }

    private void newDentalWork(HttpServletRequest request) throws IOException {
        int userId = (int) request.getSession().getAttribute(WebAPI.INSTANCE.sessionUser);

        String patient = request.getParameter(patientParam);
        String clinic = request.getParameter(clinicParam);
        String product = request.getParameter(productParam);
        int quantity = Integer.parseInt(request.getParameter(quantityParam));
        LocalDate complete = LocalDate.parse(request.getParameter(completeParam));

        int id = createDentalWork(userId, patient, clinic, product, quantity, complete);
        request.setAttribute(idParam, id);
    }

    private int createDentalWork(int userId, String patient, String clinic, String product, int quantity, LocalDate complete) throws IOException {
        String jwt = WebRepository.INSTANCE.getToken(userId);
        DentalWork dw;
        HttpRequestSender.QueryFormer queryFormer = new HttpRequestSender.QueryFormer();
        queryFormer.add(patientParam, patient);
        queryFormer.add(clinicParam, clinic);
        queryFormer.add(productParam, product);
        queryFormer.add(quantityParam, quantity);
        queryFormer.add(completeParam, complete);
        String requestParam = queryFormer.form();

        String json = WebAPI.INSTANCE.requestSender().sendHttpPostRequest(jwt, dentalWorkUrl, requestParam);

        dw = WebAPI.INSTANCE.parseFromJson(json, DentalWork.class);
        WebRepository.INSTANCE.getWorks(userId).add(dw);
        return dw.id();
    }

    private void editWork(HttpServletRequest request) throws IOException, NullPointerException {
        int userId = (int) request.getSession().getAttribute(WebAPI.INSTANCE.sessionUser);
        int id = Integer.parseInt(request.getParameter(idParam));
        String field = request.getParameter(fieldParam);
        String value = request.getParameter(valueParam);
        String requestParam = buildRequestParameters(request, id, field, value);
        String jwt = WebRepository.INSTANCE.getToken(userId);
        String json = WebAPI.INSTANCE.requestSender().sendHttpPutRequest(jwt, dentalWorkUrl, requestParam);

        DentalWork dentalWork = WebAPI.INSTANCE.parseFromJson(json, DentalWork.class);
        WebRepository.INSTANCE.updateDentalWorkList(userId, dentalWork);
        request.setAttribute(idParam, id);
    }

    private String buildRequestParameters(HttpServletRequest request, int id, String field, String value) throws NullPointerException {
        HttpRequestSender.QueryFormer queryFormer = new HttpRequestSender.QueryFormer();
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

    private void deleteProductFromWork(int userId, int id, String product) throws IOException {
        String jwt = WebRepository.INSTANCE.getToken(userId);
        HttpRequestSender.QueryFormer queryFormer = new HttpRequestSender.QueryFormer();
        queryFormer.add(idParam, id);
        queryFormer.add(productParam, product);
        DentalWork dentalWork;

        String json = WebAPI.INSTANCE.requestSender().sendHttpDeleteRequest(jwt, dentalWorkUrl, queryFormer.form());

        dentalWork = WebAPI.INSTANCE.parseFromJson(json, DentalWork.class);
        WebRepository.INSTANCE.updateDentalWorkList(userId, dentalWork);
    }


    private void deleteWorkRecord(int userId, int id) throws IOException {
        String jwt = WebRepository.INSTANCE.getToken(userId);
        HttpRequestSender.QueryFormer queryFormer = new HttpRequestSender.QueryFormer();
        queryFormer.add(idParam, id);

        WebAPI.INSTANCE.requestSender().sendHttpDeleteRequest(jwt, dentalWorkUrl, queryFormer.form());

        WebRepository.INSTANCE.deleteDentalWork(userId, id);
    }
}
