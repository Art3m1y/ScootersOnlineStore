package ru.Art3m1y.shop.utils.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Date;

@Component
public class JWTUtil {
    private String secretKeyForAccess = "ASLJHD123IAD8123I";
    private String secretKeyForRefresh = "ASLJHD123IAMSDK";
    private String subject = "Identification details";
    private String issuer = "Art3m1y";
    private Algorithm signAccessToken = Algorithm.HMAC256(secretKeyForAccess);
    private Algorithm signRefreshToken = Algorithm.HMAC256(secretKeyForRefresh);
    private JWTVerifier verifierForAccessToken = JWT.require(signAccessToken)
            .withIssuer(issuer)
            .withSubject(subject)
            .build();
    private JWTVerifier verifierForRefreshToken = JWT.require(signRefreshToken)
            .withIssuer(issuer)
            .withSubject(subject)
            .build();

    public String generateAccessToken(long id, String name, String surname, String email, String role) {
        return JWT.create()
                .withSubject(subject)
                .withExpiresAt(Date.from(ZonedDateTime.now().plusMinutes(30).toInstant()))
                .withIssuedAt(new Date())
                .withIssuer(issuer)
                .withClaim("id", id)
                .withClaim("name", name)
                .withClaim("surname", surname)
                .withClaim("email", email)
                .withClaim("role", role)
                .sign(signAccessToken);
    }

    public String generateRefreshToken(long id) {
        return JWT.create()
                .withSubject(subject)
                .withExpiresAt(Date.from(ZonedDateTime.now().plusDays(3).toInstant()))
                .withIssuedAt(new Date())
                .withIssuer(issuer)
                .withClaim("id", id)
                .sign(signRefreshToken);
    }

    public boolean verifyAccessToken(String token) {
        try {
            verifierForAccessToken.verify(token);
            return true;
        } catch (JWTVerificationException e) {
            System.out.println("Invalid access JWT token");
            return false;
        }
    }

    public boolean verifyRefreshToken(String token) {
        try {
            verifierForRefreshToken.verify(token);
            return true;
        } catch (JWTVerificationException e) {
            System.out.println("Invalid refresh JWT token");
            return false;
        }
    }

    public String getEmailFromAccessToken(String token) {
        return verifierForAccessToken.verify(token).getClaim("email").asString();
    }

    public String getEmailFromRefreshToken(String token) {
        return verifierForRefreshToken.verify(token).getClaim("email").asString();
    }

    public long getIdFromRefreshToken(String token) {
        return verifierForRefreshToken.verify(token).getClaim("id").asLong();
    }

    public String getRoleFromRefreshToken(String token) {
        return verifierForRefreshToken.verify(token).getClaim("role").asString();
    }

}
