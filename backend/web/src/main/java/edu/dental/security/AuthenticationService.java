package edu.dental.security;

import edu.dental.WebAPI;
import edu.dental.WebException;
import edu.dental.dto.UserDto;
import edu.dental.entities.User;
import io.jsonwebtoken.Claims;

public interface AuthenticationService {

    static AuthenticationService getInstance() {
        return WebAPI.INSTANCE.getAuthenticationService();
    }

    UserDto registration(String name, String email, String password) throws WebException;

    UserDto authorization(String login, String password) throws WebException;

    int verification(String jwt);

    boolean updatePassword(User user, String password) throws WebException;

    User getUser(String jwt) throws WebException;

    UserDto getUserDto(String jwt) throws WebException;

    TokenUtils tokenUtils();

    IFilterVerification filterService();

    interface TokenUtils {

        String generateJwtFromEntity(User user);

        Claims parseJwt(String jwt);

        int getId(String jwt);

        boolean isSigned(String jwt);
    }
}