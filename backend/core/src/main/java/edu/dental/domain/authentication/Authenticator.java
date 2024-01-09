package edu.dental.domain.authentication;

import edu.dental.database.DatabaseException;
import edu.dental.database.DatabaseService;
import edu.dental.database.dao.UserDAO;
import edu.dental.domain.APIManager;
import edu.dental.entities.User;

import io.jsonwebtoken.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.time.ZoneId;
import java.util.Properties;

/**
 * Authenticate a user if such exists.
 */
public final class Authenticator {

    private Authenticator() {}


    /**
     * The user authentication in system.
     * @param login    The user's account login.
     * @param password The password to verify.
     * @return The {@link User} object, if authentication was successful.
     * @throws AuthenticationException Causes of throwing
     *  - specified user is not found;
     *  - Database exception;
     *  - incorrect password;
     *  - a given argument is null.
     */
    public static User authenticate(String login, String password) throws AuthenticationException {
        if ((login == null || login.isEmpty())||(password == null || password.isEmpty())) {
            throw new AuthenticationException(AuthenticationException.Causes.NULL);
        }
        User user;
        DatabaseService databaseService = DatabaseService.getInstance();
        try {
            user = databaseService.findUser(login);
        } catch (DatabaseException e) {
            throw new AuthenticationException(AuthenticationException.Causes.ERROR);
        }
        if (user == null) {
            throw new AuthenticationException(AuthenticationException.Causes.NO);
        }
        if (!(verification(user, password))) {
            throw new AuthenticationException(AuthenticationException.Causes.PASS);
        }
        return user;
    }

    public static User create(String name, String login, String password) throws AuthenticationException {
        User user = new User(name, login, password);
        try {
            if (APIManager.INSTANCE.getDatabaseService().getUserDAO().put(user)) {
                return user;
            } else throw new AuthenticationException(AuthenticationException.Causes.ERROR);
        } catch (DatabaseException e) {
            throw new AuthenticationException(AuthenticationException.Causes.ERROR);
        }
    }

    /**
     * Verification the user's password when logging in.
     * @param password The user's password.
     * @return The {@link User} object if verification was successful, or null if not.
     */
    public static boolean verification(User user, String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            return MessageDigest.isEqual(user.getPassword(), md.digest(password.getBytes()));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Verification the user's encoded {@link MessageDigest MD5} password bytes.
     * @param password The user's password, encoded MD5.
     * @return The {@link User} object if verification was successful, or null if not.
     */
    public static boolean verification(User user, byte[] password) {
        return MessageDigest.isEqual(user.getPassword(), password);
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
            //TODO logger
            throw new RuntimeException(e);
        }
    }

    public static User getUser(String jwt) throws AuthenticationException {
        try {
            int id = JwtUtils.getId(jwt);
            if (id <= 0) {
                throw new AuthenticationException("Incorrect token");
            } else {
                return DatabaseService.getInstance().getUserDAO().get(id);
            }
        } catch (JwtException | DatabaseException e) {
            throw new AuthenticationException(e.getMessage());
        }
    }

    public static boolean updatePassword(User user, String password) throws AuthenticationException {
        if (verification(user, user.getPassword())) {
            try {
                byte[] passHash = passwordHash(password);
                return DatabaseService.getInstance().getUserDAO().updatePassword(user.getId(), passHash);
            } catch (DatabaseException e) {
                throw new AuthenticationException(AuthenticationException.Causes.ERROR);
            }
        } else {
            throw new AuthenticationException(AuthenticationException.Causes.PASS);
        }
    }

    public static boolean updateUser(User user) throws AuthenticationException {
        UserDAO dao = DatabaseService.getInstance().getUserDAO();
        try {
            return dao.update(user);
        } catch (DatabaseException e) {
            throw new AuthenticationException(e.getMessage(), AuthenticationException.Causes.ERROR);
        }
    }

    public static void deleteUser(User user) throws AuthenticationException {
        UserDAO dao = DatabaseService.getInstance().getUserDAO();
        try {
            if (!dao.delete(user.getId())) {
                throw new AuthenticationException(AuthenticationException.Causes.ERROR);
            }
        } catch (DatabaseException e) {
            throw new AuthenticationException(AuthenticationException.Causes.ERROR);
        }
    }

    public static class JwtUtils {

        private JwtUtils() {}
        static {
            SECRET_KEY = loadProperties().getProperty("key");
        }

        private static final String PROP_PATH = "D:\\Development Java\\pet_projects\\dental_web_project\\backend\\core\\target\\classes\\secret_key.properties";
        private static final String SECRET_KEY;


        public static String generateJwtFromEntity(User user) {
            JwtBuilder jwtBuilder = Jwts.builder()
                    .setId(String.valueOf(user.getId()))
                    .setIssuedAt(Date.from(user.getCreated().atStartOfDay(ZoneId.systemDefault()).toInstant()))
                    .signWith(SignatureAlgorithm.HS256, SECRET_KEY);

            return jwtBuilder.compact();
        }

        public static Claims parseJwt(String jwt) {
            return Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(jwt)
                    .getBody();
        }

        public static int getId(String jwt) {
            try {
                return Integer.parseInt(parseJwt(jwt).getId());
            } catch (JwtException e) {
                //TODO loggers
                return 0;
            }
        }

        public static boolean isSigned(String jwt) {
            return Jwts.parser().isSigned(jwt);
        }

        private static Properties loadProperties() {
            try (FileInputStream fileInput = new FileInputStream(PROP_PATH)) {
                Properties prop = new Properties();
                prop.load(fileInput);
                return prop;
            } catch (IOException e) {
                //TODO loggers
                throw new RuntimeException(e);
            }
        }
    }
}