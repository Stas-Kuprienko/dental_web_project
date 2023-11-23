package edu.dental.web.servlets.works;

import edu.dental.domain.entities.DentalWork;
import edu.dental.domain.entities.I_DentalWork;
import edu.dental.domain.entities.Product;
import edu.dental.domain.records.WorkRecordBook;
import edu.dental.domain.records.WorkRecordBookException;
import edu.dental.web.Repository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/app/edit-work")
public class DentalWorkEditor extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        int id = 0;
        try {
            id = Integer.parseInt(request.getParameter("id"));
        } catch (NumberFormatException ignored) {
            request.getRequestDispatcher("/work-list").forward(request, response);
        }
        String email = (String) session.getAttribute("user");
        WorkRecordBook recordBook = Repository.getWorkRecordBook(email);
        try {
            I_DentalWork dw = recordBook.getByID(id);
            String page = new PageBuilder(dw, recordBook.getMap().keysToArray()).getResult();
            response.getWriter().write(page);
        } catch (WorkRecordBookException e) {
            request.getRequestDispatcher("/work-list").forward(request, response);
        }
    }

    private static final String htmlSample = """
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
                table.center {
            	margin-left: auto;
                margin-right: auto;
            }
            </style>
            <header><strong>DENTAL MECHANIC SERVICE</strong></header>
            <body>
            <h3>Edit record:</h3>
            <form action="/dental/edit-work?id=%s">
                <label for="patient">patient:</label><br>
                <input type="text" id="patient" name="patient" value="%s">
                <input type="hidden" name="field" value=patient">
                <button type="submit" style="background-color: #78CF71">input</button><br>
                <br>
                <label for="clinic">clinic:</label><br>
                <input type="text" id="clinic" name="clinic" value="%s">
                <input type="hidden" name="field" value="clinic">
                <button type="submit" style="background-color: #78CF71">input</button><br>
                <br>
                <h4>
                    <table class="center">
                        %s
                    </table>
                </h4>
                <form action="/dental/edit-work">
                    <label for="product">product:</label><br>
                    <select id="product" name="product" style="width: 170px;">
                        %s
                    </select><br>
                    <label for="quantity">quantity:</label><br>
                    <input type="number" id="quantity" name="quantity" value="" max="32"><br>
                    <button type="submit" style="background-color: #78CF71">add</button><br>
                </form>
                <br>
                <label for="complete">complete:</label><br>
                <input type="date" id="complete" name="complete" style="width: 150px; height: 20px; font-size: 16px;" value="%s">
                <input type="hidden" name="field" value="complete">
                <button type="submit" style="background-color: #78CF71">input</button><br>
                <br>
                <input type="radio" id="make" name="status" value="make" %s>
                <label for="make">MAKE</label>
                <input type="radio" id="closed" name="status" value="closed" %s>
                <label for="closed">CLOSED</label>
                <input type="radio" id="paid" name="status" value="paid" %s>
                <label for="paid">PAID</label>&emsp;
                <input type="hidden" name="field" value="status">
                <button type="submit" style="background-color: #78CF71" name="status">input</button><br>
                <br>
                <label for="comment">comment:</label><br>
                <textarea id="comment" rows="5" name="comment" maxlength="127">%s</textarea>
                <input type="hidden" name="field" value="comment">
                <button type="submit" style="background-color: #78CF71">input</button><br>
                <br><br>
                <button type="submit" style="font-size: 20px; width: 100px; background-color: #78CF71" action="dental/welcome/">OK</button>
            </form>
            </body>
            </html>
            """;

    private static final String productRow = """
            <tr>
                <td>%s</td>
                <th><button type="submit" >delete</button></th>
            </tr>
            """;

    private static final String mapOption = "<option value=\"%s\">%s</option>";

    protected static class PageBuilder {

        private final DentalWork dw;

        private final String[] map;

        private final String result;

        public PageBuilder(I_DentalWork dw, String[] map) {
            this.dw = (DentalWork) dw;
            this.map = map;
            String productList = buildProductList();
            String mapOptions = buildMapOptions();
            this.result = build(dw.getId(), productList, mapOptions);
        }

        private String build(int id, String productList, String mapOptions) {
            String[] status = checkStatus();
            return String.format(htmlSample, id, dw.getPatient(),
                    dw.getClinic(), productList, mapOptions, dw.getComplete(),
                    status[0], status[1], status[2], dw.getComment()!=null ? dw.getComment() : "");
        }

        private String buildProductList() {
            if (dw.getProducts().isEmpty()) {
                return "";
            }
            StringBuilder str = new StringBuilder();
            for (Product p : dw.getProducts()) {
                str.append(String.format(productRow, "-  " + p.title() + ": " + p.quantity())).append(" -");
            }
            return str.toString();
        }

        private String buildMapOptions() {
            StringBuilder str = new StringBuilder();
            for (String s : map) {
                str.append(String.format(mapOption, s, s));
            }
            return str.toString();
        }

        private String[] checkStatus() {
            String[] str = new String[]{"","",""};
            int i = dw.getStatus().ordinal();
            str[i] = "checked";
            return str;
        }

        protected String getResult() {
            return result;
        }
    }
}
