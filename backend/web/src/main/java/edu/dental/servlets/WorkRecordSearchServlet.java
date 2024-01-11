package edu.dental.servlets;

import edu.dental.domain.reports.ReportService;
import edu.dental.domain.reports.ReportServiceException;
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

    private JsonObjectParser jsonObjectParser;
    private ReportService reportService;

    @Override
    public void init() throws ServletException {
        this.jsonObjectParser = JsonObjectParser.getInstance();
        this.reportService = ReportService.getInstance();
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
        } catch (ReportServiceException e) {
            response.sendError(500);
        } catch (IllegalArgumentException e) {
            response.sendError(400);
        }
    }


    private DentalWorkDto[] search(int userId, HttpServletRequest request) throws ReportServiceException {
        ArrayList<String> fields = new ArrayList<>(searchFields.length);
        ArrayList<String> args = new ArrayList<>(searchFields.length);

        for (String parameter : searchFields) {
            String arg = request.getParameter(parameter);

            if (arg != null && !arg.isEmpty()) {
                fields.add(parameter);
                args.add(arg);
            }
        }
        if (fields.isEmpty() || args.isEmpty() || fields.size() != args.size()) {
            throw new IllegalArgumentException();
        }
        String[] arr1 = new String[]{};
        String[] arr2 = new String[]{};
        List<DentalWork> dentalWorks;

        dentalWorks = reportService.searchRecords(userId, fields.toArray(arr1), args.toArray(arr2));
        DentalWorkDto[] dto = new DentalWorkDto[dentalWorks.size()];

        return dentalWorks.stream().map(DentalWorkDto::new).toList().toArray(dto);
    }
}
