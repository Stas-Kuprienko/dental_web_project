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
import edu.dental.web.Repository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebFilter("/app/*")
public class MyFilter extends HttpFilter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession();
        User user;

        try {
            if (session.getAttribute("user") == null) {
                if ((request.getAttribute("email") != null) && (request.getAttribute("password")) != null) {

                    String login = request.getParameter("email");
                    String password = request.getParameter("password");
                    user = Authenticator.authenticate(login, password);
                    DBService dbService = APIManager.instance().getDBService();
                    ProductMap productMap = dbService.getProductMapDAO(user).get();
                    List<I_DentalWork> dentalWorks = dbService.getDentalWorkDAO(user).getAll();
                    WorkRecordBook recordBook = APIManager.instance().getWorkRecordBook(dentalWorks, productMap);
                    session.setAttribute("user", user.getEmail());
                    Repository.put(user, recordBook);
                    filterChain.doFilter(request, response);
                }
                request.getRequestDispatcher("/enter").forward(request, response);
            } filterChain.doFilter(request, response);
        } catch (AuthenticationException | DatabaseException e) {
            request.getRequestDispatcher("/enter").forward(request, response);
        }
    }
}
