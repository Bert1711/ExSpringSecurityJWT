package com.zaroyan.exspringsecurityjwt.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Date;


/**
 * @author Zaroyan
 */

@Slf4j
@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    public String generateToken(String username) {
        Date expirationDate = Date.from(ZonedDateTime.now().plusMinutes(60).toInstant());

        return JWT.create()
                .withSubject("User details")
                .withClaim("username", username)
                .withIssuedAt(new Date())
                .withIssuer("zaroyan")
                .withExpiresAt(expirationDate)
                .sign(Algorithm.HMAC256(secret));
    }

    public String validateTokenAndRetrieveClaim(String token) throws JWTVerificationException {

        Algorithm algorithm = Algorithm.HMAC256(secret);
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer("zaroyan")
                .build();
        DecodedJWT decodedJWT = verifier.verify(token);
        return decodedJWT.getClaim("username").asString();
    }


//    public List<String> getRoles(String token) {
//        return getAllClaimsFromToken(token).get("roles", List.class);
//    }
}



//public String generateToken(UserDetails userDetails) {
//    String username = userDetails.getUsername();
//    Date expirationDate = Date.from(ZonedDateTime.now().plusMinutes(jwtLifetime).toInstant());
//
//    return Jwts.create()
//            .withSubject("User details")
//            .withClaim("login", username)
//            .withIssuedAt(new Date())
//            .withIssuer("zaroyan")
//            .withExpiresAt(expirationDate)
//            .sign(Algorithm.HMAC256(secret));
//}
//
//public String validateTokenAndRetrieveClaim(String token) throws JWTVerificationException {
//
//    JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
//            .withSubject("User details")
//            .withIssuer("zaroyan")
//            .build();
//    DecodedJWT jwt = verifier.verify(token);
//    log.info("прошли валидацию токена");
//    return jwt.getClaim("login").asString();
//}





//        List<String> rolesList = userDetails.getAuthorities().stream()
//                .map(GrantedAuthority::getAuthority)
//                .collect(Collectors.toList());
//        claims.put("roles", rolesList);
