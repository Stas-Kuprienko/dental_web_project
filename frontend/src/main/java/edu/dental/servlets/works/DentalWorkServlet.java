package edu.dental.servlets.works;

import edu.dental.WebAPI;
import edu.dental.beans.DentalWork;
import edu.dental.beans.ProductMap;
import edu.dental.service.JsonObjectParser;
import edu.dental.service.WebRepository;
import edu.dental.service.RequestSender;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

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
        int userId = (int) request.getSession().getAttribute(WebAPI.INSTANCE.sessionAttribute);
        int workId = request.getParameter(idParam) != null ? Integer.parseInt(request.getParameter(idParam)) :
                (int) request.getAttribute(idParam);
        DentalWork work = WebRepository.INSTANCE.getWorks(userId).stream().filter(dw -> dw.id() == workId).findAny().orElseThrow();
        request.setAttribute("work", work);
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
            int userId = (int) request.getSession().getAttribute(WebAPI.INSTANCE.sessionAttribute);

            String patient = request.getParameter(patientParam);
            String clinic = request.getParameter(clinicParam);
            String product = request.getParameter(productParam);
            int quantity = Integer.parseInt(request.getParameter(quantityParam));
            LocalDate complete = LocalDate.parse(request.getParameter(completeParam));

            int id = newDentalWork(userId, patient, clinic, product, quantity, complete);
            request.setAttribute(idParam, id);
            doGet(request, response);
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = (int) request.getSession().getAttribute(WebAPI.INSTANCE.sessionAttribute);
        int id = Integer.parseInt(request.getParameter(idParam));
        String field = request.getParameter(fieldParam);
        String value = request.getParameter(valueParam);
        RequestSender.QueryFormer queryFormer = new RequestSender.QueryFormer();
        queryFormer.add(idParam, id);
        queryFormer.add(fieldParam, field);
        queryFormer.add(valueParam, value);
        if (field.equals(productParam)) {
            if (!(value == null || value.isEmpty())) {
                int quantity = Integer.parseInt(request.getParameter(quantityParam));
                queryFormer.add(quantityParam, quantity);
            } else {
                response.sendError(400);
            }
        }
        String requestParam = queryFormer.form();
        editWork(userId, requestParam);
        request.setAttribute(idParam, id);
        doGet(request, response);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = (int) request.getSession().getAttribute(WebAPI.INSTANCE.sessionAttribute);
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

    private int newDentalWork(int userId, String patient, String clinic, String product, int quantity, LocalDate complete) throws IOException {
        String jwt = WebRepository.INSTANCE.getToken(userId);
        DentalWork dw;
        RequestSender.QueryFormer queryFormer = new RequestSender.QueryFormer();
        queryFormer.add(patientParam, patient);
        queryFormer.add(clinicParam, clinic);
        queryFormer.add(productParam, product);
        queryFormer.add(quantityParam, quantity);
        queryFormer.add(completeParam, complete);
        String requestParam = queryFormer.form();

        String json = WebAPI.INSTANCE.requestSender().sendHttpPostRequest(jwt, dentalWorkUrl, requestParam);

        dw = JsonObjectParser.parser.fromJson(json, DentalWork.class);
        WebRepository.INSTANCE.getWorks(userId).add(dw);
        return dw.id();
    }

    private void editWork(int userId, String requestParam) throws IOException {
        DentalWork dentalWork;

        String jwt = WebRepository.INSTANCE.getToken(userId);
        String json = WebAPI.INSTANCE.requestSender().sendHttpPutRequest(jwt, dentalWorkUrl, requestParam);

        dentalWork = JsonObjectParser.parser.fromJson(json, DentalWork.class);
        WebRepository.INSTANCE.updateDentalWorkList(userId, dentalWork);
    }

    private void deleteProductFromWork(int userId, int id, String product) throws IOException {
        String jwt = WebRepository.INSTANCE.getToken(userId);
        RequestSender.QueryFormer queryFormer = new RequestSender.QueryFormer();
        queryFormer.add(idParam, id);
        queryFormer.add(productParam, product);
        DentalWork dentalWork;

        String json = WebAPI.INSTANCE.requestSender().sendHttpDeleteRequest(jwt, dentalWorkUrl, queryFormer.form());

        dentalWork = JsonObjectParser.parser.fromJson(json, DentalWork.class);
        WebRepository.INSTANCE.updateDentalWorkList(userId, dentalWork);
    }


    private void deleteWorkRecord(int userId, int id) throws IOException {
        String jwt = WebRepository.INSTANCE.getToken(userId);
        RequestSender.QueryFormer queryFormer = new RequestSender.QueryFormer();
        queryFormer.add(idParam, id);

        WebAPI.INSTANCE.requestSender().sendHttpPutRequest(jwt, dentalWorkUrl, queryFormer.form());

        List<DentalWork> works = WebRepository.INSTANCE.getWorks(userId);
        works.remove(works.stream().filter(dw -> dw.id() == id).findAny().orElseThrow());
    }
}
