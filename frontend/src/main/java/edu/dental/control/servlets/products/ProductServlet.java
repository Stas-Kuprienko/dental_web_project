package edu.dental.control.servlets.products;

import edu.dental.APIResponseException;
import edu.dental.control.Administrator;
import edu.dental.control.ProductMapService;
import edu.http_utils.RestRequestReader;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet({"/main/product-map", "/main/product-map/*"})
public class ProductServlet extends HttpServlet {

    private static final String url = "main/product-map";
    private static final String titleParam = "title";
    private static final String priceParam = "price";
    private static final String productMapPageURL = "/main/product-map/page";

    private ProductMapService productMapService;
    private RestRequestReader restRequestReader;


    @Override
    public void init() throws ServletException {
        this.productMapService = Administrator.getInstance().getProductMapService();
        this.restRequestReader = new RestRequestReader(url);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher(productMapPageURL).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getParameter("method") != null) {
            chooseMethod(request, response);
        } else {
            try {
                String title = request.getParameter(titleParam);
                int price = Integer.parseInt(request.getParameter(priceParam));

                productMapService.createProductItem(request.getSession(), title, price);
                request.getRequestDispatcher(productMapPageURL).forward(request, response);

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

                productMapService.updateProductItem(request.getSession(), id, title, price);
                request.getRequestDispatcher(productMapPageURL).forward(request, response);

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
            productMapService.deleteProductItem(request.getSession(), id, title);
            request.getRequestDispatcher(productMapPageURL).forward(request, response);
        } catch (APIResponseException e) {
            response.sendError(e.CODE, e.MESSAGE);
        }
    }


    private void chooseMethod(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String method = request.getParameter("method");
        if (method.equals("put")) {
            doPut(request, response);
        } else if (method.equals("delete")) {
            doDelete(request, response);
        } else {
            response.sendError(405);
        }
    }
}