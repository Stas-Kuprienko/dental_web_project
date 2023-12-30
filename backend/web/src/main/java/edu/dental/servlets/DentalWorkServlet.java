package edu.dental.servlets;

import edu.dental.WebAPI;
import edu.dental.domain.records.WorkRecordBook;
import edu.dental.domain.records.WorkRecordBookException;
import edu.dental.dto.DentalWork;
import edu.dental.service.JsonObjectParser;
import edu.dental.service.Repository;
import edu.dental.service.RequestReader;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;

@WebServlet("/main/dental-work")
public class DentalWorkServlet extends HttpServlet {

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
        response.sendError(405);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = (int) request.getAttribute(WebAPI.INSTANCE.paramUser);
        String patient = request.getParameter(patientParam);
        String clinic = request.getParameter(clinicParam);
        String product = request.getParameter(productParam);
        int quantity = Integer.parseInt(request.getParameter(quantityParam));
        String complete = request.getParameter(completeParam);

        DentalWork dw;
        WorkRecordBook recordBook = Repository.getInstance().getRecordBook(userId);
        try {
            if (product != null && !product.isEmpty()) {
                dw = new DentalWork(recordBook.createRecord(patient, clinic, product, quantity, LocalDate.parse(complete)));
            } else {
                dw = new DentalWork(recordBook.createRecord(patient, clinic));
            }
            String json = JsonObjectParser.getInstance().parseToJson(dw);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().print(json);
            response.getWriter().flush();
        } catch (WorkRecordBookException e) {
            response.sendError(400);
        }

    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int userId = (int) request.getAttribute(WebAPI.INSTANCE.paramUser);
        HashMap<String, String> parameters = new RequestReader(request).getParameterMap();

        int id = Integer.parseInt(parameters.get(idParam));
        String field = parameters.get(fieldParam);
        String value = parameters.get(valueParam);
        String quantity = parameters.get(quantityParam);
        try {
            if (field != null && field.equals(productParam)) {
                addProductToWork(userId, id, value, Integer.parseInt(quantity));
            } else {
                editDentalWork(userId, id, field, value);
            }
            WorkRecordBook recordBook = Repository.getInstance().getRecordBook(userId);
            DentalWork dw = new DentalWork(recordBook.getByID(id));
            String json = JsonObjectParser.getInstance().parseToJson(dw);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().print(json);
            response.getWriter().flush();
        } catch (WorkRecordBookException e) {
            response.sendError(400);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int userId = (int) request.getAttribute(WebAPI.INSTANCE.paramUser);
        HashMap<String, String> parameters = new RequestReader(request).getParameterMap();

        int id = Integer.parseInt(parameters.get(idParam));
        String product = parameters.get(productParam);
        try {
            if (product != null) {
                WorkRecordBook recordBook = Repository.getInstance().getRecordBook(userId);
                recordBook.removeProduct(id, product);
                DentalWork dw = new DentalWork(recordBook.getByID(id));
                String json = JsonObjectParser.getInstance().parseToJson(dw);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().print(json);
                response.getWriter().flush();
            } else {
                Repository.getInstance().getRecordBook(userId).deleteRecord(id);
            }
        } catch (WorkRecordBookException e) {
            response.sendError(400);
        }
    }


    private void editDentalWork(int userId, int id, String field, String value) throws WorkRecordBookException {
        WorkRecordBook recordBook = Repository.getInstance().getRecordBook(userId);
        recordBook.editRecord(id, field, value);
    }

    private void addProductToWork(int userId, int id, String product, int quantity) throws WorkRecordBookException {
        WorkRecordBook recordBook = Repository.getInstance().getRecordBook(userId);
        recordBook.addProductToRecord(recordBook.getByID(id), product, quantity);
    }
}
