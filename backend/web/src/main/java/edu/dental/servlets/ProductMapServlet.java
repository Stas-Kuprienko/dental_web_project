package edu.dental.servlets;

import edu.dental.WebAPI;
import edu.dental.domain.records.WorkRecordBook;
import edu.dental.domain.records.WorkRecordBookException;
import edu.dental.dto.ProductMap;
import edu.dental.service.JsonObjectParser;
import edu.dental.service.Repository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/main/product-map")
public class ProductMapServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = (int) request.getAttribute(WebAPI.INSTANCE.paramUser);
        ProductMap map = Repository.getInstance().getProductMapDto(userId);
        String json = JsonObjectParser.getInstance().parseToJson(map.getItems());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().print(json);
        response.getWriter().flush();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = (int) request.getAttribute(WebAPI.INSTANCE.paramUser);
        String title = request.getParameter("title");
        int price = Integer.parseInt(request.getParameter("price"));

        ProductMap.Item item;
        WorkRecordBook recordBook = Repository.getInstance().getRecordBook(userId);
        try {
            item = new ProductMap.Item(recordBook.addProductItem(title, price));
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
}
