package edu.dental.web.servlets.main;

import edu.dental.domain.Action;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/main/delete-element")
public class DeleteElement extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String user = (String) request.getSession().getAttribute("user");
        int id = Integer.parseInt(request.getParameter("id"));
        String product = request.getParameter("product");
        if (product != null) {
            try {
                Action.deleteProductFromWork(user, id, product);
            } catch (Action.ActionException e) {
                response.sendError(e.CODE);
            }
        } else {
            try {
                Action.deleteWorkRecord(user, id);
            } catch (Action.ActionException e) {
                response.sendError(e.CODE);
            }
        }
        request.getRequestDispatcher("/main/work-handle").forward(request, response);
    }
}
