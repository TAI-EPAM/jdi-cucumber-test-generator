package com.epam.test_generator.dto;

public class ErrorDTO {

    private Boolean isError = true;
    private String message;

    public ErrorDTO(Throwable throwable) {
        this.message = throwable.getMessage();
    }

    public ErrorDTO(String message) {
        this.message = message;
    }

    public Boolean getError() {
        return isError;
    }

    public String getMessage() {
        return message;
    }
}
