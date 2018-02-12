package com.epam.test_generator.dto;

import com.epam.test_generator.entities.Action;
import com.epam.test_generator.entities.Status;
import com.epam.test_generator.entities.StepType;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class StepDTO {

    private Long id;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Action action;

    @NotNull
    @Min(value = 1)
    private int rowNumber;

    @NotNull
    @Size(min = 1, max = 255)
    private String description;

    @NotNull
    private StepType type;

    private String comment;

    @NotNull
    private Status status;

    public StepDTO() {
    }

    public StepDTO(Long id, int rowNumber, String description, StepType type, String comment, Status status) {
        this.id = id;
        this.rowNumber = rowNumber;
        this.description = description;
        this.type = type;
        this.comment = comment;
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

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
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
    public String toString() {
        return "StepDTO{" +
            "id=" + id +
            ", action=" + action +
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
        if (!(o instanceof StepDTO)) {
            return false;
        }

        StepDTO stepDTO = (StepDTO) o;

        return (rowNumber == stepDTO.rowNumber) &&
            (id != null ? id.equals(stepDTO.id) : stepDTO.id == null)
            && (description != null ? description.equals(stepDTO.description)
            : stepDTO.description == null)
            && (comment != null ? comment.equals(stepDTO.comment) : stepDTO.comment == null)
            && (status != null ? status.equals(stepDTO.status) : stepDTO.status == null)
            && (type != null ? type.equals(stepDTO.type) : stepDTO.type == null);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + rowNumber;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (comment != null ? comment.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        return result;
    }
}