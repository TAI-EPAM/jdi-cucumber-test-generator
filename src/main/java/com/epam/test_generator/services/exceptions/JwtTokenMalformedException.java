package com.epam.test_generator.services.exceptions;

import org.springframework.security.core.AuthenticationException;

public class JwtTokenMalformedException extends
    AuthenticationException {

    public JwtTokenMalformedException(String s) {
        super(s);
    }
}
