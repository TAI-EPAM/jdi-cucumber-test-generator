package com.epam.test_generator.dto;

import java.util.List;


/**
 * This DTO is a list of {@link ValidationErrorDTO}. It's used for retrieving and sending validation errors' information
 * respectively from and to API.
 */
public class ValidationErrorsDTO {

    private Boolean isError = true;
    private String type;
    private List<ValidationErrorDTO> errors;

    public ValidationErrorsDTO() {
    }

    public Boolean getError() {
        return isError;
    }

    public void setError(Boolean error) {
        isError = error;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<ValidationErrorDTO> getErrors() {
        return errors;
    }

    public void setErrors(List<ValidationErrorDTO> errors) {
        this.errors = errors;
    }
}