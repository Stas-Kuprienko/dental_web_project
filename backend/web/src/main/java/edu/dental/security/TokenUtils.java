package edu.dental.security;

import edu.dental.entities.User;
import io.jsonwebtoken.Claims;

public interface TokenUtils {

    String generateJwtFromEntity(User user);

    Claims parseJwt(String jwt);

    int getId(String jwt) throws WebSecurityException;

    boolean isSigned(String jwt);
}
