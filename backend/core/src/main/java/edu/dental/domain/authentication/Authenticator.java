package edu.dental.domain.authentication;

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

    public static int verifyTokenToId(String jwt) throws AuthenticationException {
        int id = JwtUtils.getId(jwt);
        if (id <= 0) {
            //TODO
            throw new AuthenticationException("");
        } else {
            return id;
        }
    }

    public static void updatePassword(User user, String password) throws AuthenticationException {
        if (verification(user, user.getPassword())) {
            byte[] passHash = passwordHash(password);
            user.setPassword(passHash);
        } else {
            throw new AuthenticationException(AuthenticationException.Causes.PASS);
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