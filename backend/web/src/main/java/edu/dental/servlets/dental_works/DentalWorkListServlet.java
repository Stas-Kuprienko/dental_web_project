package edu.dental.servlets.dental_works;

import edu.dental.database.DatabaseException;
import edu.dental.database.DatabaseService;
import edu.dental.database.dao.DentalWorkDAO;
import edu.dental.domain.records.WorkRecordBook;
import edu.dental.dto.DentalWorkDto;
import edu.dental.entities.DentalWork;
import edu.dental.service.Repository;
import edu.dental.service.tools.JsonObjectParser;
import edu.dental.service.tools.RequestReader;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

@WebServlet("/main/dental-works")
public class DentalWorkListServlet extends HttpServlet {

    private JsonObjectParser jsonObjectParser;
    private Repository repository;
    private DentalWorkDAO dentalWorkDAO;

    @Override
    public void init() throws ServletException {
        this.jsonObjectParser = JsonObjectParser.getInstance();
        this.repository = Repository.getInstance();
        this.dentalWorkDAO = DatabaseService.getInstance().getDentalWorkDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = (int) request.getAttribute(Repository.paramUser);
        String year = request.getParameter("year");
        String month = request.getParameter("month");
        try {
            List<DentalWorkDto> works = getDentalWorks(userId, year, month);
            String json = jsonObjectParser.parseToJson(works.toArray());
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().print(json);
            response.getWriter().flush();
        } catch (DatabaseException e) {
            response.sendError(500);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = (int) request.getAttribute(Repository.paramUser);
        int year = Integer.parseInt(request.getParameter("year"));
        int month = Integer.parseInt(request.getParameter("month"));
        try {
            repository.getRecordBook(userId).sorting(month, year);
            List<DentalWorkDto> works = repository.getDentalWorkDtoList(userId);
            String json = jsonObjectParser.parseToJson(works.toArray());
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().print(json);
            response.getWriter().flush();
        } catch (DatabaseException e) {
            response.sendError(500);
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = (int) request.getAttribute(Repository.paramUser);
        HashMap<String, String> parameters = new RequestReader(request).getParameterMap();

        int year = Integer.parseInt(parameters.get("year"));
        int month = Integer.parseInt(parameters.get("month"));

        try {
            String json = setWorkListStatus(userId, year, month);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().print(json);
            response.getWriter().flush();
        } catch (DatabaseException e) {
            response.sendError(500, e.getMessage());
        }
    }

    private List<DentalWorkDto> getDentalWorks(int userId, String year, String month) throws DatabaseException {
        if (year == null || year.isEmpty() && month == null || month.isEmpty()) {
            return repository.getDentalWorkDtoList(userId);
        } else {
            int yearInt = Integer.parseInt(year);
            int monthInt = Integer.parseInt(month);
            WorkRecordBook recordBook = repository.getRecordBook(userId);
            List<DentalWork> works = recordBook.getWorksByMonth(monthInt, yearInt);
            return works.stream().map(DentalWorkDto::new).toList();
        }
    }

    private String setWorkListStatus(int userId, int year, int month) throws DatabaseException {
        String field = "status";
        String value = DentalWork.Status.PAID.toString();
        List<DentalWork> works;
        DentalWorkDto[] dtoArray = new DentalWorkDto[]{};
        if (isCurrent(year, month)) {
            works = repository.getRecordBook(userId).getRecords();
            dentalWorkDAO.setFieldValue(userId, works, field, value);
            repository.reloadWorks(userId);
            dtoArray = repository.getDentalWorkDtoList(userId).toArray(dtoArray);
        } else {
            works = repository.getRecordBook(userId).getWorksByMonth(month, year);
            dentalWorkDAO.setFieldValue(userId, works, field, value);
            works = repository.getRecordBook(userId).getWorksByMonth(month, year);
            dtoArray = works.stream().map(DentalWorkDto::new).toList().toArray(dtoArray);
        }
        return jsonObjectParser.parseToJson(dtoArray);
    }

    private boolean isCurrent(int year, int month) {
        LocalDate now = LocalDate.now();
        return (year == now.getYear() && month == now.getMonthValue());
    }
}