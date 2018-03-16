package com.epam.test_generator.dto;

import com.epam.test_generator.entities.Status;

import java.util.Date;
import java.util.List;

public class TestResultDTO {

    private Date date;

    private long duration;

    private Status status;

    private String executedBy;

    private int amountOfPassed;

    private int amountOfFailed;

    private int amountOfSkipped;

    private List<SuitResultDTO> suits;

    public TestResultDTO() {
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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

    public List<SuitResultDTO> getSuits() {
        return suits;
    }

    public void setSuits(List<SuitResultDTO> suits) {
        this.suits = suits;
    }

}
