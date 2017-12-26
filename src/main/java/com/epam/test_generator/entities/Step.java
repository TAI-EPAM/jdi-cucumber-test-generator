package com.epam.test_generator.entities;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Step implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    private int rowNumber;

    private String description;

    @Enumerated(EnumType.STRING)
    private StepType type;

    public Step() {
    }

    public Step(Long id, int rowNumber, String description, StepType type) {
        this.id = id;
        this.rowNumber = rowNumber;
        this.description = description;
        this.type = type;
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

    @Override
    public String toString() {
        return "Step{" +
            "id=" + id +
            ", rowNumber=" + rowNumber +
            ", description='" + description + '\'' +
            ", type=" + type +
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
            && (type == step.type);
    }
}
