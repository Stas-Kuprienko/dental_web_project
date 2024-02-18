package edu.dental.security.my_authentication;

import edu.dental.WebAPI;
import edu.dental.entities.User;
import edu.dental.security.AuthenticationService;
import edu.dental.security.TokenUtils;
import edu.dental.security.WebSecurityException;
import io.jsonwebtoken.*;

import java.sql.Date;
import java.time.ZoneId;
import java.util.logging.Level;

class JwtUtils implements TokenUtils {

    private final String SECRET_KEY;

    JwtUtils() {
        this.SECRET_KEY = WebAPI.getSecretKeyProperties().getProperty("key");
    }


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
}
