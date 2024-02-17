package edu.dental.servlets.dental_works;

import edu.dental.database.DatabaseException;
import edu.dental.domain.records.WorkRecordBook;
import edu.dental.domain.records.WorkRecordException;
import edu.dental.dto.DentalWorkDto;
import edu.dental.entities.DentalWork;
import edu.dental.service.Repository;
import edu.dental.service.JsonObjectParser;
import stas.http_tools.HttpRequestReader;
import stas.http_tools.RestRequestIDReader;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;

@WebServlet({"/main/dental-work", "/main/dental-work/*"})
public class DentalWorkServlet extends HttpServlet {

    private static final String url = "/main/dental-work";
    private static final String patientParam = "patient";
    private static final String clinicParam = "clinic";
    private static final String completeParam = "complete";
    private static final String productParam = "product";
    private static final String quantityParam = "quantity";
    private static final String fieldParam = "field";
    private static final String valueParam = "value";

    private Repository repository;
    private JsonObjectParser jsonObjectParser;
    private RestRequestIDReader restRequestReader;


    @Override
    public void init() throws ServletException {
        this.repository = Repository.getInstance();
        this.jsonObjectParser = JsonObjectParser.getInstance();
        this.restRequestReader = new RestRequestIDReader(url);
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = (int) request.getAttribute(Repository.paramUser);
        int id = restRequestReader.getId(request.getRequestURI());
        DentalWorkDto dto;
        WorkRecordBook recordBook = repository.getRecordBook(userId);
        try {
            DentalWork dw = recordBook.getById(id, true);
            if (dw == null) {
                response.sendError(404, "such record is not found");
            } else {
                dto = new DentalWorkDto(dw);
                String json = jsonObjectParser.parseToJson(dto);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().print(json);
                response.getWriter().flush();
            }
        } catch (DatabaseException e) {
            response.sendError(500);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = (int) request.getAttribute(Repository.paramUser);
        String patient = request.getParameter(patientParam);
        String clinic = request.getParameter(clinicParam);
        String complete = request.getParameter(completeParam);
        String productItem = request.getParameter(productParam);
        String quantity = request.getParameter(quantityParam);

        WorkRecordBook recordBook = repository.getRecordBook(userId);
        try {
            DentalWork dentalWork;
            if (productItem == null || productItem.isEmpty()) {
                dentalWork = recordBook.addNewRecord(patient, clinic);
            } else {
                LocalDate completeDate = complete == null ? null : LocalDate.parse(complete);
                dentalWork = recordBook.addNewRecord(patient, clinic, productItem, Integer.parseInt(quantity), completeDate);
            }
            String jsonCreated = jsonObjectParser.parseToJson(new DentalWorkDto(dentalWork));
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().print(jsonCreated);
            response.getWriter().flush();
        } catch (DatabaseException e) {
            response.sendError(500);
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int userId = (int) request.getAttribute(Repository.paramUser);
        HashMap<String, String> parameters = new HttpRequestReader(request).getParameterMap();

        int id = restRequestReader.getId(request.getRequestURI());
        String field = parameters.get(fieldParam);
        String value = parameters.get(valueParam);
        String quantity = parameters.get(quantityParam);
        try {
            WorkRecordBook recordBook = repository.getRecordBook(userId);
            DentalWork dentalWork = recordBook.getById(id, true);
            if (value != null && !value.isEmpty()) {
                if (field.equals(productParam)) {
                    recordBook.addProductToRecord(dentalWork, value, Integer.parseInt(quantity));
                } else {
                    recordBook.updateRecord(dentalWork, field, value);
                }
            }
            String json = jsonObjectParser.parseToJson(new DentalWorkDto(dentalWork));
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().print(json);
            response.getWriter().flush();
        } catch (WorkRecordException | DatabaseException e) {
            response.sendError(500);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int userId = (int) request.getAttribute(Repository.paramUser);
        HashMap<String, String> parameters = new HttpRequestReader(request).getParameterMap();

        int id = restRequestReader.getId(request.getRequestURI());
        String product = parameters.get(productParam);
        try {
            if (product != null) {
                WorkRecordBook recordBook = repository.getRecordBook(userId);
                DentalWork dw = recordBook.getById(id, true);
                recordBook.removeProduct(dw, product);
                String json = jsonObjectParser.parseToJson(new DentalWorkDto(dw));
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().print(json);
                response.getWriter().flush();
            } else {
                repository.getRecordBook(userId).deleteRecord(id);
            }
        } catch (WorkRecordException e) {
            response.sendError(400);
        } catch (DatabaseException e) {
            response.sendError(500);
        }
    }
}
