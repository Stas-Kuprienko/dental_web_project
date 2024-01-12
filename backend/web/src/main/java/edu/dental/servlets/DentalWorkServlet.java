package edu.dental.servlets;

import edu.dental.domain.records.WorkRecordBook;
import edu.dental.domain.records.WorkRecordBookException;
import edu.dental.dto.DentalWorkDto;
import edu.dental.entities.DentalWork;
import edu.dental.service.Repository;
import edu.dental.service.tools.JsonObjectParser;
import edu.dental.service.tools.RequestReader;
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

    private Repository repository;
    private JsonObjectParser jsonObjectParser;

    @Override
    public void init() throws ServletException {
        this.repository = Repository.getInstance();
        this.jsonObjectParser = JsonObjectParser.getInstance();
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = (int) request.getAttribute(Repository.paramUser);
        int workId = Integer.parseInt(request.getParameter(idParam));
        DentalWorkDto work;
        WorkRecordBook recordBook = repository.getRecordBook(userId);
        try {
            work = new DentalWorkDto(recordBook.getById(workId, true));
            String json = jsonObjectParser.parseToJson(work);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().print(json);
            response.getWriter().flush();
        } catch (WorkRecordBookException e) {
            response.sendError(500);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = (int) request.getAttribute(Repository.paramUser);
        String patient = request.getParameter(patientParam);
        String clinic = request.getParameter(clinicParam);
        String product = request.getParameter(productParam);
        int quantity = Integer.parseInt(request.getParameter(quantityParam));
        String complete = request.getParameter(completeParam);

        DentalWorkDto dw;
        WorkRecordBook recordBook = repository.getRecordBook(userId);
        try {
            if (product != null && !product.isEmpty()) {
                dw = new DentalWorkDto(recordBook.createRecord(patient, clinic, product, quantity, LocalDate.parse(complete)));
            } else {
                dw = new DentalWorkDto(recordBook.createRecord(patient, clinic));
            }
            String json = jsonObjectParser.parseToJson(dw);
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
        int userId = (int) request.getAttribute(Repository.paramUser);
        HashMap<String, String> parameters = new RequestReader(request).getParameterMap();

        int id = Integer.parseInt(parameters.get(idParam));
        String field = parameters.get(fieldParam);
        String value = parameters.get(valueParam);
        String quantity = parameters.get(quantityParam);
        DentalWorkDto dw;
        try {
            if (field != null && field.equals(productParam)) {
                dw = new DentalWorkDto(addProductToWork(userId, id, value, Integer.parseInt(quantity)));
            } else {
                dw = new DentalWorkDto(editDentalWork(userId, id, field, value));
            }
            String json = jsonObjectParser.parseToJson(dw);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().print(json);
            response.getWriter().flush();
        } catch (WorkRecordBookException e) {
            response.sendError(500);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int userId = (int) request.getAttribute(Repository.paramUser);
        HashMap<String, String> parameters = new RequestReader(request).getParameterMap();

        int id = Integer.parseInt(parameters.get(idParam));
        String product = parameters.get(productParam);
        try {
            if (product != null) {
                WorkRecordBook recordBook = repository.getRecordBook(userId);
                recordBook.removeProduct(id, product);
                DentalWorkDto dw = new DentalWorkDto(recordBook.getById(id));
                //TODO add search in db
                String json = jsonObjectParser.parseToJson(dw);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().print(json);
                response.getWriter().flush();
            } else {
                repository.getRecordBook(userId).deleteRecord(id);
            }
        } catch (WorkRecordBookException e) {
            response.sendError(400);
        }
    }


    private DentalWork editDentalWork(int userId, int id, String field, String value) throws WorkRecordBookException {
        WorkRecordBook recordBook = repository.getRecordBook(userId);
        DentalWork dw = recordBook.getById(id, true);
        return recordBook.editRecord(dw, field, value);
    }

    private DentalWork addProductToWork(int userId, int id, String product, int quantity) throws WorkRecordBookException {
        WorkRecordBook recordBook = repository.getRecordBook(userId);
        recordBook.addProductToRecord(recordBook.getById(id), product, quantity);
        DentalWork dw = recordBook.getById(id, true);
        return recordBook.addProductToRecord(dw, product, quantity);
    }
}
