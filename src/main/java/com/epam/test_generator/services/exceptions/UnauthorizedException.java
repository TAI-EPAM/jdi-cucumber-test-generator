package com.epam.test_generator.services.exceptions;

import org.springframework.security.core.AuthenticationException;

public class UnauthorizedException extends AuthenticationException {

    public UnauthorizedException(String s) {
        super(s);
    }
}
