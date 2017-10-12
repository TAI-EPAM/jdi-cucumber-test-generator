package com.epam.test_generator.dto;

import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.stream.Collectors;

public class ValidationErrorsDTO {

    private Boolean isError;
    private String type;
    private List<ValidationErrorDTO> errors;


    public ValidationErrorsDTO(MethodArgumentNotValidException ex) {
        isError = true;
        type = "Method Argument Not Valid Exception";

        errors = ex.getBindingResult().getFieldErrors().stream()
                .map((f)->
                        new ValidationErrorDTO(f.getObjectName(),f.getField(),f.getDefaultMessage()))
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "ValidationErrorsDTO{" +
                "isError=" + isError +
                ", type='" + type + '\'' +
                ", errors=" + errors +
                '}';
    }
}