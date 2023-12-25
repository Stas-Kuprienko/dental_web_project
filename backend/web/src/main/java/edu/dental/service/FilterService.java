package edu.dental.service;

import edu.dental.WebAPI;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter("/main/*")
public class FilterService implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        if (request.getParameter(WebAPI.INSTANCE.paramToken) == null) {
            response.sendError(401);
        } else {
            chain.doFilter(request, response);
        }
    }
}
