package edu.dental.servlets.filters;

import edu.dental.service.WebUtility;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter(urlPatterns = {"/", "/sign-in", "/sign-up", "/log-in", "/new-user"})
public class EnterFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute(WebUtility.INSTANCE.sessionToken) == null) {
            chain.doFilter(request, response);
        } else {
            request.getRequestDispatcher("/main").forward(request, response);
        }
    }
}
