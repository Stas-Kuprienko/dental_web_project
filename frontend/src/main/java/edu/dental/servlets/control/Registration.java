package edu.dental.servlets.control;

import edu.dental.WebAPI;
import edu.dental.beans.UserDto;
import edu.dental.service.JsonObjectParser;
import edu.dental.service.Repository;
import edu.dental.servlets.RequestSender;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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
            RequestSender.QueryFormer queryFormer = new RequestSender.QueryFormer();

            queryFormer.add(paramName, name);
            queryFormer.add(paramEmail, email);
            queryFormer.add(paramPassword, password);
            String requestParameters = queryFormer.form();

            String jsonUser = WebAPI.INSTANCE.requestSender().sendHttpPostRequest(signUpUrl, requestParameters);
            UserDto user = JsonObjectParser.parser.fromJson(jsonUser, UserDto.class);

            Repository.getInstance().addNew(user);
            request.getSession().setAttribute(WebAPI.INSTANCE.sessionAttribute, user.id());

            request.getRequestDispatcher("/main").forward(request, response);
        }
    }
}