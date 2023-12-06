package edu.dental.web.servlets.main;

import edu.dental.domain.Action;
import edu.dental.web.Repository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/main/work-handle")
public class DentalWorkHandling extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getParameter("id") == null) {
            Repository.getInstance().setDtoAttributes(request, (String) request.getSession().getAttribute("user"));
            request.getRequestDispatcher("/main/work-list").forward(request, response);
        } else {
            String user = (String) request.getSession().getAttribute("user");
            int id = Integer.parseInt(request.getParameter("id"));
            String field = request.getParameter("field");
            String value = request.getParameter(field);
            try {
                if (field.equals("product")) {
                    int quantity = Integer.parseInt(request.getParameter("quantity"));
                    Action.addProductToWork(user, id, value, quantity);
                } else {
                    Action.editWork(user, id, field, value);
                }
            } catch (Action.ActionException e) {
                response.sendError(e.CODE);
            }
            request.getRequestDispatcher("/main/edit-work").forward(request, response);
        }
    }
}
