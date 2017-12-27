package com.epam.test_generator.services.exceptions;


import org.springframework.security.core.AuthenticationException;

public class JwtTokenMissingException extends
    AuthenticationException {

    public JwtTokenMissingException(String s) {
        super(s);
    }
}
