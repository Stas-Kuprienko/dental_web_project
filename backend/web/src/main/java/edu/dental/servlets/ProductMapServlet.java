package edu.dental.servlets;

import edu.dental.domain.records.WorkRecordBook;
import edu.dental.domain.records.WorkRecordBookException;
import edu.dental.dto.ProductMapDto;
import edu.dental.service.tools.JsonObjectParser;
import edu.dental.service.Repository;
import edu.dental.service.tools.RequestReader;
import edu.dental.service.tools.RestRequestReader;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;

@WebServlet({"/main/product-map", "/main/product-map/*"})
public class ProductMapServlet extends HttpServlet {

    private static final String url = "/main/product-map";
    private static final String idParam = "id";
    private static final String titleParam = "title";
    private static final String priceParam = "price";

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
        ProductMapDto map = repository.getProductMapDto(userId);
        String json = jsonObjectParser.parseToJson(map.getItems());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().print(json);
        response.getWriter().flush();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = (int) request.getAttribute(Repository.paramUser);
        String title = request.getParameter(titleParam);
        int price = Integer.parseInt(request.getParameter(priceParam));

        ProductMapDto.Item item;
        WorkRecordBook recordBook = repository.getRecordBook(userId);
        try {
            item = new ProductMapDto.Item(recordBook.addProductItem(title, price));
            String json = jsonObjectParser.parseToJson(item);
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

        int id = restRequestReader.getId(request.getRequestURI());
        String title = parameters.get(titleParam);
        int price = Integer.parseInt(parameters.get(priceParam));

        WorkRecordBook recordBook = repository.getRecordBook(userId);
        try {
            recordBook.updateProductItem(title, price);
            response.getWriter().flush();
        } catch (WorkRecordBookException e) {
            response.sendError(500);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int userId = (int) request.getAttribute(Repository.paramUser);
        HashMap<String, String> parameters = new RequestReader(request).getParameterMap();

        String title = parameters.get(titleParam);
        WorkRecordBook recordBook = repository.getRecordBook(userId);
        try {
            recordBook.deleteProductItem(title);
            response.getWriter().flush();
        } catch (WorkRecordBookException e) {
            response.sendError(500);
        }
    }
}
