package com.epam.test_generator.services.exceptions;


public class NotFoundException extends RuntimeException {

    public NotFoundException() {
    }

    public NotFoundException(String message) {
        super(message);
    }
}
