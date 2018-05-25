package com.epam.test_generator.services.exceptions;

import com.epam.test_generator.controllers.GlobalExceptionController;
import com.epam.test_generator.services.utils.UtilsService;

/**
 * Exception that can be thrown from {@link UtilsService} or
 * Controllers test classes when no data can be found for requested parameters.
 * {@link GlobalExceptionController} catches this exception.
 */
public class NotFoundException extends RuntimeException {

    public NotFoundException() {
    }

    public NotFoundException(String message) {
        super(message);
    }
}
