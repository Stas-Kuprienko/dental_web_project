package edu.dental.web.servlets.main;

import edu.dental.web.Repository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/main/work-list")
public class WorkRecordHandling extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getParameter("id") == null) {
            Repository.getInstance().setDtoAttributes(request, (String) request.getSession().getAttribute("user"));
            request.getRequestDispatcher("/main/work-list-view").forward(request, response);
        } else {
            int id = Integer.parseInt(request.getParameter("id"));
        }
    }
}
