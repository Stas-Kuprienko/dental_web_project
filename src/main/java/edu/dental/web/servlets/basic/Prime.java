package edu.dental.web.servlets.basic;

import edu.dental.database.DBService;
import edu.dental.database.DBServiceManager;
import edu.dental.database.DatabaseException;
import edu.dental.domain.authentication.AuthenticationException;
import edu.dental.domain.authentication.Authenticator;
import edu.dental.domain.entities.I_DentalWork;
import edu.dental.domain.entities.User;
import edu.dental.domain.records.ProductMap;
import edu.dental.domain.records.RecordManager;
import edu.dental.domain.records.WorkRecordBook;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

public class Prime extends HttpServlet {

//    @Override
//    public void init() throws ServletException {
//        DBServiceManager.get().getDBService().getTableInitializer().addReports();
//    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter writer = response.getWriter();
        User user;
        ProductMap productMap;
        WorkRecordBook recordBook;

        try {
            if (request.getAttribute("user") == null) {
                String login = request.getParameter("email");
                String password = request.getParameter("password");
                user = Authenticator.authenticate(login, password);
                DBService dbService = DBServiceManager.get().getDBService();
                productMap = dbService.getProductMapDAO(user).get();
                Collection<I_DentalWork> dentalWorks = dbService.getDentalWorkDAO(user).getAll();
                recordBook = RecordManager.get().getWorkRecordBook(dentalWorks, productMap);
                HttpSession session = request.getSession();
                session.setAttribute("user", user);
                session.setAttribute("recordBook", recordBook);
            } else {
                user = (User) request.getAttribute("user");
            }
            writer.write(String.format(htmlPage, user.getName()));
        } catch (AuthenticationException | DatabaseException e) {
            writer.write("no such user.");
            request.getRequestDispatcher("dental/").forward(request, response);
        }
    }

    private static final String htmlPage = """
            <!DOCTYPE html>
            <html>
            <meta charset="UTF-8">
                        
            <style>
                        
                header {
                    background-color: #555;
                    padding: 30px;
                    text-align: center;
                    font-size: 35px;
                    color: white;
                }
                body {
                    background-color: dimGrey;
                    text-align: center;
                    font-size: 20px;
                    color:white;
                }
            </style>
            <header><strong>DENTAL MECHANIC SERVICE</strong></header>
            <body>
            <h2>Welcome, %s!</h2>
            <form action="/dental/new-product" method="post">
                <input type="submit" value="NEW PRODUCT TYPE" style="font-size: 20px; width: 300px; background-color: #78CF71">
            </form>
            <form action="/dental/product-map" method="post">
                <input type="submit" value="OPEN PRODUCT MAP" style="font-size: 20px; width: 300px; background-color: #78CF71">
            </form>
            <form action="/dental/new-work">
                <input type="submit" value="NEW WORK RECORD" style="font-size: 20px; width: 300px; background-color: #78CF71">
            </form>
            <form action="/dental//work-list">
                <input type="submit" value="OPEN WORK LIST" style="font-size: 20px; width: 300px; background-color: #78CF71">
            </form>
            <form action="/dental/save-report-file">
                <input type="submit" value="SAVE WORK REPORT" style="font-size: 20px; width: 300px; background-color: #78CF71">
            </form>
            </body>
            </html>
            """;
}
