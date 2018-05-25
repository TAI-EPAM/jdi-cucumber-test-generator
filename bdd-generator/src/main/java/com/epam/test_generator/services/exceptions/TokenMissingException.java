package com.epam.test_generator.services.exceptions;

import com.epam.test_generator.config.security.JwtAuthenticationFilter;
import com.epam.test_generator.controllers.GlobalExceptionController;
import com.epam.test_generator.services.PasswordService;
import com.epam.test_generator.services.TokenService;
import org.springframework.security.core.AuthenticationException;

/**
 * Exception that can be thrown when {@link JwtAuthenticationFilter}, {@link PasswordService} or
 * {@link TokenService} API user tries to perform authentication operations without
 * authentication token. {@link GlobalExceptionController} catches this exception.
 */
public class TokenMissingException extends AuthenticationException {

    public TokenMissingException(String s) {
        super(s);
    }
}
