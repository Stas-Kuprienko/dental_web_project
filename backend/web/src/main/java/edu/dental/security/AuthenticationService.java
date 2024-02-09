package edu.dental.security;

import edu.dental.WebAPI;
import edu.dental.database.DatabaseException;
import edu.dental.dto.UserDto;
import edu.dental.entities.User;

public interface AuthenticationService {

    static AuthenticationService getInstance() {
        return WebAPI.INSTANCE.getAuthenticationService();
    }

    UserDto registration(String name, String email, String password) throws DatabaseException;

    UserDto authorization(String login, String password) throws WebSecurityException, DatabaseException;

    User authenticate(String login, String password) throws WebSecurityException, DatabaseException;

    boolean verification(User user, String password);

    boolean updatePassword(User user, String password) throws DatabaseException;

    byte[] passwordHash(String password);

    TokenUtils tokenUtils();

    enum ERROR {

        SERVER_ERROR(500),
        BAD_REQUEST(400),
        UNAUTHORIZED(401),
        FORBIDDEN(403),
        NOT_FOUND(404);

        public final int code;
        ERROR(int code) {
            this.code = code;
        }
    }
}