package edu.dental.security.my_authentication;

import edu.dental.WebException;
import edu.dental.domain.user.AccountException;
import edu.dental.entities.User;
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
            int userId = verification(jwt);
            if (userId > 0) {
                if (isLoggedIn(userId)) {
                    repository.updateAccountLastAction(userId);
                    return userId;
                } else {
                    if (addToRepository(jwt)) {
                        return userId;
                    }
                }
            }
        }
        throw new WebException(AccountException.CAUSE.FORBIDDEN, AccountException.MESSAGE.TOKEN_INVALID);
    }

    private int verification(String jwt) {
        return authenticationService.tokenUtils().getId(jwt);
    }

    private boolean isLoggedIn(int userId) {
        return repository.get(userId) != null;
    }

    private boolean addToRepository(String jwt) throws WebException {
        User user = authenticationService.getUser(jwt);
        return repository.put(user);
    }
}
