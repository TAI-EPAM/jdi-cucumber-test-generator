package com.epam.test_generator.services.exceptions;

public class BadRoleException extends RuntimeException {
    public BadRoleException() {

    }
    public BadRoleException(String message) {
        super(message);
    }


}
