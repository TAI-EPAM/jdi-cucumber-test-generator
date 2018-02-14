package com.epam.test_generator.dto;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.bind.MethodArgumentNotValidException;


/**
 * This DTO is a list of {@Link ValidationErrorDTO}. It's used for retrieving and sending validation errors' information
 * respectively from and to API.
 */
public class ValidationErrorsDTO {

    private Boolean isError = true;
    private String type;
    private List<ValidationErrorDTO> errors;

    public ValidationErrorsDTO(MethodArgumentNotValidException ex) {
        type = "Method Argument Not Valid Exception";
        errors = ex.getBindingResult().getFieldErrors().stream()
            .map((f) -> new ValidationErrorDTO(f.getObjectName(), f.getField(),
                f.getDefaultMessage()))
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