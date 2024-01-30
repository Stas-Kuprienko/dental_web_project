package edu.dental.servlets.products;

import edu.dental.APIResponseException;
import edu.dental.beans.ProductMap;
import edu.dental.service.WebUtility;
import edu.http_utils.RestRequestReader;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet({"/main/product-map", "/main/product-map/*"})
public class ProductServlet extends HttpServlet {

    public final String productMapUrl = "main/product-map";
    public final String titleParam = "title";
    public final String idParam = "id";
    public final String priceParam = "price";

    private RestRequestReader restRequestReader;


    @Override
    public void init() throws ServletException {
        this.restRequestReader = new RestRequestReader(productMapUrl);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
            } else {
                response.sendError(400);
            }
        } else {
            try {
                String title = request.getParameter(titleParam);
                int price = Integer.parseInt(request.getParameter(priceParam));

                newProduct(request.getSession(), title, price);
                doGet(request, response);
            } catch (APIResponseException e) {
                response.sendError(e.CODE, e.MESSAGE);
            }
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        int id = restRequestReader.getId(request.getRequestURI());
        if (id > 0) {

            try {
                String title = request.getParameter(titleParam);
                int price = Integer.parseInt(request.getParameter(priceParam));
                editProduct(request.getSession(), id, title, price);
                request.getRequestDispatcher("/main/product-map/page").forward(request, response);
            } catch (APIResponseException e) {
                response.sendError(e.CODE, e.MESSAGE);
            }
        } else {
            response.sendError(400);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            int id = restRequestReader.getId(request.getRequestURI());
            String title = request.getParameter(titleParam);
            deleteProduct(request.getSession(), id, title);
            doGet(request, response);
        } catch (APIResponseException e) {
            response.sendError(e.CODE, e.MESSAGE);
        }
    }


    private void newProduct(HttpSession session, String title, int price) throws IOException, APIResponseException {
        String jwt = (String) session.getAttribute(WebUtility.INSTANCE.sessionToken);
        ProductMap.Item item;

        WebUtility.QueryFormer queryFormer = new WebUtility.QueryFormer();
        queryFormer.add(titleParam, title);
        queryFormer.add(priceParam, price);
        String requestParam = queryFormer.form();

        String json = WebUtility.INSTANCE.requestSender().sendHttpPostRequest(jwt, productMapUrl, requestParam);
        item = WebUtility.INSTANCE.parseFromJson(json, ProductMap.Item.class);
        ProductMap map = (ProductMap) session.getAttribute(WebUtility.INSTANCE.sessionMap);
        map.add(item);
    }

    private void editProduct(HttpSession session, int id, String title, int price) throws IOException, APIResponseException {
        String jwt = (String) session.getAttribute(WebUtility.INSTANCE.sessionToken);
        WebUtility.QueryFormer queryFormer = new WebUtility.QueryFormer();
        queryFormer.add(idParam, id);
        queryFormer.add(titleParam, title);
        queryFormer.add(priceParam, price);
        String requestParam = queryFormer.form();
        WebUtility.INSTANCE.requestSender().sendHttpPutRequest(jwt, productMapUrl, requestParam);
        ProductMap map = (ProductMap) session.getAttribute(WebUtility.INSTANCE.sessionMap);
        map.update(id, price);
    }

    private void deleteProduct(HttpSession session, int id, String title) throws IOException, APIResponseException {
        String jwt = (String) session.getAttribute(WebUtility.INSTANCE.sessionToken);
        WebUtility.QueryFormer queryFormer = new WebUtility.QueryFormer();
        queryFormer.add(titleParam, title);
        WebUtility.INSTANCE.requestSender().sendHttpDeleteRequest(jwt, productMapUrl + '/' + id, queryFormer.form());
        ProductMap map = (ProductMap) session.getAttribute(WebUtility.INSTANCE.sessionMap);
        map.remove(id);
    }
}