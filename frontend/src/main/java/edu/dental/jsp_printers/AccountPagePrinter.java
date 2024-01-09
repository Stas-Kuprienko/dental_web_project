package edu.dental.jsp_printers;

import edu.dental.WebAPI;
import edu.dental.beans.UserDto;
import edu.dental.service.WebRepository;
import jakarta.servlet.http.HttpServletRequest;

import static edu.dental.jsp_printers.HtmlTag.*;

@SuppressWarnings("unused")
public class AccountPagePrinter {

    private static final String href = "account";

    private final UserDto user;

    public AccountPagePrinter(HttpServletRequest request) {
        int userId = (int) request.getSession().getAttribute(WebAPI.INSTANCE.sessionUser);
        this.user = WebRepository.INSTANCE.getUser(userId);
    }

    public UserDto user() {
        return user;
    }

    public String value(String value) {
        return String.format(WORK_VIEW.FORM_INPUT_TEXT_VALUE.sample, value);
    }
}
