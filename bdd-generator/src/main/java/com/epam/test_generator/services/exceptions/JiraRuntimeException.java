package com.epam.test_generator.services.exceptions;

public class JiraRuntimeException extends RuntimeException {

    public JiraRuntimeException(String message) {
        super(message);
    }

    public JiraRuntimeException(String message, Exception e) {
        super(message,e);
    }
}
