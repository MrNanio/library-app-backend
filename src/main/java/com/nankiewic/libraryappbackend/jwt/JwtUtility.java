package com.nankiewic.libraryappbackend.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.nankiewic.libraryappbackend.dto.UserJwtDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

@Slf4j
public class JwtUtility {

    private static final String JWT_SECRET = "secret";
    private static final String ISSUER = "library-app";
    private static final int JWT_EXPIRATION_TIME = 600000; //milliseconds -> 10 minutes

    public static String generateJwtToken(UserDetails userDetails) {

        Date currentDate = new Date();
        Date expirationDate = new Date(currentDate.getTime() + JWT_EXPIRATION_TIME);
        String token = null;
        try {
            Algorithm algorithm = Algorithm.HMAC256(JWT_SECRET);
            token = JWT.create()
                    .withIssuer(ISSUER)
                    .withSubject(userDetails.getUsername())
                    .withClaim("role", userDetails.getAuthorities().toString())
                    .withExpiresAt(expirationDate)
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            //Invalid Signing configuration / Couldn't convert Claims.
        }
        return token;
    }

    public static String getSubject(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getSubject();
        } catch (JWTDecodeException exception) {
            return null;
        }
    }

    public static UserJwtDTO getUser(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);

            Collection<SimpleGrantedAuthority> authority = new ArrayList<>();
            authority.add(new SimpleGrantedAuthority(jwt.getClaims().get("role").toString()));

            return UserJwtDTO.builder()
                    .email(jwt.getSubject())
                    .authority(authority)
                    .build();

        } catch (JWTDecodeException exception) {
            return null;
        }
    }

    public static boolean tokenVerify(String token) {
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(JWT_SECRET))
                    .withIssuer(ISSUER)
                    .build();
            verifier.verify(token);
            return true;
        } catch (JWTVerificationException exception) {
            log.error("token is invalid");
            return false;
        }
    }
}
