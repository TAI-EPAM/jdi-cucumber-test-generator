package com.epam.test_generator.services.exceptions;

import com.epam.test_generator.controllers.GlobalExceptionController;
import org.springframework.security.core.AuthenticationException;
import com.epam.test_generator.services.TokenService;
import com.epam.test_generator.services.PasswordService;

/**
 * Exception that can be thrown when {@link JwtAuthenticationProvider}, {@link PasswordService} or
 * {@link TokenService} API user tries to perform authentication operations without
 * authentication token. {@link GlobalExceptionController} catches this exception.
 */
public class TokenMissingException extends AuthenticationException {

    public TokenMissingException(String s) {
        super(s);
    }
}
