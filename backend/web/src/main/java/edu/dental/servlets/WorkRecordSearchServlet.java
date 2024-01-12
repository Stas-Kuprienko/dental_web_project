package edu.dental.servlets;

import edu.dental.domain.records.WorkRecordBook;
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
import java.util.ArrayList;
import java.util.List;

@WebServlet("/main/dental-works/search")
public class WorkRecordSearchServlet extends HttpServlet {

    private final String patientParam = "patient";
    private final String clinicParam = "clinic";

    private final String[] searchFields = {patientParam, clinicParam};

    private Repository repository;
    private JsonObjectParser jsonObjectParser;

    @Override
    public void init() throws ServletException {
        this.repository = Repository.getInstance();
        this.jsonObjectParser = JsonObjectParser.getInstance();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = (int) request.getAttribute(Repository.paramUser);
        try {
            DentalWorkDto[] works = search(userId, request);
            String json = jsonObjectParser.parseToJson(works);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().print(json);
            response.getWriter().flush();
        } catch (WorkRecordBookException e) {
            response.sendError(500);
        }
    }


    private DentalWorkDto[] search(int userId, HttpServletRequest request) throws WorkRecordBookException {
        ArrayList<String> fields = new ArrayList<>(searchFields.length);
        ArrayList<String> args = new ArrayList<>(searchFields.length);

        for (String parameter : searchFields) {
            String arg = request.getParameter(parameter);

            if (arg != null && !arg.isEmpty()) {
                fields.add(parameter);
                args.add(arg);
            }
        }
        String[] arr1 = new String[]{};
        String[] arr2 = new String[]{};
        List<DentalWork> dentalWorks;

        WorkRecordBook recordBook = repository.getRecordBook(userId);
        dentalWorks = recordBook.searchRecordsInDatabase(fields.toArray(arr1), args.toArray(arr2));
        DentalWorkDto[] dto = new DentalWorkDto[dentalWorks.size()];

        return dentalWorks.stream().map(DentalWorkDto::new).toList().toArray(dto);
    }
}
