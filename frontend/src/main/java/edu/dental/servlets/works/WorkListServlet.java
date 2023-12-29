package edu.dental.servlets.works;

import edu.dental.WebAPI;
import edu.dental.beans.DentalWork;
import edu.dental.service.WebRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/main/work-list")
public class WorkListServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = (int) request.getSession().getAttribute(WebAPI.INSTANCE.sessionAttribute);

        DentalWork[] works = new DentalWork[]{};
        works = WebRepository.INSTANCE.getWorks(userId).toArray(works);
        request.setAttribute("works", works);

        String[] map = WebRepository.INSTANCE.getMap(userId).getKeys();
        request.setAttribute("map", map);

        request.getRequestDispatcher("/main/work-list/page").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
