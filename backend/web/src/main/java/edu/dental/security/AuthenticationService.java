package edu.dental.security;

import edu.dental.WebException;
import edu.dental.domain.account.AccountException;
import edu.dental.domain.account.AccountService;
import edu.dental.domain.records.WorkRecordBook;
import edu.dental.domain.records.WorkRecordBookException;
import edu.dental.dto.UserDto;
import edu.dental.entities.User;
import edu.dental.service.Repository;

import io.jsonwebtoken.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Date;
import java.time.ZoneId;
import java.util.Properties;

public final class AuthenticationService {

    private AuthenticationService() {}


    public static UserDto registration(String name, String email, String password) throws WebException {
        User user;
        try {
            user = AccountService.getInstance().create(name, email, password);
            String jwt = JwtUtils.generateJwtFromEntity(user);
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
        String jwt = JwtUtils.generateJwtFromEntity(user);
        try {
            Repository.getInstance().put(user, WorkRecordBook.getInstance(user.getId()));
        } catch (WorkRecordBookException e) {
            throw new WebException(e.getMessage(), WebException.CODE.SERVER_ERROR);
        }
        return new UserDto(user.getId(), user.getName(), user.getEmail(), user.getCreated().toString(), jwt);
    }

    public static int verification(String jwt) {
        return JwtUtils.getId(jwt);
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
            int id = JwtUtils.getId(jwt);
            if (id > 0) {
                return AccountService.getInstance().get(id);
            } else {
                throw new WebException(WebException.CODE.UNAUTHORIZED);
            }
        } catch (AccountException e) {
            throw new WebException(e.getMessage(), WebException.CODE.FORBIDDEN);
        }
    }

    public static UserDto getUserDto(String jwt) throws WebException {
        User user;
        try {
            int id = JwtUtils.getId(jwt);
            if (id > 0) {
                user = AccountService.getInstance().get(id);
                return new UserDto(user.getId(), user.getName(), user.getEmail(), user.getCreated().toString(), jwt);
            } else {
                throw new WebException(WebException.CODE.UNAUTHORIZED);
            }
        } catch (AccountException e) {
            throw new WebException(e.getMessage(), WebException.CODE.FORBIDDEN);
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
