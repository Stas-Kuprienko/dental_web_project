package edu.dental.web.servlets.works;

import edu.dental.domain.entities.DentalWork;
import edu.dental.domain.entities.I_DentalWork;
import edu.dental.domain.entities.Product;
import edu.dental.domain.records.WorkRecordBook;
import edu.dental.utils.data_structures.MyList;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public class DentalWorkEditor extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        int index = 0;
        try {
            index = Integer.parseInt(request.getParameter("index")) - 1;
        } catch (NumberFormatException ignored) {
            request.getRequestDispatcher("dental/").forward(request, response);
        }
        WorkRecordBook recordBook = (WorkRecordBook) session.getAttribute("recordBook");
        MyList<I_DentalWork> works = (MyList<I_DentalWork>) recordBook.getList();
        I_DentalWork dw = works.get(index);
        String page = new PageBuilder(index, recordBook).getResult();
        response.getWriter().write(page);
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
            <form action="/dental/edit-work" name="index: %s">
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

        public PageBuilder(int index, WorkRecordBook recordBook) {
            MyList<I_DentalWork> works = (MyList<I_DentalWork>) recordBook.getList();
            this.dw = (DentalWork) works.get(index);
            this.map = recordBook.getMap().keysToArray();
            String productList = buildProductList();
            String mapOptions = buildMapOptions();
            this.result = build(index, productList, mapOptions);
        }

        private String build(int index, String productList, String mapOptions) {
            String[] status = checkStatus();
            return String.format(htmlSample, index, dw.getPatient(),
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
