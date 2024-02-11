package edu.dental.security.my_authentication;

import edu.dental.entities.User;
import edu.dental.security.AuthenticationService;
import edu.dental.security.TokenUtils;
import edu.dental.security.WebSecurityException;
import io.jsonwebtoken.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Date;
import java.time.ZoneId;
import java.util.Properties;
import java.util.logging.Level;

class JwtUtils implements TokenUtils {

    JwtUtils() {
    }

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
    public int getId(String jwt) throws WebSecurityException {
        try {
            return Integer.parseInt(parseJwt(jwt).getId());
        } catch (JwtException e) {
            throw new WebSecurityException(Level.SEVERE, AuthenticationService.ERROR.SERVER_ERROR, e);
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
            throw new RuntimeException
                    (new WebSecurityException(Level.SEVERE, AuthenticationService.ERROR.SERVER_ERROR, e));
        }
    }
}
