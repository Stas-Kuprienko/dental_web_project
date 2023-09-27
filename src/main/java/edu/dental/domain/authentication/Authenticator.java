package edu.dental.domain.authentication;

import edu.dental.database.mysql_api.MyDBService;
import edu.dental.domain.entities.User;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Authenticate a user if such exists.
 */
public final class Authenticator {

    private Authenticator() {}


    /**
     * The user logging in system.
     * @param login    The user's account login.
     * @param password The password to verify.
     * @return True if authentication was successful.
     */
    private static synchronized User authenticate(String login, String password) throws AuthenticationException {
        if ((login == null || login.isEmpty())||(password == null || password.isEmpty())) {
            throw new AuthenticationException(AuthenticationException.Causes.NULL);
        }
        User user = MyDBService.getInstance().authenticate(login, password);
        if (user == null) {
            throw new AuthenticationException(AuthenticationException.Causes.NO);
        }
        if (!(verification(user, password.getBytes()))) {
            throw new AuthenticationException(AuthenticationException.Causes.PASS);
        }
        return user;
    }

    /**
     * Verification the user's password when logging in.
     * @param password The user's password.
     * @return The {@link User} object if verification was successful, or null if not.
     */
    private static synchronized boolean verification(User user, byte[] password) {
        return MessageDigest.isEqual(user.getPassword(), password);
    }

    /**
     * Get the {@link MessageDigest MD5} hash of the password.
     * @param password The user's password string.
     * @return byte array of the password hash.
     */
    public static synchronized byte[] passwordHash(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            return md.digest(password.getBytes());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}