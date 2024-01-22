package edu.dental.security.my_authentication;

import edu.dental.WebException;
import edu.dental.domain.account.AccountException;
import edu.dental.domain.account.AccountService;
import edu.dental.domain.records.WorkRecordBook;
import edu.dental.domain.records.WorkRecordBookException;
import edu.dental.dto.UserDto;
import edu.dental.entities.User;
import edu.dental.security.AuthenticationService;
import edu.dental.security.IFilterVerification;
import edu.dental.service.Repository;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Date;
import java.time.ZoneId;
import java.util.Properties;


public class MyAuthentication implements AuthenticationService {


    private MyAuthentication() {
        this.jwtUtils = new JwtUtils();
        this.filter = new MyFilter();
        this.accountService = AccountService.getInstance();
        this.repository = Repository.getInstance();
    }

    private final JwtUtils jwtUtils;
    private final MyFilter filter;
    private final AccountService accountService;
    private final Repository repository;


    @Override
    public UserDto registration(String name, String email, String password) throws WebException {
        User user;
        try {
            user = accountService.create(name, email, password);
            repository.put(user, WorkRecordBook.createNew(user.getId()));
            return new UserDto(user);
        } catch (AccountException e) {
            throw new WebException(e.cause, e);
        }
    }

    @Override
    public UserDto authorization(String login, String password) throws WebException {
        User user;
        try {
            user = accountService.authenticate(login, password);
        } catch (AccountException e) {
            throw new WebException(e.cause, e);
        }
        try {
            repository.put(user, WorkRecordBook.getInstance(user.getId()));
        } catch (WorkRecordBookException e) {
            throw new WebException(AccountException.CAUSE.BAD_REQUEST, e);
        }
        return new UserDto(user);
    }

    @Override
    public int verification(String jwt) {
        return jwtUtils.getId(jwt);
    }

    @Override
    public boolean updatePassword(User user, String password) throws WebException {
        try {
            accountService.updatePassword(user, password);
            return true;
        } catch (AccountException e) {
            throw new WebException(e.cause, e);
        }
    }

    @Override
    public User getUser(String jwt) throws WebException {
        try {
            int id = jwtUtils.getId(jwt);
            if (id > 0) {
                return accountService.get(id);
            } else {
                throw new WebException(AccountException.CAUSE.FORBIDDEN, AccountException.MESSAGE.TOKEN_INVALID);
            }
        } catch (AccountException e) {
            throw new WebException(e.cause, e);
        }
    }

    @Override
    public UserDto getUserDto(String jwt) throws WebException {
        User user;
        try {
            int id = jwtUtils.getId(jwt);
            if (id > 0) {
                user = accountService.get(id);
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

    @Override
    public IFilterVerification filterService() {
        return filter;
    }

    private static class JwtUtils implements AuthenticationService.TokenUtils {

        private JwtUtils() {}

        static {
            SECRET_KEY = loadProperties().getProperty("key");
        }

        private static final String PROP_PATH = "D:\\Development Java\\pet_projects\\dental_web_project\\backend\\core\\target\\classes\\secret_key.properties";
        private static final String SECRET_KEY;


        @Override
        public String generateJwtFromEntity(User user) {
            JwtBuilder jwtBuilder = Jwts.builder()
                    .setId(String.valueOf(user.getId()))
                    .setIssuedAt(Date.from(user.getCreated().atStartOfDay(ZoneId.systemDefault()).toInstant()))
                    .signWith(SignatureAlgorithm.HS256, SECRET_KEY);

            return jwtBuilder.compact();
        }

        @Override
        public Claims parseJwt(String jwt) {
            return Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(jwt)
                    .getBody();
        }

        @Override
        public int getId(String jwt) {
            try {
                return Integer.parseInt(parseJwt(jwt).getId());
            } catch (JwtException e) {
                //TODO loggers
                return 0;
            }
        }

        @Override
        public boolean isSigned(String jwt) {
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

    private class MyFilter implements IFilterVerification {

        private static final String authorizationType = "Bearer ";
        private static final String authVar = "Authorization";

        @Override
        public int verify(HttpServletRequest request) throws WebException {
            String authorization = request.getHeader(authVar);

            if (authorization != null && authorization.startsWith(authorizationType)) {
                String jwt = authorization.substring(authorizationType.length());
                int userId = verification(jwt);
                if (userId > 0) {
                    if (isLoggedIn(userId)) {
                        repository.updateAccountLastAction(userId);
                        return userId;
                    } else {
                        if (addToRepository(jwt)) {
                            return userId;
                        }
                    }
                }
            } throw new WebException(AccountException.CAUSE.FORBIDDEN, AccountException.MESSAGE.TOKEN_INVALID);
        }

        private boolean isLoggedIn(int userId) {
            return repository.get(userId) != null;
        }

        private boolean addToRepository(String jwt) throws WebException {
            User user = getUser(jwt);
            return repository.put(user);
        }
    }
}