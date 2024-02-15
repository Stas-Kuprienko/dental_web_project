package edu.dental.servlets.user;

import edu.dental.database.DatabaseException;
import edu.dental.dto.UserDto;
import edu.dental.service.Repository;
import edu.dental.service.JsonObjectParser;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/main/account")
public class AccountServlet extends HttpServlet {

    private static final String fieldParam = "field";
    private static final String valueParam = "value";

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
        UserDto dto = new UserDto(repository.getUser(userId));
        String json = jsonObjectParser.parseToJson(dto);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().print(json);
        response.getWriter().flush();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = (int) request.getAttribute(Repository.paramUser);
        String field = request.getParameter(fieldParam);
        String value = request.getParameter(valueParam);
        if (nullParameters(field, value)) {
            response.sendError(400);
        } else {
            try {
                UserDto dto = repository.update(userId, field, value);
                if (dto == null) {
                    response.sendError(400);
                } else {
                    String json = jsonObjectParser.parseToJson(dto);
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().print(json);
                    response.getWriter().flush();
                }
            } catch (DatabaseException e) {
                response.sendError(500);
            }
        }
        }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int userId = (int) request.getAttribute(Repository.paramUser);
        try {
            repository.delete(userId, true);
            response.setStatus(200);
        } catch (DatabaseException e) {
            response.sendError(500, e.getMessage());
        }
    }


    private boolean nullParameters(String field, String value) {
        return (field == null || value == null ||
                field.isEmpty() || value.isEmpty());
    }
}