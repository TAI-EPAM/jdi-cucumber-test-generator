package com.epam.test_generator.services.exceptions;

import com.epam.test_generator.controllers.GlobalExceptionController;
import com.epam.test_generator.services.PasswordService;

/**
 * Exception that can be thrown when {@link PasswordService} API user tries to form an incorrect
 * URI. {@link GlobalExceptionController} catches this exception.
 */
public class IncorrectURI extends RuntimeException {

    public IncorrectURI() {
    }

    public IncorrectURI(String message) {
        super(message);
    }
}
