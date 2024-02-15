package edu.dental.servlets.products;

import edu.dental.HttpWebException;
import edu.dental.control.Administrator;
import edu.dental.control.ProductMapService;
import stas.http_tools.RestRequestIDReader;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.eclipse.tags.shaded.org.apache.bcel.verifier.exc.InvalidMethodException;

import java.io.IOException;

@WebServlet({"/main/product-map", "/main/product-map/*"})
public class ProductServlet extends HttpServlet {

    private static final String url = "main/product-map";
    private static final String titleParam = "title";
    private static final String priceParam = "price";
    private static final String productMapPageURL = "/main/product-map/page";

    private ProductMapService productMapService;
    private RestRequestIDReader restRequestReader;


    @Override
    public void init() throws ServletException {
        this.productMapService = Administrator.getInstance().getProductMapService();
        this.restRequestReader = new RestRequestIDReader(url);
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
                if (title != null && !title.isEmpty()) {
                    int price = Integer.parseInt(request.getParameter(priceParam));

                    productMapService.createProductItem(request.getSession(), title, price);
                }
                request.getRequestDispatcher(productMapPageURL).forward(request, response);
            } catch (HttpWebException e) {
                e.errorRedirect(request, response);
            }
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        int id = restRequestReader.getId(request.getRequestURI());
        if (id > 0) {
            try {
                String title = request.getParameter(titleParam);
                if (title != null && !title.isEmpty()) {
                    int price = Integer.parseInt(request.getParameter(priceParam));

                    productMapService.updateProductItem(request.getSession(), id, title, price);
                }
                request.getRequestDispatcher(productMapPageURL).forward(request, response);
            } catch (HttpWebException e) {
                e.errorRedirect(request, response);
            }
        } else {
            ServletException e = new ServletException(request.getRequestURI());
            new HttpWebException(HttpWebException.ERROR.BAD_REQUEST, e.getStackTrace()).errorRedirect(request, response);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            int id = restRequestReader.getId(request.getRequestURI());
            String title = request.getParameter(titleParam);
            productMapService.deleteProductItem(request.getSession(), id, title);
            request.getRequestDispatcher(productMapPageURL).forward(request, response);
        } catch (HttpWebException e) {
            e.errorRedirect(request, response);
        }
    }


    private void chooseMethod(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String method = request.getParameter("method");
        if (method.equals("put")) {
            doPut(request, response);
        } else if (method.equals("delete")) {
            doDelete(request, response);
        } else {
            InvalidMethodException e = new InvalidMethodException(method);
            new HttpWebException(HttpWebException.ERROR.NOT_ALLOWED, e.getStackTrace()).errorRedirect(request, response);
        }
    }
}