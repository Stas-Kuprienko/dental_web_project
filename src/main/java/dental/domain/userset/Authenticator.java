package dental.domain.userset;

import dental.database.service.DBAgent;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Authenticate {@link Account} if such exists.
 */
public class Authenticator {

    private Account account;

    public Authenticator(String login, String password) throws AuthenticationException {
        if (!authenticate(login, password)) {
            throw new AuthenticationException();
        }
    }

    /**
     * The user {@link Account account} logging in system.
     * @param login    The user's account login.
     * @param password The password to verify.
     * @return True if authentication was successful.
     */
    private boolean authenticate(String login, String password) {
        if ((login == null || login.isEmpty())||(password == null || password.isEmpty())) {
            return false;
        }
        Account a = DBAgent.authenticate(login, password);
        if (a == null) {
            return false;
        }
        if (!(verification(account, password.getBytes()))) {
            return false;
        }
        this.account = a;
        return true;
    }

    /**
     * Verification the user's password when logging in.
     * @param password The user's password.
     * @return The {@link Account} object if verification was successful, or null if not.
     */
    private boolean verification(Account account, byte[] password) {
        return MessageDigest.isEqual(account.getPassword(), password);
    }

    /**
     * Get the {@link MessageDigest MD5} hash of the password.
     * @param password The user's password string.
     * @return byte array of the password hash.
     */
    public static byte[] passwordHash(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            return md.digest(password.getBytes());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException();
        }
    }

    public Account getAccount() {
        return account;
    }

}