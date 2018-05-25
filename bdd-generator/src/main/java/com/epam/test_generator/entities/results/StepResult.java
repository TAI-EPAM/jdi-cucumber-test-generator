package com.epam.test_generator.entities.results;

import javax.persistence.Entity;
import javax.persistence.Transient;

@Entity
public class StepResult extends AbstractResult {

    @Transient
    private long duration;

    private String description;

    public StepResult() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
