package edu.dental.security.my_authentication;

import edu.dental.database.DatabaseException;
import edu.dental.database.DatabaseService;
import edu.dental.database.dao.UserDAO;
import edu.dental.dto.UserDto;
import edu.dental.entities.User;
import edu.dental.security.AuthenticationService;
import edu.dental.security.TokenUtils;
import edu.dental.security.WebSecurityException;
import edu.dental.service.Repository;

import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;


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
    public UserDto registration(String name, String email, String password) throws DatabaseException {
        User user;
        byte[] passHashByte = MD5.digest(password.getBytes());
        user = repository.createNew(name, email, passHashByte);
        return new UserDto(user);
    }

    @Override
    public UserDto authorization(String login, String password) throws DatabaseException, WebSecurityException {
        User user = authenticate(login, password);
        if (repository.put(user)) {
            return new UserDto(user);
        } else {
            throw new WebSecurityException(Level.INFO, ERROR.SERVER_ERROR, new Exception("user is not put in repository"));
        }
    }

    /**
     * The user authentication in system.
     * @param login    The user's account login.
     * @param password The password to verify.
     * @return The {@link User} object, if authentication was successful.
     * @throws WebSecurityException Causes of throwing
     *  - specified user is not found;
     *  - incorrect password;
     *  - a given argument is null.
     * @throws DatabaseException if troubles with database connection.
     */
    @Override
    public User authenticate(String login, String password) throws WebSecurityException, DatabaseException {
        if ((login == null || login.isEmpty())||(password == null || password.isEmpty())) {
            throw new WebSecurityException(Level.INFO, ERROR.BAD_REQUEST, new NullPointerException("argument is null"));
        }
        User user;
        try {
            user = userDAO.search(login).get(0);
        } catch (NullPointerException e) {
            throw new WebSecurityException(Level.INFO, ERROR.NOT_FOUND, e);
        }
        if (!verification(user, password)) {
            throw new WebSecurityException(Level.INFO, ERROR.UNAUTHORIZED, new InvalidKeyException("password is invalid"));
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
    public boolean updatePassword(User user, String password) throws DatabaseException {
        byte[] oldValue = user.getPassword();
        byte[] newPassword = passwordHash(password);
        try {
            user.setPassword(newPassword);
            return userDAO.update(user);
        } catch (DatabaseException e) {
            user.setPassword(oldValue);
            throw e;
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