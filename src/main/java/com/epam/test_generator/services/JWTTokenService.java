package com.epam.test_generator.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.epam.test_generator.entities.User;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
public class JWTTokenService {
    public String createJwtTokenFor(User user, String issuer, Environment environment) {
        JWTCreator.Builder builder = JWT.create()
                .withIssuer(issuer)
                .withClaim("id", user.getId())
                .withClaim("email", user.getEmail())
                .withClaim("given_name", user.getName())
                .withClaim("family_name", user.getSurname())
                .withClaim("role", user.getRole().getName());
        return signJWTToken(builder, environment);
    }

    private String signJWTToken(JWTCreator.Builder builder, Environment environment) {
        try {
            return builder.sign(Algorithm.HMAC256(environment.getProperty("jwt_secret")));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
