package edu.dental.web.servlets.works;

import edu.dental.domain.entities.I_DentalWork;
import edu.dental.domain.entities.Product;
import edu.dental.domain.entities.User;
import edu.dental.domain.records.ProductMap;
import edu.dental.domain.records.WorkRecordBook;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.Collection;
import java.util.NoSuchElementException;

public class WorkTablePage extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter writer = response.getWriter();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            request.getRequestDispatcher("dental/").forward(request, response);
        } else {
            WorkRecordBook recordBook = (WorkRecordBook) session.getAttribute("recordBook");
            String page = new PageBuilder(recordBook.getMap(), recordBook.getList()).getResult();
            writer.write(page);
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
                table {
                    border-collapse: collapse;
                    width: %s;
                }
                        
                tr {
                    border-bottom: 1px solid #ddd;
                }
                h2 {
                    font-size: 18px;
                }
            </style>
            <header><strong>DENTAL MECHANIC SERVICE</strong></header>
            <h2 style="text-align: center; margin-left: -900px;">
                <strong>%s</strong>
            </h2>
            <body>
            <form action="/dental/edit-work" id="id" method="post"></form>
                        
            <h3>
                <label style="background-color: DodgerBlue">CLOSED</label>&emsp;&emsp;&emsp;
                <label style="background-color: green"> PAID </label>
            </h3>
            <h2>
            <table>
                <tr>
                    <TD></TD>
                    <TD>PATIENT</TD>
                    <TD>CLINIC</TD>
                    %s
                </tr>
                %s
            </table>
            </h2>
            </body>
            </html>
            """;

    private static final String submit = "<input type=\"submit\" value=\"  %s  \" form=\"id\">";

    enum TD {
        COMMON("<TD>", "</TD>\n"),
        CLOSED("<TD style=\"background-color: DodgerBlue\">", "</TD>\n"),
        PAID("<TD style=\"background-color: green\">", "</TD>\n");
        final String open;
        final String close;
        TD(String open, String close) {
            this.open = open;
            this.close = close;
        }
    }

    protected static class PageBuilder {

        private final String[] productMap;
        private final Collection<I_DentalWork> dentalWorks;

        private final String result;

        protected PageBuilder(ProductMap productMap, Collection<I_DentalWork> dentalWorks) {
            this.productMap = productMap.keysToArray();
            this.dentalWorks = dentalWorks;
            String head = buildHead();
            String rows = buildRows();
            String month = LocalDate.now().getMonth() + "    " + LocalDate.now().getYear();
            this.result = String.format(htmlSample, "100%", month, head, rows);
        }

        protected String getResult() {
            return result;
        }

        private String buildRows() {
            StringBuilder rows = new StringBuilder();
            int i = 1;
            for (I_DentalWork dw : dentalWorks) {
                rows.append("<tr>\n\t\t");
                if (dw.getStatus().equals(I_DentalWork.Status.MAKE)) {
                    concatenateRecord(rows, TD.COMMON, i++, dw);
                } else if (dw.getStatus().equals(I_DentalWork.Status.CLOSED)) {
                    concatenateRecord(rows, TD.CLOSED, i++, dw);
                } else if (dw.getStatus().equals(I_DentalWork.Status.PAID)) {
                    concatenateRecord(rows, TD.PAID, i++, dw);
                } else {
                    throw new IllegalArgumentException("the DentalWork(id=" + dw.getId()
                            + ") status is illegal - " + dw.getStatus());
                }
                rows.append("</tr>\n\t\t");
            }
            return rows.toString();
        }

        private String buildHead() {
            StringBuilder head = new StringBuilder();
            for (String s : productMap) {
                head.append(TD.COMMON.open)
                        .append(s.toUpperCase())
                        .append(TD.COMMON.close)
                        .append("\n\t\t");
            }
            return head.toString();
        }

        private void concatenateRecord(StringBuilder str, TD td, int i, I_DentalWork dw) {
            str.append(td.open).append(String.format(submit, i)).append(td.close).append("\t\t")
                    .append(td.open).append(dw.getPatient()).append(td.close).append("\t\t")
                    .append(td.open).append(dw.getClinic()).append(td.close).append("\t\t");
            if (dw.getProducts().isEmpty()) {
                for (String ignored : productMap) {
                    str.append(td.open).append(" ").append(td.close).append("\t\t");
                }
            }
            for (String entry : productMap) {
                str.append(td.open);
                try {
                    Product p = dw.findProduct(entry);
                    str.append(p.quantity());
                } catch (NoSuchElementException | NullPointerException ignored) {
                    str.append(" ");
                }
                str.append(td.close).append("\t\t");
            }
        }
    }
}
