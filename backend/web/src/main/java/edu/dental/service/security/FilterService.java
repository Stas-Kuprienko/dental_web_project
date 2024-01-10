package edu.dental.service.security;

import edu.dental.WebAPI;
import edu.dental.entities.User;
import edu.dental.service.Repository;
import edu.dental.service.WebException;
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

        try {
            if (authorization != null && authorization.startsWith(authorizationType)) {
                String jwt = authorization.substring(authorizationType.length());
                int userId = AuthenticationService.verification(jwt);
                if (userId > 0) {
                    if (isLoggedIn(userId)) {
                        Repository.getInstance().updateAccountLastAction(userId);
                    } else {
                        if (!addToRepository(jwt)) {
                            response.sendError(403);
                            return;
                        }
                    }
                    request.setAttribute(WebAPI.INSTANCE.paramUser, userId);
                    chain.doFilter(request, response);
                }
            } else {
                response.sendError(403);
            }
        } catch (WebException e) {
            response.sendError(e.code.i);
        }
    }

    private boolean isLoggedIn(int userId) {
        return Repository.getInstance().get(userId) != null;
    }

    private boolean addToRepository(String jwt) throws WebException {
        User user = AuthenticationService.getUser(jwt);
        return Repository.getInstance().put(user);
    }
}
