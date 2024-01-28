package edu.dental.security.my_authentication;

import edu.dental.WebException;
import edu.dental.domain.user.AccountException;
import edu.dental.domain.user.UserService;
import edu.dental.dto.UserDto;
import edu.dental.entities.User;
import edu.dental.security.AuthenticationService;
import edu.dental.security.TokenUtils;
import edu.dental.service.Repository;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class MyAuthentication implements AuthenticationService {

    private final JwtUtils jwtUtils;
    private final UserService userService;
    private final Repository repository;
    private final MessageDigest MD5;


    private MyAuthentication() {
        try {
            this.MD5 = MessageDigest.getInstance("MD5");
            this.jwtUtils = new JwtUtils();
            this.userService = UserService.getInstance();
            this.repository = Repository.getInstance();
        } catch (NoSuchAlgorithmException e) {

            throw new RuntimeException(e);
        }
    }


    @Override
    public UserDto registration(String name, String email, String password) throws WebException {
        User user;
        try {
            byte[] passHashByte = MD5.digest(password.getBytes());
            user = userService.create(name, email, passHashByte);
            repository.createNew(user);
            return new UserDto(user);
        } catch (AccountException e) {
            throw new WebException(e.cause, e);
        }
    }

    @Override
    public UserDto authorization(String login, String password) throws WebException {
        User user = authenticate(login, password);
        if (repository.put(user)) {
            return new UserDto(user);
        } else {
            throw new WebException(AccountException.CAUSE.SERVER_ERROR, AccountException.MESSAGE.DATABASE_ERROR);
        }
    }

    /**
     * The user authentication in system.
     * @param login    The user's account login.
     * @param password The password to verify.
     * @return The {@link User} object, if authentication was successful.
     * @throws WebException Causes of throwing
     *  - specified user is not found;
     *  - Database exception;
     *  - incorrect password;
     *  - a given argument is null.
     */
    @Override
    public User authenticate(String login, String password) throws WebException {
        if ((login == null || login.isEmpty())||(password == null || password.isEmpty())) {
            throw new WebException(AccountException.CAUSE.BAD_REQUEST, new NullPointerException("argument is null"));
        }
        User user;
        try {
            user = userService.findUser(login);
        } catch (AccountException e) {
            throw new WebException(e.cause, e);
        }
        if (user == null) {
            throw new WebException(AccountException.CAUSE.NOT_FOUND, AccountException.MESSAGE.USER_NOT_FOUND);
        }
        if (!verification(user, password)) {
            throw new WebException(AccountException.CAUSE.UNAUTHORIZED, AccountException.MESSAGE.PASSWORD_INVALID);
        }
        return user;
    }

    /**
     * Verification the user's password when logging in.
     * @param password The user's password.
     * @return The {@link User} object if verification was successful, or null if not.
     */
    @Override
    public boolean verification(User user, String password) {
        return MessageDigest.isEqual(user.getPassword(), MD5.digest(password.getBytes()));
    }

    @Override
    public boolean updatePassword(User user, String password) throws WebException {
        byte[] oldValue = user.getPassword();
        byte[] newPassword = passwordHash(password);
        try {
            user.setPassword(newPassword);
            return userService.update(user);
        } catch (AccountException e) {
            user.setPassword(oldValue);
            throw new WebException(AccountException.CAUSE.SERVER_ERROR, e);
        }
    }

    @Override
    public byte[] passwordHash(String password) {
        return MD5.digest(password.getBytes());
    }

    @Override
    public UserDto getUserDto(String jwt) throws WebException {
        User user;
        try {
            int id = jwtUtils.getId(jwt);
            if (id > 0) {
                user = userService.get(id);
                return new UserDto(user);
            } else {
                throw new WebException(AccountException.CAUSE.FORBIDDEN, AccountException.MESSAGE.TOKEN_INVALID);
            }
        } catch (AccountException e) {
            throw new WebException(e.cause, e);
        }
    }

    @Override
    public TokenUtils tokenUtils() {
        return jwtUtils;
    }
}