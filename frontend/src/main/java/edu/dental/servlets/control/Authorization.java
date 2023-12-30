package edu.dental.servlets.control;

import edu.dental.WebAPI;
import edu.dental.beans.DentalWork;
import edu.dental.beans.ProductMap;
import edu.dental.beans.UserDto;
import edu.dental.service.JsonObjectParser;
import edu.dental.service.WebRepository;
import edu.dental.service.RequestSender;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/log-in")
public class Authorization extends HttpServlet {

    public final String paramEmail = "email";
    public final String paramPassword = "password";
    public final String logInUrl = "log-in";
    public final String dentalWorkListUrl = "main/dental-works";
    public final String productMapUrl = "main/product-map";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter(paramEmail);
        String password = request.getParameter(paramPassword);
        if (email == null || password == null) {
            request.getRequestDispatcher("/").forward(request, response);
        } else {
            RequestSender.QueryFormer queryFormer = new RequestSender.QueryFormer();

            queryFormer.add(paramEmail, email);
            queryFormer.add(paramPassword, password);
            String requestParameters = queryFormer.form();

            String jsonUser = WebAPI.INSTANCE.requestSender().sendHttpPostRequest(logInUrl, requestParameters);
            UserDto user = JsonObjectParser.parser.fromJson(jsonUser, UserDto.class);

            String jsonWorks = WebAPI.INSTANCE.requestSender().sendHttpGetRequest(user.jwt(), dentalWorkListUrl);
            List<DentalWork> works = List.of(JsonObjectParser.parser.fromJson(jsonWorks, DentalWork[].class));

            String jsonMap = WebAPI.INSTANCE.requestSender().sendHttpGetRequest(user.jwt(), productMapUrl);
            List<ProductMap.Item> items = List.of(JsonObjectParser.parser.fromJson(jsonMap, ProductMap.Item[].class));
            ProductMap map = new ProductMap(items);

            WebRepository.INSTANCE.setAccount(user, works, map);
            request.getSession().setAttribute(WebAPI.INSTANCE.sessionAttribute, user.id());

            request.getRequestDispatcher("/main").forward(request, response);
        }
    }
}
