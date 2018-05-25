package com.epam.test_generator.controllers.step.response;

import com.epam.test_generator.entities.Status;
import com.epam.test_generator.entities.StepType;
import java.util.Objects;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class StepDTO {

    private Long id;

    @NotNull
    @Min(value = 1)
    private int rowNumber;

    @NotNull
    @Size(min = 1, max = 255)
    private String description;

    @NotNull
    private StepType type;

    @Size(min = 1, max = 255)
    private String comment;

    @NotNull
    private String status;

    public StepDTO() {
    }

    public StepDTO(Long id, int rowNumber, String description, StepType type, String comment, String status) {
        this.id = id;
        this.rowNumber = rowNumber;
        this.description = description;
        this.type = type;
        this.comment = comment;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(int rowNumber) {
        this.rowNumber = rowNumber;
    }

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

    public String getDisplayedStatusName() {
        return status;
    }

    public void setDisplayedStatusName(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "StepDTO{" +
            "id=" + id +
            ", rowNumber=" + rowNumber +
            ", description='" + description + '\'' +
            ", type=" + type +
            ", comment='" + comment + '\'' +
            ", status=" + status +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StepDTO stepDTO = (StepDTO) o;
        return rowNumber == stepDTO.rowNumber &&
            Objects.equals(id, stepDTO.id) &&
            Objects.equals(description, stepDTO.description) &&
            type == stepDTO.type &&
            Objects.equals(comment, stepDTO.comment) &&
            status == stepDTO.status;
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, rowNumber, description, type, comment, status);
    }
}