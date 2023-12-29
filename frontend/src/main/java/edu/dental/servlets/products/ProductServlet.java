package edu.dental.servlets.products;

//import edu.dental.database.DatabaseException;
//import edu.dental.database.DatabaseService;
//import edu.dental.web.WebRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/main/product-map")
public class ProductServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/main/product-map/page").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String method = request.getParameter("method");
        if (method != null && method.equals("put")) {
            doPut(request, response);
        } else if (method != null && method.equals("delete")) {
            doDelete(request, response);
        }else {
            String title = request.getParameter("title");
            int price = Integer.parseInt(request.getParameter("price"));
            String user = (String) request.getSession().getAttribute("user");
//            try {
//                newProduct(user, title, price);
//                doGet(request, response);
//            } catch (DatabaseException e) {
//                response.sendError(500);
//            }
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        int id = Integer.parseInt(request.getParameter("id"));
        String title = request.getParameter("title");
        int price = Integer.parseInt(request.getParameter("price"));
        String user = (String) request.getSession().getAttribute("user");
//        try {
//            updateProduct(user, id, title, price);
//            doGet(request, response);
//        } catch (DatabaseException e) {
//            response.sendError(500);
//        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        int id = Integer.parseInt(request.getParameter("id"));
        String title = request.getParameter("title");
        String user = (String) request.getSession().getAttribute("user");
//        try {
//            deleteProduct(user, id, title);
//            doGet(request, response);
//        } catch (DatabaseException e) {
//            response.sendError(500);
//        }
    }


//    private void newProduct(String user, String title, int price) throws DatabaseException {
//        WebRepository.Account account = WebRepository.getInstance().get(user);
//        DatabaseService database = DatabaseService.getInstance();
//        int id = database.getProductMapDAO(account.user()).put(title, price);
//        account.recordBook().getMap().put(title, price, id);
//    }
//
//    private void updateProduct(String user, int id, String title, int price) throws DatabaseException {
//        WebRepository.Account account = WebRepository.getInstance().get(user);
//        DatabaseService database = DatabaseService.getInstance();
//        database.getProductMapDAO(account.user()).edit(id, price);
//        account.recordBook().getMap().put(title, price);
//    }
//
//    private void deleteProduct(String user, int id, String title) throws DatabaseException {
//        WebRepository.Account account = WebRepository.getInstance().get(user);
//        DatabaseService database = DatabaseService.getInstance();
//        database.getProductMapDAO(account.user()).delete(id);
//        account.recordBook().getMap().remove(title);
//    }
}
