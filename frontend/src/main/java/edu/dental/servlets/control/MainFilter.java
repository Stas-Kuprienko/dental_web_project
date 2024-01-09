package edu.dental.servlets.control;

import edu.dental.WebAPI;
import edu.dental.service.Reception;
import edu.dental.service.WebRepository;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter("/main/*")
public class MainFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute(WebAPI.INSTANCE.sessionUser) == null) {
            request.getRequestDispatcher("/").forward(request, response);
        } else {
            int userId = (int) session.getAttribute(WebAPI.INSTANCE.sessionUser);
            if (!WebRepository.INSTANCE.isExist(userId)) {
                Reception.getInstance().getByToken((String) session.getAttribute(WebAPI.INSTANCE.sessionToken));
            }
            WebRepository.INSTANCE.updateAccountLastAction(userId);
            chain.doFilter(request, response);
        }
    }
}
