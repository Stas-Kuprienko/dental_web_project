package edu.dental.servlets;

import edu.dental.WebAPI;
import edu.dental.dto.DentalWork;
import edu.dental.service.AuthenticationService;
import edu.dental.service.JsonObjectParser;
import edu.dental.service.Repository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/main/dental-works")
public class DentalWorkListServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendError(405);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = AuthenticationService.verification(request.getParameter(WebAPI.INSTANCE.paramToken));
        List<DentalWork> works = Repository.getInstance().getDentalWorkDtoList(userId);
        String json = JsonObjectParser.getInstance().parseToJson(works.toArray());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().print(json);
        response.getWriter().flush();
    }
}
