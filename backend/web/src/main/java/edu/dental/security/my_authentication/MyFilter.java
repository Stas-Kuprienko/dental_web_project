package edu.dental.security.my_authentication;

import edu.dental.WebException;
import edu.dental.service.AccountException;
import edu.dental.security.AuthenticationService;
import edu.dental.security.IFilterVerification;
import edu.dental.service.Repository;
import jakarta.servlet.http.HttpServletRequest;

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
    public int verify(HttpServletRequest request) throws WebException {
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
        throw new WebException(AccountException.CAUSE.FORBIDDEN, AccountException.MESSAGE.TOKEN_INVALID);
    }
}