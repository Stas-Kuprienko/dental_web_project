package edu.dental.servlets.products;

import edu.dental.WebAPI;
import edu.dental.beans.ProductMap;
import edu.dental.service.JsonObjectParser;
import edu.dental.service.RequestSender;
import edu.dental.service.WebRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/main/product-map")
public class ProductServlet extends HttpServlet {

    public final String productMapUrl = "main/product-map";
    public final String titleParam = "title";
    public final String priceParam = "price";


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = (int) request.getSession().getAttribute(WebAPI.INSTANCE.sessionAttribute);
        ProductMap.Item[] items = WebRepository.INSTANCE.getMap(userId).getItems();
        request.setAttribute("map", items);
        request.getRequestDispatcher("/main/product-map/page").forward(request, response);
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
            String title = request.getParameter(titleParam);
            int price = Integer.parseInt(request.getParameter(priceParam));

            newProduct(userId, title, price);
            doGet(request, response);
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        int userId = (int) request.getSession().getAttribute(WebAPI.INSTANCE.sessionAttribute);
        String title = request.getParameter(titleParam);
        int price = Integer.parseInt(request.getParameter(priceParam));
        editProduct(userId, title, price);
        doGet(request, response);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        int userId = (int) request.getSession().getAttribute(WebAPI.INSTANCE.sessionAttribute);
        String title = request.getParameter(titleParam);
        deleteProduct(userId, title);
        doGet(request, response);
    }


    private void newProduct(int userId, String title, int price) throws IOException {
        String jwt = WebRepository.INSTANCE.getToken(userId);
        ProductMap.Item item;

        RequestSender.QueryFormer queryFormer = new RequestSender.QueryFormer();
        queryFormer.add(titleParam, title);
        queryFormer.add(priceParam, price);
        String requestParam = queryFormer.form();

        String json = WebAPI.INSTANCE.requestSender().sendHttpPostRequest(jwt, productMapUrl, requestParam);
        item = JsonObjectParser.parser.fromJson(json, ProductMap.Item.class);
        WebRepository.INSTANCE.getMap(userId).add(item);
    }

    private void editProduct(int userId, String title, int price) throws IOException {
        String jwt = WebRepository.INSTANCE.getToken(userId);
        RequestSender.QueryFormer queryFormer = new RequestSender.QueryFormer();
        queryFormer.add(titleParam, title);
        queryFormer.add(priceParam, price);
        String requestParam = queryFormer.form();
        WebAPI.INSTANCE.requestSender().sendHttpPutRequest(jwt, productMapUrl, requestParam);
        WebRepository.INSTANCE.getMap(userId).update(title, price);
    }

    private void deleteProduct(int userId, String title) throws IOException {
        String jwt = WebRepository.INSTANCE.getToken(userId);
        RequestSender.QueryFormer queryFormer = new RequestSender.QueryFormer();
        queryFormer.add(titleParam, title);
        String requestParam = queryFormer.form();
        WebAPI.INSTANCE.requestSender().sendHttpDeleteRequest(jwt, productMapUrl, requestParam);
        WebRepository.INSTANCE.getMap(userId).remove(title);
    }
}
