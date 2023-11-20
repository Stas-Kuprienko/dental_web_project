package edu.dental.web.servlets.basic;

import edu.dental.database.DBService;
import edu.dental.database.DBServiceManager;
import edu.dental.database.DatabaseException;
import edu.dental.domain.authentication.AuthenticationException;
import edu.dental.domain.authentication.Authenticator;
import edu.dental.domain.entities.I_DentalWork;
import edu.dental.domain.entities.User;
import edu.dental.domain.records.ProductMap;
import edu.dental.domain.records.RecordManager;
import edu.dental.domain.records.WorkRecordBook;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Collection;

public class MyFilter extends HttpFilter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession();
        User user;
        ProductMap productMap;
        WorkRecordBook recordBook;

        try {
            if (request.getAttribute("user") == null) {
                if ((request.getAttribute("email") != null) && (request.getAttribute("password")) != null) {

                    String login = request.getParameter("email");
                    String password = request.getParameter("password");
                    user = Authenticator.authenticate(login, password);
                    DBService dbService = DBServiceManager.get().getDBService();
                    productMap = dbService.getProductMapDAO(user).get();
                    Collection<I_DentalWork> dentalWorks = dbService.getDentalWorkDAO(user).getAll();
                    recordBook = RecordManager.get().getWorkRecordBook(dentalWorks, productMap);
                    session.setAttribute("user", user);
                    session.setAttribute("recordBook", recordBook);
                    request.getRequestDispatcher("/welcome").forward(request, response);
                }
                request.getRequestDispatcher("/").forward(request, response);
            }
        } catch (AuthenticationException | DatabaseException e) {
            request.getRequestDispatcher("/").forward(request, response);
        }
    }
}
