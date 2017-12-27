package com.epam.test_generator.config.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

/**
 * This class is just wrapper for our JWT token to transfer it between classes.
 */
public class JwtAuthenticationToken extends UsernamePasswordAuthenticationToken {

    private String token;

    public JwtAuthenticationToken(String token) {
        super("", "");

        this.token = token;

    }

    public String getToken() {
        return token;
    }
}

