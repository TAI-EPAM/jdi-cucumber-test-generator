package com.epam.test_generator.controllers.test.result.response;

import com.epam.test_generator.entities.Status;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class TestResultDTO {

    private LocalDate date;

    private Long duration;

    private Status status;

    private String executedBy;

    private int amountOfPassed;

    private int amountOfFailed;

    private int amountOfSkipped;

    private List<SuitResultDTO> suitResults;

    public TestResultDTO() {
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getExecutedBy() {
        return executedBy;
    }

    public void setExecutedBy(String executedBy) {
        this.executedBy = executedBy;
    }

    public int getAmountOfPassed() {
        return amountOfPassed;
    }

    public void setAmountOfPassed(int amountOfPassed) {
        this.amountOfPassed = amountOfPassed;
    }

    public int getAmountOfFailed() {
        return amountOfFailed;
    }

    public void setAmountOfFailed(int amountOfFailed) {
        this.amountOfFailed = amountOfFailed;
    }

    public int getAmountOfSkipped() {
        return amountOfSkipped;
    }

    public void setAmountOfSkipped(int amountOfSkipped) {
        this.amountOfSkipped = amountOfSkipped;
    }

    public List<SuitResultDTO> getSuitResults() {
        return suitResults;
    }

    public void setSuitResults(List<SuitResultDTO> suitResults) {
        this.suitResults = suitResults;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TestResultDTO that = (TestResultDTO) o;
        return amountOfPassed == that.amountOfPassed &&
            amountOfFailed == that.amountOfFailed &&
            amountOfSkipped == that.amountOfSkipped &&
            Objects.equals(date, that.date) &&
            Objects.equals(duration, that.duration) &&
            status == that.status &&
            Objects.equals(executedBy, that.executedBy) &&
            Objects.equals(suitResults, that.suitResults);
    }

    @Override
    public int hashCode() {

        return Objects
            .hash(date, duration, status, executedBy, amountOfPassed, amountOfFailed,
                amountOfSkipped,
                suitResults);
    }
}
