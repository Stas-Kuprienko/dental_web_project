package edu.dental.servlets;

import edu.dental.domain.records.WorkRecordBookException;
import edu.dental.dto.DentalWorkDto;
import edu.dental.service.tools.JsonObjectParser;
import edu.dental.service.Repository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/main/work-list/sort")
public class WorkRecordSortServlet extends HttpServlet {

    private Repository repository;
    private JsonObjectParser jsonObjectParser;

    @Override
    public void init() throws ServletException {
        this.repository = Repository.getInstance();
        this.jsonObjectParser = JsonObjectParser.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = (int) request.getAttribute(Repository.paramUser);
        int year = Integer.parseInt(request.getParameter("year"));
        int month = Integer.parseInt(request.getParameter("month"));
        try {
            repository.getRecordBook(userId).getSorter().doIt(year, month);
            List<DentalWorkDto> works = repository.getDentalWorkDtoList(userId);
            String json = jsonObjectParser.parseToJson(works.toArray());
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().print(json);
            response.getWriter().flush();
        } catch (WorkRecordBookException e) {
            response.sendError(500);
        }
    }
}
