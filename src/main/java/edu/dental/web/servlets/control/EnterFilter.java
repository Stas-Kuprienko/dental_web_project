package edu.dental.web.servlets.control;

import edu.dental.web.MyRepository;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public class EnterFilter implements Filter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        HttpSession session = request.getSession();
        if (session == null) {
            chain.doFilter(request, response);
        } else {
            String user = (String) session.getAttribute("user");
            if (user == null || MyRepository.get(user) == null) {
                chain.doFilter(request, response);
            } else {
                request.getRequestDispatcher("/main");
            }
        }
    }
}
