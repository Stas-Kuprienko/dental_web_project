package edu.dental.web.servlets.control;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/log-in")
public class UserLogIn extends HttpServlet {

//    @Override
//    public void init() throws ServletException {
//        DBServiceManager.get().getDBService().getTableInitializer().addReports();
//    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String[] map = new String[5];
        for (int i = 0; i < 5; i++) {
            map[i] = String.valueOf(i);
        }
        request.setAttribute("map", map);
        request.getRequestDispatcher("/work-list").forward(request, response);
    }

}
