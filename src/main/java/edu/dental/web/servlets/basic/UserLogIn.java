package edu.dental.web.servlets.basic;

import edu.dental.database.DBService;
import edu.dental.database.DatabaseException;
import edu.dental.domain.APIManager;
import edu.dental.domain.authentication.AuthenticationException;
import edu.dental.domain.authentication.Authenticator;
import edu.dental.domain.entities.I_DentalWork;
import edu.dental.domain.entities.User;
import edu.dental.domain.records.ProductMap;
import edu.dental.domain.records.WorkRecordBook;
import edu.dental.web.RAM;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Collection;

public class UserLogIn extends HttpServlet {

//    @Override
//    public void init() throws ServletException {
//        DBServiceManager.get().getDBService().getTableInitializer().addReports();
//    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user;
        ProductMap productMap;
        WorkRecordBook recordBook;

        try {
            if (request.getAttribute("user") == null) {
                String login = request.getParameter("email");
                String password = request.getParameter("password");
                user = Authenticator.authenticate(login, password);
                DBService dbService = APIManager.instance().getDBService();
                productMap = dbService.getProductMapDAO(user).get();
                Collection<I_DentalWork> dentalWorks = dbService.getDentalWorkDAO(user).getAll();
                recordBook = APIManager.instance().getWorkRecordBook(dentalWorks, productMap);
                HttpSession session = request.getSession();
                session.setAttribute("user", user.getEmail());
                RAM.put(user, recordBook);
                request.getRequestDispatcher("/welcome").forward(request, response);
            } else {
                request.getRequestDispatcher("/").forward(request, response);
            }
        } catch (AuthenticationException | DatabaseException e) {
            request.getRequestDispatcher("/").forward(request, response);
        }
    }

}
