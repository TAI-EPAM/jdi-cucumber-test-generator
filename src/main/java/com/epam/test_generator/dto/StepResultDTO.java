package com.epam.test_generator.dto;

import com.epam.test_generator.entities.Status;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class StepResultDTO {

    private String description;

    private Status status;

    public StepResultDTO() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

}
