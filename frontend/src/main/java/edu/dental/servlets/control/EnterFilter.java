package edu.dental.servlets.control;

import edu.dental.WebAPI;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter(urlPatterns = {"/", "/sign-up"})
public class EnterFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute(WebAPI.INSTANCE.sessionAttribute) == null) {
            chain.doFilter(request, response);
        } else {
            request.getRequestDispatcher("/main").forward(request, response);
        }
    }
}
