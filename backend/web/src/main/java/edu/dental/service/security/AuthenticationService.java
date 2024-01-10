package edu.dental.service.security;

import edu.dental.domain.account.AccountException;
import edu.dental.domain.account.AccountService;
import edu.dental.domain.authentication.AuthenticationException;
import edu.dental.domain.authentication.Authenticator;
import edu.dental.domain.records.WorkRecordBook;
import edu.dental.domain.records.WorkRecordBookException;
import edu.dental.dto.UserDto;
import edu.dental.entities.User;
import edu.dental.service.Repository;

public final class AuthenticationService {

    private AuthenticationService() {}


    public static UserDto registration(String name, String email, String password) throws AccountException {
        User user = AccountService.getInstance().create(name, email, password);
        String jwt = Authenticator.JwtUtils.generateJwtFromEntity(user);
        Repository.getInstance().put(user, WorkRecordBook.createNew(user.getId()));
        return new UserDto(user.getId(), user.getName(), user.getEmail(), user.getCreated().toString(), jwt);
    }

    public static UserDto authorization(String login, String password) throws AuthenticationException {
        User user = Authenticator.authenticate(login, password);
        String jwt = Authenticator.JwtUtils.generateJwtFromEntity(user);
        try {
            Repository.getInstance().put(user, WorkRecordBook.getInstance(user.getId()));
        } catch (WorkRecordBookException e) {
            throw new AuthenticationException(AuthenticationException.Causes.ERROR);
        }
        return new UserDto(user.getId(), user.getName(), user.getEmail(), user.getCreated().toString(), jwt);
    }

    public static int verification(String jwt) {
        return Authenticator.JwtUtils.getId(jwt);
    }

    public static boolean updatePassword(User user, String password) throws AuthenticationException {
            byte[] passHash = Authenticator.passwordHash(password);
            user.setPassword(passHash);
            return true;
    }

    public static User getUser(String jwt) throws AuthenticationException {
        return Authenticator.getUser(jwt);
    }

    public static UserDto getUserDto(String jwt) throws AuthenticationException {
        User user = Authenticator.getUser(jwt);
        return new UserDto(user.getId(), user.getName(), user.getEmail(), user.getCreated().toString(), jwt);
    }
}
