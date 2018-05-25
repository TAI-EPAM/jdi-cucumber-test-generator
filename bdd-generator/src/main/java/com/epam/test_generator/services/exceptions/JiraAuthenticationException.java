package com.epam.test_generator.services.exceptions;

import org.springframework.security.core.AuthenticationException;

public class JiraAuthenticationException extends AuthenticationException {

    public JiraAuthenticationException(String msg) {
        super(msg);
    }
}
