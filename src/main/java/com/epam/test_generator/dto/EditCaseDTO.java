package com.epam.test_generator.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

public class EditCaseDTO {

    @Size(min = 1, max = 255)
    private String description;

    @Min(value = 1)
    @Max(value = 5)
    private Integer priority;

    public EditCaseDTO() {
    }

    public EditCaseDTO(String description, Integer priority) {
        this.description = description;
        this.priority = priority;
    }

    public String getDescription() {
        return description;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }
}
