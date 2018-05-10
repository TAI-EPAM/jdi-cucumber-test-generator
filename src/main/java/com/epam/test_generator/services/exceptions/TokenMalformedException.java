package com.epam.test_generator.services.exceptions;

import com.epam.test_generator.controllers.GlobalExceptionController;
import org.springframework.security.core.AuthenticationException;
import com.epam.test_generator.services.TokenService;

/**
 * Exception that can be thrown when {@link JwtAuthenticationProvider} or {@link TokenService} API user
 * tries to retrieve a user with a wrong authentication token or when token has expired.
 * {@link GlobalExceptionController} catches this exception.
 */
public class TokenMalformedException extends AuthenticationException {

    public TokenMalformedException(String s) {
        super(s);
    }
}
