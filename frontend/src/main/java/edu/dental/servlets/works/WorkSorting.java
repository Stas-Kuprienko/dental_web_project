package edu.dental.servlets.works;

import edu.dental.WebAPI;
import edu.dental.service.WebRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/main/work-list/sorting")
public class WorkSorting extends HttpServlet {

    public final String sortUrl = "/main/sort?month=";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = (int) request.getSession().getAttribute(WebAPI.INSTANCE.sessionAttribute);
        String jwt = WebRepository.INSTANCE.getToken(userId);
        int month = Integer.parseInt(request.getParameter("month"));
        WebAPI.INSTANCE.requestSender().sendHttpGetRequest(jwt, sortUrl + month);
        //TODO
        //TODO
        //TODO
        request.getRequestDispatcher("/main/work-list").forward(request, response);
    }

    private void workSorting() {

    }
}
