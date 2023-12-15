package edu.dental.web.servlets.works;

import edu.dental.database.DatabaseException;
import edu.dental.database.DatabaseService;
import edu.dental.database.dao.DentalWorkDAO;
import edu.dental.domain.entities.DentalWork;
import edu.dental.domain.entities.User;
import edu.dental.domain.entities.dto.DentalWorkDTO;
import edu.dental.domain.records.WorkRecordBook;
import edu.dental.domain.records.WorkRecordBookException;
import edu.dental.web.Repository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
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

            try {
                int id = newDentalWork(login, patient, clinic, product, quantity, complete);
                request.setAttribute("id", id);
                doGet(request, response);
            } catch (DatabaseException | WorkRecordBookException e) {
                response.sendError(500);
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String user = (String) request.getSession().getAttribute("user");
        int id = request.getParameter("id") != null ?
                Integer.parseInt(request.getParameter("id")) : (int) request.getAttribute("id");
        try {
            DentalWorkDTO dto = new DentalWorkDTO(Repository.getInstance().getRecordBook(user).getByID(id));
            request.setAttribute("work", dto);
            request.getRequestDispatcher("/main/dental-work/page").forward(request, response);
        } catch (WorkRecordBookException e) {
            request.getRequestDispatcher("/error?").forward(request, response);
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String user = (String) request.getSession().getAttribute("user");
        int id = Integer.parseInt(request.getParameter("id"));
        String field = request.getParameter("field");
        String value = request.getParameter("value");
        try {
            if (field.equals("product")) {
                if (!(value == null || value.isEmpty())) {
                    int quantity = Integer.parseInt(request.getParameter("quantity"));
                    addProductToWork(user, id, value, quantity);
                }
            } else {
                editWork(user, id, field, value);
            }
        } catch (WorkRecordBookException | DatabaseException e) {
            response.sendError(500);
            return;
        }
        doGet(request, response);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String user = (String) request.getSession().getAttribute("user");
        int id = Integer.parseInt(request.getParameter("id"));
        String product = request.getParameter("product");
        try {
            if (product != null) {
                deleteProductFromWork(user, id, product);
                doGet(request, response);
            } else {
                deleteWorkRecord(user, id);
                request.getRequestDispatcher("/main/work-list").forward(request, response);
            }
        } catch (WorkRecordBookException | DatabaseException e) {
            response.sendError(500);
        }
    }

    private int newDentalWork(String login, String patient, String clinic, String product, int quantity, LocalDate complete) throws WorkRecordBookException, DatabaseException {
        User user = Repository.getInstance().getUser(login);
        WorkRecordBook recordBook = Repository.getInstance().getRecordBook(login);
        DentalWorkDAO workDAO = DatabaseService.getInstance().getDentalWorkDAO(user);
        DentalWork dw;

        if (product == null || product.isEmpty()) {
            dw = recordBook.createRecord(patient, clinic);
        } else {
            dw = recordBook.createRecord(patient, clinic, product, quantity, complete);
        }
        try {
            workDAO.put(dw);
        } catch (DatabaseException e) {
            recordBook.deleteRecord(dw);
            throw e;
        }
        return dw.getId();
    }

    private void addProductToWork(String login, int id, String product, int quantity) throws WorkRecordBookException, DatabaseException {
        User user = Repository.getInstance().getUser(login);
        WorkRecordBook recordBook = Repository.getInstance().getRecordBook(login);
        DentalWorkDAO workDAO = DatabaseService.getInstance().getDentalWorkDAO(user);
        DentalWork dw;
        dw = recordBook.getByID(id);
        recordBook.addProductToRecord(dw, product, quantity);
        try {
            workDAO.edit(dw);
        } catch (DatabaseException e) {
            recordBook.deleteRecord(dw);
            throw e;
        }
    }

    private void editWork(String login, int id, String field, String value) throws WorkRecordBookException, DatabaseException {
        User user = Repository.getInstance().getUser(login);
        WorkRecordBook recordBook = Repository.getInstance().getRecordBook(login);
        DentalWorkDAO workDAO = DatabaseService.getInstance().getDentalWorkDAO(user);
        DentalWork dw = recordBook.getByID(id);
        try {
            DentalWork.class.getMethod(concatenate(field, "set"), String.class).invoke(dw, value);
            workDAO.edit(dw);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new WorkRecordBookException(e.getMessage(), e);
        }
    }

    private void deleteProductFromWork(String login, int id, String product) throws WorkRecordBookException, DatabaseException {
        User user = Repository.getInstance().getUser(login);
        WorkRecordBook recordBook = Repository.getInstance().getRecordBook(login);
        DentalWorkDAO workDAO = DatabaseService.getInstance().getDentalWorkDAO(user);
        DentalWork dw = recordBook.getByID(id);
        recordBook.removeProduct(dw, product);
        workDAO.edit(dw);
    }


    private void deleteWorkRecord(String login, int id) throws WorkRecordBookException, DatabaseException {
        User user = Repository.getInstance().getUser(login);
        WorkRecordBook recordBook = Repository.getInstance().getRecordBook(login);
        DentalWorkDAO workDAO = DatabaseService.getInstance().getDentalWorkDAO(user);
        DentalWork dw = recordBook.getByID(id);
        recordBook.deleteRecord(dw);
        workDAO.delete(id);
    }

    private String concatenate(String toModify, @SuppressWarnings("all") String toInsert) {
        char firstLetter = (char) (toModify.charAt(0) - 32);
        StringBuilder str = new StringBuilder(toModify);
        str.setCharAt(0, firstLetter);
        str.insert(0, toInsert, 0, toInsert.length());
        return str.toString();
    }
}
