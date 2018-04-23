package com.epam.test_generator.controllers.test.result.response;

import com.epam.test_generator.entities.Status;
import java.util.List;
import java.util.Objects;

public class CaseResultDTO {

    private String name;
    private long duration;
    private Status status;
    private String comment;
    private List<StepResultDTO> stepResults;

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

    public List<StepResultDTO> getStepResults() {
        return stepResults;
    }

    public void setStepResults(List<StepResultDTO> stepResults) {
        this.stepResults = stepResults;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CaseResultDTO that = (CaseResultDTO) o;
        return duration == that.duration &&
            Objects.equals(name, that.name) &&
            status == that.status &&
            Objects.equals(comment, that.comment) &&
            Objects.equals(stepResults, that.stepResults);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name, duration, status, comment, stepResults);
    }
}
