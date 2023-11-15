package edu.dental.web.servlets.works;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class DentalWorkEditor extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

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
            </style>
            <header><strong>DENTAL MECHANIC SERVICE</strong></header>
            <body>
            <h2>Edit record:</h2>
            <form action="/dental/edit-work" id="%s" method="post">
                <label for="patient">patient:</label><br>
                <input type="text" id="patient" name="patient" value="%s"><br>
                <label for="clinic">clinic:</label><br>
                <input type="text" id="clinic" name="clinic" value="%s"><br>
                <h4>
                    %s
                </h4>
                <form action="/dental/edit-work?%s">
                    <label for="product">product:</label><br>
                    <select id="product" name="product" style="width: 170px;">
                        <option value="%s">%s</option>
                        <option value="%s">%s</option>
                        <option value="%s">%s</option>
                    </select><br>
                    <label for="quantity">quantity:</label><br>
                    <input type="number" id="quantity" name="quantity" value="" max="32"><br><br>
                    <input type="submit" value="ADD PRODUCT" style="font-size: 16px; background-color: #78CF71"><br><br>
                </form>
                <label for="complete">complete:</label><br>
                <input type="date" id="complete" name="complete" style="width: 150px; height: 20px; font-size: 16px;" value="%s"><br>
                <br>
                <input type="radio" id="make" name="status" value="make">
                <label for="make">MAKE</label>
                <input type="radio" id="closed" name="status" value="closed">
                <label for="closed">CLOSED</label>
                <input type="radio" id="paid" name="status" value="paid">
                <label for="paid">PAID</label><br>
                <br>
                <label for="comment">comment:</label><br>
                <textarea id="comment" rows="5" name="comment" maxlength="127">%s</textarea><br>
                <br>
                <input type="submit" value="SAVE" style="font-size: 20px; width: 100px; background-color: #78CF71">
            </form>
            </body>
            </html>
            """;

    protected static class PageBuilder {

    }
}
