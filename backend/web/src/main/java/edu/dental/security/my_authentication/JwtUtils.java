package edu.dental.security.my_authentication;

import edu.dental.entities.User;
import edu.dental.security.TokenUtils;
import io.jsonwebtoken.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Date;
import java.time.ZoneId;
import java.util.Properties;

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
