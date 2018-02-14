package com.epam.test_generator.services.exceptions;

import com.epam.test_generator.controllers.GlobalExceptionController;
import com.epam.test_generator.services.CaseService;
import com.epam.test_generator.services.SuitService;
import com.epam.test_generator.services.utils.UtilsService;

/**
 * Exception that can be thrown when {@link CaseService}, {@link SuitService} or {@link UtilsService} API user tries to use
 * these services methods with empty or malformed parameters. {@link GlobalExceptionController} catches this exception.
 */
public class BadRequestException extends RuntimeException {

    public BadRequestException() {
    }

    public BadRequestException(String message) {
        super(message);
    }
}
