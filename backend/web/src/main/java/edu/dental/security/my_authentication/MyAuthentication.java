package edu.dental.security.my_authentication;

import edu.dental.WebException;
import edu.dental.database.DatabaseException;
import edu.dental.database.DatabaseService;
import edu.dental.database.dao.UserDAO;
import edu.dental.service.AccountException;
import edu.dental.dto.UserDto;
import edu.dental.entities.User;
import edu.dental.security.AuthenticationService;
import edu.dental.security.TokenUtils;
import edu.dental.service.Repository;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class MyAuthentication implements AuthenticationService {

    private final JwtUtils jwtUtils;
    private final UserDAO userDAO;
    private final Repository repository;
    private final MessageDigest MD5;


    private MyAuthentication() {
        try {
            this.MD5 = MessageDigest.getInstance("MD5");
            this.jwtUtils = new JwtUtils();
            this.userDAO = DatabaseService.getInstance().getUserDAO();
            this.repository = Repository.getInstance();
        } catch (NoSuchAlgorithmException e) {

            throw new RuntimeException(e);
        }
    }


    @Override
    public UserDto registration(String name, String email, String password) throws SecurityException {
        User user;
        try {
            byte[] passHashByte = MD5.digest(password.getBytes());
            user = repository.createNew(name, email, passHashByte);
            return new UserDto(user);
        } catch (AccountException e) {
            throw new SecurityException(e);
        }
    }

    @Override
    public UserDto authorization(String login, String password) throws WebException {
        User user = authenticate(login, password);
        repository.put(user);
        return new UserDto(user);
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
    public User authenticate(String login, String password) throws WebException, SecurityException {
        if ((login == null || login.isEmpty())||(password == null || password.isEmpty())) {
            throw new SecurityException("argument is null");
        }
        User user;
        try {
            user = userDAO.search(login).get(0);
        } catch (DatabaseException e) {
            if (e.getMessage().equals("The such object is not found.")) {
                throw new WebException(WebException.CODE.NOT_FOUND);
            }
            throw new SecurityException(e);
        }
        if (!verification(user, password)) {
            throw new WebException(WebException.CODE.UNAUTHORIZED);
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
    public boolean updatePassword(User user, String password) throws SecurityException {
        byte[] oldValue = user.getPassword();
        byte[] newPassword = passwordHash(password);
        try {
            user.setPassword(newPassword);
            return userDAO.update(user);
        } catch (DatabaseException e) {
            user.setPassword(oldValue);
            throw new SecurityException(e);
        }
    }

    @Override
    public byte[] passwordHash(String password) {
        return MD5.digest(password.getBytes());
    }

    @Override
    public TokenUtils tokenUtils() {
        return jwtUtils;
    }
}