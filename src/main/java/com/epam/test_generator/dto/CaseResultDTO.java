package com.epam.test_generator.dto;

import com.epam.test_generator.entities.Status;
import java.util.List;

public class CaseResultDTO {

    private String name;
    private long duration;
    private Status status;
    private String comment;
    private List<StepResultDTO> steps;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<StepResultDTO> getSteps() {
        return steps;
    }

    public void setSteps(List<StepResultDTO> steps) {
        this.steps = steps;
    }

}
