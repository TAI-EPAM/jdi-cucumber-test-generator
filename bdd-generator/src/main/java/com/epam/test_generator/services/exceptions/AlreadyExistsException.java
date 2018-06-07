package com.epam.test_generator.services.exceptions;

import com.epam.test_generator.controllers.GlobalExceptionController;
import com.epam.test_generator.services.utils.UtilsService;

/**
 * Exception that can be thrown from {@link UtilsService} or
 * Controllers test classes when trying to create something that is already exists
 * and can't has duplicates.
 * {@link GlobalExceptionController} catches this exception.
 */
public class AlreadyExistsException extends RuntimeException {

    public AlreadyExistsException() {
    }

    public AlreadyExistsException(String message) {
        super(message);
    }
}
