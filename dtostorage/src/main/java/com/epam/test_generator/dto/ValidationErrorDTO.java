package com.epam.test_generator.dto;


/**
 * This DTO is practically the same as {@link ErrorDTO}, but have information about only fields validation errors
 * (problems of unsuitable types and formats of fields values). It is used only in {@link ValidationErrorsDTO}.
 */
public class ValidationErrorDTO {

    private String type;
    private String field;
    private String message;

    public ValidationErrorDTO(String objectName, String field, String defaultMessage) {
        this.type = objectName;
        this.field = field;
        this.message = defaultMessage;
    }

    public ValidationErrorDTO() {
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
