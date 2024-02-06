package edu.dental.security;

import edu.dental.WebException;
import edu.dental.service.Repository;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter("/main/*")
public class FilterService implements Filter {

    private IFilterVerification filterVerification;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
        this.filterVerification = IFilterVerification.getInstance();
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        int userId;

        try {
            userId = filterVerification.verify(request);
            request.setAttribute(Repository.paramUser, userId);
            chain.doFilter(request, response);
        } catch (WebException e) {
            response.sendError(e.code.code, e.message);
        }
    }
}
