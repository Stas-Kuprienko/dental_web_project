package edu.dental.service.security;

import edu.dental.domain.account.AccountException;
import edu.dental.domain.account.AccountService;
import edu.dental.domain.authentication.Authenticator;
import edu.dental.domain.records.WorkRecordBook;
import edu.dental.domain.records.WorkRecordBookException;
import edu.dental.dto.UserDto;
import edu.dental.entities.User;
import edu.dental.service.Repository;
import edu.dental.service.WebException;

public final class AuthenticationService {

    private AuthenticationService() {}


    public static UserDto registration(String name, String email, String password) throws WebException {
        User user;
        try {
            user = AccountService.getInstance().create(name, email, password);
            String jwt = Authenticator.JwtUtils.generateJwtFromEntity(user);
            Repository.getInstance().put(user, WorkRecordBook.createNew(user.getId()));
            return new UserDto(user.getId(), user.getName(), user.getEmail(), user.getCreated().toString(), jwt);
        } catch (AccountException e) {
            throw new WebException(e.getMessage(), WebException.CODE.SERVER_ERROR);
        }
    }

    public static UserDto authorization(String login, String password) throws WebException {
        User user;
        try {
            user = AccountService.getInstance().get(login, password);
        } catch (AccountException e) {
            throw new WebException(e.getMessage(), WebException.CODE.BAD_REQUEST);
        }
        String jwt = Authenticator.JwtUtils.generateJwtFromEntity(user);
        try {
            Repository.getInstance().put(user, WorkRecordBook.getInstance(user.getId()));
        } catch (WorkRecordBookException e) {
            throw new WebException(e.getMessage(), WebException.CODE.SERVER_ERROR);
        }
        return new UserDto(user.getId(), user.getName(), user.getEmail(), user.getCreated().toString(), jwt);
    }

    public static int verification(String jwt) {
        return Authenticator.JwtUtils.getId(jwt);
    }

    public static boolean updatePassword(User user, String password) throws WebException {
        try {
            AccountService.getInstance().updatePassword(user, password);
            return true;
        } catch (AccountException e) {
            throw new WebException(e.getMessage(), WebException.CODE.BAD_REQUEST);
        }
    }

    public static User getUser(String jwt) throws WebException {
        try {
            return AccountService.getInstance().get(jwt);
        } catch (AccountException e) {
            //TODO
            throw new WebException(e.getMessage(), WebException.CODE.SERVER_ERROR);
        }
    }

    public static UserDto getUserDto(String jwt) throws WebException {
        User user;
        try {
            user = AccountService.getInstance().get(jwt);
        } catch (AccountException e) {
            throw new WebException(e.getMessage(), WebException.CODE.FORBIDDEN);
        }
        return new UserDto(user.getId(), user.getName(), user.getEmail(), user.getCreated().toString(), jwt);
    }
}
