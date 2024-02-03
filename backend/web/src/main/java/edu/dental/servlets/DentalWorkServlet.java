package edu.dental.servlets;

import edu.dental.domain.records.WorkRecordBook;
import edu.dental.domain.records.WorkRecordBookException;
import edu.dental.dto.DentalWorkDto;
import edu.dental.entities.DentalWork;
import edu.dental.service.Repository;
import edu.dental.service.tools.JsonObjectParser;
import edu.dental.service.tools.RequestReader;
import edu.dental.service.tools.RestRequestReader;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;

@WebServlet({"/main/dental-work", "/main/dental-work/*"})
public class DentalWorkServlet extends HttpServlet {

    private static final String url = "/main/dental-work";

    public final String idParam = "id";
    public final String productParam = "product";
    public final String quantityParam = "quantity";
    public final String fieldParam = "field";
    public final String valueParam = "value";

    private Repository repository;
    private JsonObjectParser jsonObjectParser;
    private RestRequestReader restRequestReader;


    @Override
    public void init() throws ServletException {
        this.repository = Repository.getInstance();
        this.jsonObjectParser = JsonObjectParser.getInstance();
        this.restRequestReader = new RestRequestReader(url);
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
        } catch (WorkRecordBookException e) {
            response.sendError(500);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = (int) request.getAttribute(Repository.paramUser);
        String jsonNew = RequestReader.readJson(request);
        DentalWorkDto dto = jsonObjectParser.parseFromJson(jsonNew, DentalWorkDto.class);

        WorkRecordBook recordBook = repository.getRecordBook(userId);
        try {
            DentalWork dentalWork = dto.revert(userId);
            dentalWork = recordBook.addNewRecord(dentalWork);
            String jsonCreated = jsonObjectParser.parseToJson(new DentalWorkDto(dentalWork));
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().print(jsonCreated);
            response.getWriter().flush();
        } catch (WorkRecordBookException e) {
            response.sendError(400);
        }

    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int userId = (int) request.getAttribute(Repository.paramUser);
        HashMap<String, String> parameters = new RequestReader(request).getParameterMap();

        int id = restRequestReader.getId(request.getRequestURI());
        String field = parameters.get(fieldParam);
        String value = parameters.get(valueParam);
        String quantity = parameters.get(quantityParam);
        try {
            WorkRecordBook recordBook = repository.getRecordBook(userId);
            DentalWork dentalWork = recordBook.getById(id, true);
            if (field.equals(productParam)) {
                recordBook.addProductToRecord(dentalWork, value, Integer.parseInt(quantity));
            } else {
                recordBook.updateRecord(dentalWork, field, value);
            }
            String json = jsonObjectParser.parseToJson(new DentalWorkDto(dentalWork));
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
        } catch (WorkRecordBookException e) {
            response.sendError(400);
        }
    }
}
