package edu.dental.web.servlets.products;

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

public class ProductMapTable extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        PrintWriter writer = response.getWriter();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            request.getRequestDispatcher("dental/").forward(request, response);
        }
        WorkRecordBook recordBook = (WorkRecordBook) session.getAttribute("recordBook");
        String page = new PageBuilder(recordBook.getMap()).getResult();
        page = String.format(htmlSample, "50%", page);
        writer.write(page);
    }

    private static final String htmlSample = """
            <!DOCTYPE html>
            <html>
            <head>
            <meta charset="UTF-8">
            <style>
                body {
                    background-color: dimGrey;
                }
                body {color:white;}
                        
            table {
              border-collapse: collapse;
              width: %s;
            }
                        
            tr {
              border-bottom: 1px solid #ddd;
            }
            </style>
            <body>
            <form action="/dental/edit-type" id="id" ></form>
                        
            <h2>
            <table>
                %s
            </table>
            </h2>
            
            </body>
            </html>
            """;

    private static final String tableRow = """
            <tr>
                <TD><input type="submit" value="%s" form="id"></TD>
                <TD>%s</TD>
                <TD>%s</TD>
            </tr>
            """;

    protected static class PageBuilder {

        private final ProductMap map;

        private final String result;

        public PageBuilder(ProductMap map) {
            this.map = map;
            this.result = build();
        }

        private String build() {
            StringBuilder str = new StringBuilder();
            int i = 1;
            for (ProductMap.Item e : map.toArray()) {
                String s = String.format(tableRow, i++, e.getKey(), e.getValue());
                str.append(s).append("\n");
            }
            return str.toString();
        }

        protected String getResult() {
            return result;
        }
    }
}
