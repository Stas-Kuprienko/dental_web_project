package edu.dental.servlets;

import edu.dental.WebAPI;
import edu.dental.domain.records.WorkRecordBook;
import edu.dental.domain.records.WorkRecordBookException;
import edu.dental.dto.ProductMapDto;
import edu.dental.service.tools.JsonObjectParser;
import edu.dental.service.Repository;
import edu.dental.service.tools.RequestReader;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;

@WebServlet("/main/product-map")
public class ProductMapServlet extends HttpServlet {

    public final String titleParam = "title";
    public final String priceParam = "price";


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = (int) request.getAttribute(WebAPI.INSTANCE.paramUser);
        ProductMapDto map = Repository.getInstance().getProductMapDto(userId);
        String json = JsonObjectParser.getInstance().parseToJson(map.getItems());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().print(json);
        response.getWriter().flush();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = (int) request.getAttribute(WebAPI.INSTANCE.paramUser);
        String title = request.getParameter(titleParam);
        int price = Integer.parseInt(request.getParameter(priceParam));

        ProductMapDto.Item item;
        WorkRecordBook recordBook = Repository.getInstance().getRecordBook(userId);
        try {
            item = new ProductMapDto.Item(recordBook.addProductItem(title, price));
            String json = JsonObjectParser.getInstance().parseToJson(item);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().print(json);
            response.getWriter().flush();
        } catch (WorkRecordBookException e) {
            //TODO
            response.sendError(400);
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int userId = (int) request.getAttribute(WebAPI.INSTANCE.paramUser);
        HashMap<String, String> parameters = new RequestReader(request).getParameterMap();

        String title = parameters.get(titleParam);
        int price = Integer.parseInt(parameters.get(priceParam));
        WorkRecordBook recordBook = Repository.getInstance().getRecordBook(userId);
        try {
            recordBook.editProductItem(title, price);
            response.getWriter().flush();
        } catch (WorkRecordBookException e) {
            //TODO
            response.sendError(500);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int userId = (int) request.getAttribute(WebAPI.INSTANCE.paramUser);
        HashMap<String, String> parameters = new RequestReader(request).getParameterMap();

        String title = parameters.get(titleParam);
        WorkRecordBook recordBook = Repository.getInstance().getRecordBook(userId);
        try {
            recordBook.deleteProductItem(title);
            response.getWriter().flush();
        } catch (WorkRecordBookException e) {
            //TODO
            response.sendError(500);
        }
    }


}
