package dental.server;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

/*
    Require connection with Tomcat:
        - Apache Tomcat/10.1.11
        - deployment directory (...\dental\src\main\webapp)
        - module (dental)
        - context (/dental)
 */

@WebServlet
public class ServerApp extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException
    {
        PrintWriter writer = response.getWriter();
        writer.write("All good!");
        writer.close();
    }
}
