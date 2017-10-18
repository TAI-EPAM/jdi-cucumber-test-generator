package com.epam.test_generator.dto;

import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.stream.Collectors;

public class ValidationErrorsDTO {

    private Boolean isError = true;
    private String type;
    private List<ValidationErrorDTO> errors;

    public ValidationErrorsDTO(MethodArgumentNotValidException ex) {
        type = "Method Argument Not Valid Exception";
        errors = ex.getBindingResult().getFieldErrors().stream()
                .map((f) -> new ValidationErrorDTO(f.getObjectName(), f.getField(), f.getDefaultMessage()))
                .collect(Collectors.toList());
    }

    public Boolean getError() {
        return isError;
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