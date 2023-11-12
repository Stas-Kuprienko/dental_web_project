package edu.dental.web.servlets.works;

import edu.dental.domain.entities.I_DentalWork;
import edu.dental.domain.entities.Product;
import edu.dental.domain.entities.User;
import edu.dental.domain.records.ProductMap;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
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
            //TODO print options
            writer.write(user.getName());
        }
    }

    private static final String htmlPage = """
            <!DOCTYPE html>
            <html>
            <head>
                <style>
            table {
              border-collapse: collapse;
              width: %s;
            }
                        
            tr {
              border-bottom: 1px solid #ddd;
            }
            </style>
            </head>
            <body>
            <form action="/dental/edit" id="id" ></form>
            <table>
                <tr>
                    <TD></TD>
                    <TD>PATIENT</TD>
                    <TD>CLINIC</TD>
                    %s
                </tr>
                %s
            </table>
                        
            </body>
            </html>
            """;

    private static final String submit = "<input type=\"submit\" value=\"  %s  \" form=\"id\">";

    enum TD {
        COMMON("<TD>", "</TD>\n"),
        CLOSED("<TD style=\"background-color: yellow\">", "</TD>\n"),
        PAID("<TD style=\"background-color: green\">", "</TD>\n");
        final String open;
        final String close;
        TD(String open, String close) {
            this.open = open;
            this.close = close;
        }
    }

    protected class PageBuilder {

        private final String[] productMap;
        private final Collection<I_DentalWork> dentalWorks;

        private final String result;

        protected PageBuilder(ProductMap productMap, Collection<I_DentalWork> dentalWorks) {
            this.productMap = productMap.keysToArray();
            this.dentalWorks = dentalWorks;
            String head = buildHead();
            String rows = buildRows();
            this.result = String.format(htmlPage, "100%", head, rows);
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
