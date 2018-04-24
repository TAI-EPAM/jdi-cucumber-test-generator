package com.epam.test_generator.controllers.step.request;

import com.epam.test_generator.entities.Status;
import com.epam.test_generator.entities.StepType;
import java.util.Objects;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class StepCreateDTO {
    @NotNull
    @Size(min = 1, max = 255)
    private String description;

    @NotNull
    private StepType type;

    @Size(min = 1, max = 255)
    private String comment;

    @NotNull
    private Status status;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public StepType getType() {
        return type;
    }

    public void setType(StepType type) {
        this.type = type;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
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
        StepCreateDTO that = (StepCreateDTO) o;
        return Objects.equals(description, that.description) &&
            type == that.type &&
            Objects.equals(comment, that.comment) &&
            status == that.status;
    }

    @Override
    public int hashCode() {

        return Objects.hash(description, type, comment, status);
    }
}
