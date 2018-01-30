package com.epam.test_generator.services.exceptions;

public class IncorrectURI extends RuntimeException {

    public IncorrectURI() {
    }

    public IncorrectURI(String message) {
        super(message);
    }
}
