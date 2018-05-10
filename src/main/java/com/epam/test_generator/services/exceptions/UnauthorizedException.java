package com.epam.test_generator.services.exceptions;

import com.epam.test_generator.controllers.GlobalExceptionController;
import org.springframework.security.core.AuthenticationException;
import com.epam.test_generator.services.AdminService;
import com.epam.test_generator.services.LoginService;
import com.epam.test_generator.services.UserService;

/**
 * Exception that can be thrown when {@link AdminService}, {@link LoginService},
 * {@link UserService} or {@link JwtAuthenticationProvider} API user tries
 * to perform operations on users which don't exist or are blocked.
 * {@link GlobalExceptionController} catches this exception.
 */
public class UnauthorizedException extends AuthenticationException {

    public UnauthorizedException(String s) {
        super(s);
    }
}
