package com.epam.test_generator.entities;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class TestResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    private Date date;

    private Long duration;

    @Enumerated(EnumType.STRING)
    private Status status;

    private String executedBy;

    private int amountOfPassed;

    private int amountOfFailed;

    private int amountOfSkipped;

    public TestResult() {
    }

    @ManyToOne
    private Project project;

    @OneToMany(cascade = {CascadeType.ALL})
    private List<SuitResult> suits;

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

    public void setProject(Project project) {
        this.project = project;
    }

    public List<SuitResult> getSuits() {
        return suits;
    }

    public void setSuits(List<SuitResult> suits) {
        this.suits = suits;
    }

    public Project getProject() {
        return project;
    }
}
