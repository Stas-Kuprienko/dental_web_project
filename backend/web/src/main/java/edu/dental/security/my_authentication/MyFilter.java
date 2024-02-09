package edu.dental.security.my_authentication;

import edu.dental.database.DatabaseException;
import edu.dental.security.AuthenticationService;
import edu.dental.security.IFilterVerification;
import edu.dental.security.WebSecurityException;
import edu.dental.service.Repository;
import jakarta.servlet.http.HttpServletRequest;

import java.util.logging.Level;

class MyFilter implements IFilterVerification {

    private static final String authorizationType = "Bearer ";
    private static final String authVar = "Authorization";
    private final Repository repository;
    private final AuthenticationService authenticationService;

    public MyFilter() {
        this.repository = Repository.getInstance();
        this.authenticationService = AuthenticationService.getInstance();
    }


    @Override
    public int verify(HttpServletRequest request) throws DatabaseException, WebSecurityException {
        String authorization = request.getHeader(authVar);

        if (authorization != null && authorization.startsWith(authorizationType)) {
            String jwt = authorization.substring(authorizationType.length());
            int userId = authenticationService.tokenUtils().getId(jwt);
            if (userId > 0) {
                repository.ensureLoggingIn(userId);
                repository.updateAccountLastAction(userId);
                return userId;
            }
        }
        throw new WebSecurityException(Level.WARNING, AuthenticationService.ERROR.FORBIDDEN, "invalid token");
    }
}