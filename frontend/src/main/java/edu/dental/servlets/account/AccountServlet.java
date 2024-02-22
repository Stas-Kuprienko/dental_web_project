package edu.dental.servlets.account;

import stas.exceptions.HttpWebException;
import edu.dental.beans.UserBean;
import edu.dental.service.WebUtility;
import edu.dental.control.Administrator;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.eclipse.tags.shaded.org.apache.bcel.verifier.exc.InvalidMethodException;
import stas.utilities.LoggerKit;

import java.io.IOException;
import java.util.logging.Level;

@WebServlet("/main/account")
public class AccountServlet extends HttpServlet {

    private static final String accountUrl = "main/account";
    private static final String fieldParam = "field";
    private static final String valueParam = "value";
    private static final String accountPageURL = "/main/account/page";

    private LoggerKit loggerKit;
    private Administrator administrator;

    @Override
    public void init() throws ServletException {
        this.loggerKit = WebUtility.INSTANCE.loggerKit();
        this.administrator = Administrator.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String token = (String) request.getSession().getAttribute(WebUtility.INSTANCE.attribToken);
            UserBean user = administrator.getUser(token);
            request.setAttribute(WebUtility.INSTANCE.attribUser, user);
            request.getRequestDispatcher(accountPageURL).forward(request, response);
        } catch (HttpWebException e) {
            e.errorRedirect(WebUtility.INSTANCE.errorPageURL, request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getParameter("method") != null) {
            chooseMethod(request, response);
        } else {
            ServletException e = new ServletException(request.getRequestURI());
            loggerKit.doLog(this.getClass().getSuperclass(), e, Level.SEVERE);
            new HttpWebException(HttpWebException.ERROR.NOT_ALLOWED).errorRedirect(WebUtility.INSTANCE.errorPageURL, request, response);
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String token = (String) request.getSession().getAttribute(WebUtility.INSTANCE.attribToken);
            String field = request.getParameter(fieldParam);
            String value = request.getParameter(valueParam);
            UserBean user;
            if (value != null && !value.isEmpty()) {
                 user = administrator.updateUser(token, field, value);
            } else {
                user = administrator.getUser(token);
            }
            request.setAttribute(WebUtility.INSTANCE.attribUser, user);
            request.getRequestDispatcher(accountPageURL).forward(request, response);
        } catch (HttpWebException e) {
            e.errorRedirect(WebUtility.INSTANCE.errorPageURL, request, response);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String jwt = (String) request.getSession().getAttribute(WebUtility.INSTANCE.attribToken);
        try {
            WebUtility.INSTANCE.getRequestSender().sendHttpDeleteRequest(jwt, accountUrl, null);
            request.getRequestDispatcher("/main/log-out").forward(request, response);
            response.sendError(400);
        } catch (HttpWebException e) {
            e.errorRedirect(WebUtility.INSTANCE.errorPageURL, request, response);
        }
    }


    private void chooseMethod(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String method = request.getParameter("method");
        if (method.equals("put")) {
            doPut(request, response);
        } else if (method.equals("delete")) {
            doDelete(request, response);
        } else {
            InvalidMethodException e = new InvalidMethodException(method);
            loggerKit.doLog(this.getClass().getSuperclass(), e, Level.SEVERE);
            new HttpWebException(HttpWebException.ERROR.NOT_ALLOWED).errorRedirect(WebUtility.INSTANCE.errorPageURL, request, response);
        }
    }
}