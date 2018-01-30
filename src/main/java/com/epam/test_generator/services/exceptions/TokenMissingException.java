package com.epam.test_generator.services.exceptions;


import org.springframework.security.core.AuthenticationException;

public class TokenMissingException extends
    AuthenticationException {

    public TokenMissingException(String s) {
        super(s);
    }
}
