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
        String authorizationType = "Bearer ";

        String authorization = request.getHeader("Authorization");

        if (authorization != null && authorization.startsWith(authorizationType)) {
            String jwt = authorization.substring(authorizationType.length());
            int userId = AuthenticationService.verification(jwt);
            request.setAttribute(WebAPI.INSTANCE.paramUser, userId);
            chain.doFilter(request, response);
        } else {
            response.sendError(403);
        }
    }
}
