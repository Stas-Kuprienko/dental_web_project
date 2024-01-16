package edu.dental.servlets;

import edu.dental.WebException;
import edu.dental.database.DatabaseException;
import edu.dental.database.DatabaseService;
import edu.dental.database.dao.DentalWorkDAO;
import edu.dental.domain.account.AccountException;
import edu.dental.domain.records.WorkRecordBookException;
import edu.dental.dto.DentalWorkDto;
import edu.dental.entities.DentalWork;
import edu.dental.service.Repository;
import edu.dental.service.tools.JsonObjectParser;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@WebServlet("/main/work-list/sort")
public class WorkRecordSortServlet extends HttpServlet {

    private Repository repository;
    private JsonObjectParser jsonObjectParser;
    private DentalWorkDAO dentalWorkDAO;

    @Override
    public void init() throws ServletException {
        this.repository = Repository.getInstance();
        this.jsonObjectParser = JsonObjectParser.getInstance();
        this.dentalWorkDAO = DatabaseService.getInstance().getDentalWorkDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
        } catch (WorkRecordBookException e) {
            response.sendError(500);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = (int) request.getAttribute(Repository.paramUser);
        int year = Integer.parseInt(request.getParameter("year"));
        int month = Integer.parseInt(request.getParameter("month"));

        try {
            String json = execute(userId, year, month);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().print(json);
            response.getWriter().flush();
        } catch (WebException e) {
            response.sendError(500, e.getMessage());
        }
    }


    private String execute(int userId, int year, int month) throws WebException {
        String field = "status";
        String value = DentalWork.Status.PAID.toString();
        List<DentalWork> works;
        DentalWorkDto[] dtoArray = new DentalWorkDto[]{};
        try {
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
        } catch (DatabaseException | WorkRecordBookException e) {
            throw new WebException(AccountException.CAUSE.SERVER_ERROR, e);
        }
    }

    private boolean isCurrent(int year, int month) {
        LocalDate now = LocalDate.now();
        return (year == now.getYear() && month == now.getMonthValue());
    }
}
