package com.epam.test_generator.services.exceptions;

import com.epam.test_generator.controllers.GlobalExceptionController;
import com.epam.test_generator.services.AdminService;

/**
 * Exception that can be thrown when {@link AdminService} API user tries to get role which
 * doesn't exist. {@link GlobalExceptionController} catches this exception.
 */
public class BadRoleException extends RuntimeException {
    public BadRoleException() {

    }
    public BadRoleException(String message) {
        super(message);
    }


}
