package edu.dental.servlets;

import edu.dental.WebAPI;
import edu.dental.domain.records.WorkRecordBook;
import edu.dental.domain.records.WorkRecordBookException;
import edu.dental.domain.reports.ReportService;
import edu.dental.domain.reports.ReportServiceException;
import edu.dental.dto.DentalWorkDto;
import edu.dental.service.tools.JsonObjectParser;
import edu.dental.service.Repository;
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
    private ReportService reportService;
    private JsonObjectParser jsonObjectParser;

    @Override
    public void init() throws ServletException {
        this.repository = Repository.getInstance();
        this.reportService = ReportService.getInstance();
        this.jsonObjectParser = JsonObjectParser.getInstance();
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = (int) request.getAttribute(WebAPI.INSTANCE.paramUser);
        int workId = Integer.parseInt(request.getParameter(idParam));
        DentalWorkDto work;
        try {
            work = new DentalWorkDto(repository.getRecordBook(userId).getByID(workId));
        } catch (WorkRecordBookException ignored) {
            try {
                work = new DentalWorkDto(reportService.getByIDFromDatabase(userId, workId));
            } catch (ReportServiceException ex) {
                response.sendError(400);
                return;
            }
        }
        String json = jsonObjectParser.parseToJson(work);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().print(json);
        response.getWriter().flush();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = (int) request.getAttribute(WebAPI.INSTANCE.paramUser);
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
            WorkRecordBook recordBook = repository.getRecordBook(userId);
            DentalWorkDto dw = new DentalWorkDto(recordBook.getByID(id));
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
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int userId = (int) request.getAttribute(WebAPI.INSTANCE.paramUser);
        HashMap<String, String> parameters = new RequestReader(request).getParameterMap();

        int id = Integer.parseInt(parameters.get(idParam));
        String product = parameters.get(productParam);
        try {
            if (product != null) {
                WorkRecordBook recordBook = repository.getRecordBook(userId);
                recordBook.removeProduct(id, product);
                DentalWorkDto dw = new DentalWorkDto(recordBook.getByID(id));
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


    private void editDentalWork(int userId, int id, String field, String value) throws WorkRecordBookException {
        WorkRecordBook recordBook = repository.getRecordBook(userId);
        recordBook.editRecord(id, field, value);
    }

    private void addProductToWork(int userId, int id, String product, int quantity) throws WorkRecordBookException {
        WorkRecordBook recordBook = repository.getRecordBook(userId);
        recordBook.addProductToRecord(recordBook.getByID(id), product, quantity);
    }
}
