package edu.dental.servlets.works;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/main/work-list/sorting")
public class WorkSorting extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String user = (String) request.getSession().getAttribute("user");
        workSorting(user);
        request.getRequestDispatcher("/main/work-list").forward(request, response);
    }

    private void workSorting(String login) {

    }
}
