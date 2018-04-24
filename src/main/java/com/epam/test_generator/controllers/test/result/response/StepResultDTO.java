package com.epam.test_generator.controllers.test.result.response;

import java.util.Objects;

public class StepResultDTO {

    private String description;

    private String status;

    public StepResultDTO() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StepResultDTO that = (StepResultDTO) o;
        return Objects.equals(description, that.description) &&
            status == that.status;
    }

    @Override
    public int hashCode() {

        return Objects.hash(description, status);
    }
}
