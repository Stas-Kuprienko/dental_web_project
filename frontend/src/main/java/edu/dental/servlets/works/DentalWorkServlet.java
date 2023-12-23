package edu.dental.servlets.works;

import edu.dental.beans.DentalWork;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;

@WebServlet("/main/dental-work")
public class DentalWorkServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String method = request.getParameter("method");
        if (method != null && method.equals("put")) {
            doPut(request, response);
        } else if (method != null && method.equals("delete")) {
            doDelete(request, response);
        } else {
            String login = (String) request.getSession().getAttribute("user");

            String patient = request.getParameter("patient");
            String clinic = request.getParameter("clinic");
            String product = request.getParameter("product");
            int quantity = Integer.parseInt(request.getParameter("quantity"));
            LocalDate complete = LocalDate.parse(request.getParameter("complete"));

            int id = newDentalWork(login, patient, clinic, product, quantity, complete);
            request.setAttribute("id", id);
            doGet(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String user = (String) request.getSession().getAttribute("user");
        int id = request.getParameter("id") != null ?
                Integer.parseInt(request.getParameter("id")) : (int) request.getAttribute("id");
        DentalWork dto = null;
        request.setAttribute("work", dto);
        request.getRequestDispatcher("/main/dental-work/page").forward(request, response);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String user = (String) request.getSession().getAttribute("user");
        int id = Integer.parseInt(request.getParameter("id"));
        String field = request.getParameter("field");
        String value = request.getParameter("value");
        if (field.equals("product")) {
            if (!(value == null || value.isEmpty())) {
                int quantity = Integer.parseInt(request.getParameter("quantity"));
                addProductToWork(user, id, value, quantity);
            }
        } else {
            editWork(user, id, field, value);
        }
        doGet(request, response);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String user = (String) request.getSession().getAttribute("user");
        int id = Integer.parseInt(request.getParameter("id"));
        String product = request.getParameter("product");
        if (product != null) {
            deleteProductFromWork(user, id, product);
            doGet(request, response);
        } else {
            deleteWorkRecord(user, id);
            request.getRequestDispatcher("/main/work-list").forward(request, response);
        }
    }

    private int newDentalWork(String login, String patient, String clinic, String product, int quantity, LocalDate complete) {
        DentalWork dw;
        return 0;
    }

    private void addProductToWork(String login, int id, String product, int quantity) {
    }

    private void editWork(String login, int id, String field, String value) {
    }

    private void deleteProductFromWork(String login, int id, String product) {
    }


    private void deleteWorkRecord(String login, int id) {
    }

    private String concatenate(String toModify, @SuppressWarnings("all") String toInsert) {
        char firstLetter = (char) (toModify.charAt(0) - 32);
        StringBuilder str = new StringBuilder(toModify);
        str.setCharAt(0, firstLetter);
        str.insert(0, toInsert, 0, toInsert.length());
        return str.toString();
    }
}
