package edu.dental.web.servlets.works;

import edu.dental.domain.records.WorkRecordBook;
import edu.dental.web.Repository;
import edu.dental.web.builders.TablePageBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/app/work-list")
public class WorkTablePage extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter writer = response.getWriter();
        HttpSession session = request.getSession();
        String email = (String) session.getAttribute("user");
        try {
            Repository.Account account = Repository.get(email);
            WorkRecordBook recordBook = account.recordBook();
            String page = TablePageBuilder.get().build(recordBook.getMap(), recordBook.getList());
            writer.write(page);
        } catch (NullPointerException ignored) {
            request.getRequestDispatcher("/app/main").forward(request, response);
        }
    }
}

