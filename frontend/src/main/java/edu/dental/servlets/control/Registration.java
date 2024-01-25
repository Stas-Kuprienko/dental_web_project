package edu.dental.servlets.control;

import edu.dental.APIResponseException;
import edu.dental.service.WebUtility;
import edu.dental.beans.DentalWork;
import edu.dental.beans.ProductMap;
import edu.dental.beans.UserDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/new-user")
public class Registration extends HttpServlet {

    public final String paramName = "name";
    public final String paramEmail = "email";
    public final String paramPassword = "password";
    public final String signUpUrl = "sign-up";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/sign-up").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter(paramName);
        String email = request.getParameter(paramEmail);
        String password = request.getParameter(paramPassword);
        if (email == null || password == null) {
            request.getRequestDispatcher("/sign-up").forward(request, response);
        } else {
            try {
                WebUtility.QueryFormer queryFormer = new WebUtility.QueryFormer();

                queryFormer.add(paramName, name);
                queryFormer.add(paramEmail, email);
                queryFormer.add(paramPassword, password);
                String requestParameters = queryFormer.form();

                String jsonUser = WebUtility.INSTANCE.requestSender().sendHttpPostRequest(signUpUrl, requestParameters);
                UserDto user = WebUtility.INSTANCE.parseFromJson(jsonUser, UserDto.class);
                HttpSession session = request.getSession();
                session.setAttribute(WebUtility.INSTANCE.sessionUser, user.getId());
                session.setAttribute(WebUtility.INSTANCE.sessionWorks, new DentalWork[]{});
                session.setAttribute(WebUtility.INSTANCE.sessionMap, new ProductMap());
                request.getRequestDispatcher("/main").forward(request, response);
            } catch (APIResponseException e) {
                response.sendError(e.CODE, e.MESSAGE);
            }
        }
    }
}