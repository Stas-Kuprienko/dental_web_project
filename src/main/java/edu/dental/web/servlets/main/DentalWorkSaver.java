package edu.dental.web.servlets.main;

import edu.dental.database.DBService;
import edu.dental.database.DatabaseException;
import edu.dental.domain.APIManager;
import edu.dental.domain.entities.I_DentalWork;
import edu.dental.domain.entities.User;
import edu.dental.domain.records.WorkRecordBook;
import edu.dental.domain.records.WorkRecordBookException;
import edu.dental.web.Repository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDate;

@WebServlet("/app/save-work")
public class DentalWorkSaver extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        String email = (String) session.getAttribute("user");
        if (email == null) {
            request.getRequestDispatcher("/enter").forward(request, response);
        }
        Repository.Account account = APIManager.instance().getRepository().get(email);
        User user = account.user();
        WorkRecordBook recordBook = account.recordBook();

        String patient = request.getParameter("patient");
        String clinic = request.getParameter("clinic");
        String product = request.getParameter("product");
        int quantity = Integer.parseInt(request.getParameter("quantity"));
        LocalDate complete = LocalDate.parse(request.getParameter("complete"));

        try {
            I_DentalWork dw = recordBook.createRecord(patient, clinic, product, quantity, complete);
            DBService dbService = APIManager.instance().getDBService();
            dbService.getDentalWorkDAO(user).put(dw);
            request.setAttribute("user", user);
            request.getRequestDispatcher("/app/main").forward(request, response);
        } catch (WorkRecordBookException | DatabaseException e) {
            System.out.println("Error!");
            request.getRequestDispatcher("/app/main").forward(request, response);
        }
    }
}
