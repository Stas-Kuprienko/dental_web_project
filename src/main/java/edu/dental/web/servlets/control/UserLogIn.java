package edu.dental.web.servlets.control;

import edu.dental.database.DatabaseException;
import edu.dental.database.mysql_api.dao.DentalWorkMySql;
import edu.dental.database.mysql_api.dao.ProductMapMySql;
import edu.dental.domain.authentication.AuthenticationException;
import edu.dental.domain.authentication.Authenticator;
import edu.dental.domain.entities.I_DentalWork;
import edu.dental.domain.entities.User;
import edu.dental.domain.records.ProductMap;
import edu.dental.domain.records.my_work_record_book.MyProductMap;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.collections.SimpleList;

import java.io.IOException;

public class UserLogIn extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String login = "stas.com";
        String password = "1234";
        User user = null;
        try {
            user = Authenticator.authenticate(login, password);
        } catch (AuthenticationException e) {
            e.printStackTrace();
        }
        System.out.println(user);
        SimpleList<I_DentalWork> records = new SimpleList<>();
        ProductMap map = new MyProductMap();
        try {
            records = (SimpleList<I_DentalWork>) new DentalWorkMySql(user).getAll();
            map = new ProductMapMySql(user).get();
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
        System.out.println(records);
        System.out.println(map);
//
//        request.setAttribute(PageBuilder.TABLE_MAP, map.keysToArray());
//        request.setAttribute(PageBuilder.WORK_LIST, records);
//        request.getRequestDispatcher("/work-list").forward(request, response);
    }
}
