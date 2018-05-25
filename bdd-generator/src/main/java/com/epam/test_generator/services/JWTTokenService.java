package com.epam.test_generator.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.epam.test_generator.entities.User;
import java.io.UnsupportedEncodingException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
class JWTTokenService {

    private static final long REFRESH_TOKEN_DURATION_IN_HOURS = 1L;
    private static final long EXPIRING_TOKEN_DURATION_IN_HOURS = 24L;


    private static final String CLAIM_ID = "id";
    private static final String CLAIM_EMAIL = "email";
    private static final String CLAIM_GIVEN_NAME = "given_name";
    private static final String CLAIM_FAMILY_NAME = "family_name";
    private static final String CLAIM_ROLE = "role";
    private static final String CLAIM_REFRESH_AFTER = "refresh_at";

    private static final String JWT_SECRET_KEY = "jwt_secret";


    String createJwtTokenFor(User user, String issuer, Environment environment) {
        Date expiresDate = Date.from(Instant.now()
            .plus(EXPIRING_TOKEN_DURATION_IN_HOURS, ChronoUnit.HOURS));
        Date refreshDate = Date.from(Instant.now()
            .plus(REFRESH_TOKEN_DURATION_IN_HOURS, ChronoUnit.HOURS));
        JWTCreator.Builder builder = JWT.create()
                .withIssuer(issuer)
            .withClaim(CLAIM_ID, user.getId())
            .withClaim(CLAIM_EMAIL, user.getEmail())
            .withClaim(CLAIM_GIVEN_NAME, user.getName())
            .withClaim(CLAIM_FAMILY_NAME, user.getSurname())
            .withClaim(CLAIM_ROLE, user.getRole().getName())
            .withClaim(CLAIM_REFRESH_AFTER, refreshDate)
            .withExpiresAt(expiresDate);
        return signJWTToken(builder, environment);
    }

    private String signJWTToken(JWTCreator.Builder builder, Environment environment) {
        try {
            return builder.sign(Algorithm.HMAC256(environment.getProperty(JWT_SECRET_KEY)));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
