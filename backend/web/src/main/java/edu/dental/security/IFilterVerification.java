package edu.dental.security;

import edu.dental.WebAPI;
import edu.dental.database.DatabaseException;
import jakarta.servlet.http.HttpServletRequest;

public interface IFilterVerification {

    static IFilterVerification getInstance() {
        return WebAPI.INSTANCE.getFilterVerification();
    }

    int verify(HttpServletRequest request) throws DatabaseException, WebSecurityException;
}
