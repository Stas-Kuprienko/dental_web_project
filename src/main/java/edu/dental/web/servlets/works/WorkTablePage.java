package edu.dental.web.servlets.works;

import edu.dental.domain.entities.User;
import edu.dental.domain.records.WorkRecordBook;
import edu.dental.web.builders.TablePageBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;

public class WorkTablePage extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter writer = response.getWriter();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            request.getRequestDispatcher("/").forward(request, response);
        } else {
            WorkRecordBook recordBook = (WorkRecordBook) session.getAttribute("recordBook");
            String page = TablePageBuilder.get().build(recordBook.getMap(), recordBook.getList());
            writer.write(page);
        }
    }
}
