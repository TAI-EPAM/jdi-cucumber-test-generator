package com.epam.test_generator.entities;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


/**
 * This class represents step essence. Step means one of the actions that should be done within the relative test case.
 * Step consist of simple fields: id, number, description of action and {@Link StepType} which describe type of action
 * in test case.
 */
@Entity
public class Step implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    private int rowNumber;

    private String description;

    @Enumerated(EnumType.STRING)
    private StepType type;

    @Enumerated(EnumType.STRING)
    private Status status;

    private String comment;

    public Step() {
    }

    public Step(Long id, int rowNumber, String description, StepType type, String comment, Status status) {
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Step{" +
            "id=" + id +
            ", rowNumber=" + rowNumber +
            ", description='" + description + '\'' +
            ", type=" + type +
            ", status=" + status +
            ", comment='" + comment + '\'' +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Step)) {
            return false;
        }

        Step step = (Step) o;

        return rowNumber == step.rowNumber
            && (id != null ? id.equals(step.id) : step.id == null)
            && (description != null ? description.equals(step.description)
            : step.description == null)
            && (comment != null ? comment.equals(step.comment) : step.comment == null)
            && (status != null ? status.equals(step.status) : step.status == null)
            && (type == step.type);
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
