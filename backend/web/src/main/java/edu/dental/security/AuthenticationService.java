package edu.dental.security;

import edu.dental.WebAPI;
import edu.dental.WebException;
import edu.dental.domain.user.AccountException;
import edu.dental.dto.UserDto;
import edu.dental.entities.User;

public interface AuthenticationService {

    static AuthenticationService getInstance() {
        return WebAPI.INSTANCE.getAuthenticationService();
    }

    UserDto registration(String name, String email, String password) throws WebException;

    UserDto authorization(String login, String password) throws WebException;

    User authenticate(String login, String password) throws AccountException, WebException;

    boolean verification(User user, String password);

    boolean updatePassword(User user, String password) throws WebException;

    byte[] passwordHash(String password);

    UserDto getUserDto(String jwt) throws WebException;

    TokenUtils tokenUtils();
}