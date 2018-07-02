package com.epam.admin_client.service.exception;

public class JiraException extends RuntimeException {

    public JiraException(String message) {
        super(message);
    }

    public JiraException(Throwable cause) {
        super(cause);
    }

    public JiraException(String message, Throwable cause) {
        super(message, cause);
    }
}
