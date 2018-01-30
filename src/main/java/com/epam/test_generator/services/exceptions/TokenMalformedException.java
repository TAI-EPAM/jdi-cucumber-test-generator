package com.epam.test_generator.services.exceptions;

import org.springframework.security.core.AuthenticationException;

public class TokenMalformedException extends
    AuthenticationException {

    public TokenMalformedException(String s) {
        super(s);
    }
}
