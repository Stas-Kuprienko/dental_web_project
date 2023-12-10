package edu.dental.web.servlets.main;

import edu.dental.domain.entities.dto.DentalWorkDTO;
import edu.dental.domain.records.WorkRecordBookException;
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
        String user = (String) request.getSession().getAttribute("user");
        if (request.getParameter("id") == null) {
            Repository.getInstance().setDtoAttributes(request, user);
            request.getRequestDispatcher("/main/work-list").forward(request, response);
        } else {
            int id = Integer.parseInt(request.getParameter("id"));
            DentalWorkDTO dto;
            try {
                dto = new DentalWorkDTO(Repository.getInstance().getRecordBook(user).getByID(id));
            } catch (WorkRecordBookException e) {
                request.getRequestDispatcher("/error?").forward(request, response);
                return;
            }
            request.setAttribute("work", dto);
            request.getRequestDispatcher("/main/view-work").forward(request, response);
        }
    }
}
