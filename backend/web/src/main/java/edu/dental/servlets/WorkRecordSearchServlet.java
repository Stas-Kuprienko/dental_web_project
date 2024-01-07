package edu.dental.servlets;

import edu.dental.WebAPI;
import edu.dental.domain.reports.ReportService;
import edu.dental.domain.reports.ReportServiceException;
import edu.dental.dto.DentalWorkDto;
import edu.dental.entities.DentalWork;
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

    public final String patientParam = "patient";
    public final String clinicParam = "clinic";

    public final String[] searchFields = {patientParam, clinicParam};


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = (int) request.getAttribute(WebAPI.INSTANCE.paramUser);
        try {
            DentalWorkDto[] works = search(userId, request);
            String json = JsonObjectParser.getInstance().parseToJson(works);
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

        dentalWorks = ReportService.getInstance().searchRecords(userId, fields.toArray(arr1), args.toArray(arr2));
        DentalWorkDto[] dto = new DentalWorkDto[dentalWorks.size()];

        return dentalWorks.stream().map(DentalWorkDto::new).toList().toArray(dto);
    }
}
