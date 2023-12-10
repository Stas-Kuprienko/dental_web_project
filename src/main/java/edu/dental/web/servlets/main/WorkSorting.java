package edu.dental.web.servlets.main;

import edu.dental.domain.Action;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/main/sorting")
public class WorkSorting extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String user = (String) request.getSession().getAttribute("user");
        try {
            Action.workSorting(user);
        } catch (Action.ActionException e) {
            response.sendError(e.CODE);
            return;
        }
        request.getRequestDispatcher("/main/work-handle").forward(request, response);
    }
}
