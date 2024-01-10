package edu.dental.jsp_printers;

import edu.dental.WebUtility;
import edu.dental.beans.UserDto;
import edu.dental.service.WebRepository;
import jakarta.servlet.http.HttpServletRequest;

@SuppressWarnings("unused")
public class AccountPagePrinter {

    private final UserDto user;

    public AccountPagePrinter(HttpServletRequest request) {
        int userId = (int) request.getSession().getAttribute(WebUtility.INSTANCE.sessionUser);
        this.user = WebRepository.INSTANCE.getUser(userId);
    }

    public UserDto user() {
        return user;
    }
}
