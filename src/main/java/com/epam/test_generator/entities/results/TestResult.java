package com.epam.test_generator.entities.results;

import com.epam.test_generator.entities.Project;
import java.time.ZonedDateTime;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class TestResult extends AbstractResult implements ResultTrait {

    private ZonedDateTime date;

    private String executedBy;

    private int amountOfPassed;

    private int amountOfFailed;

    private int amountOfSkipped;

    public TestResult() {
        date = ZonedDateTime.now();
    }

    @ManyToOne
    private Project project;

    @OneToMany(cascade = {CascadeType.ALL})
    private List<SuitResult> suitResults;

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
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

    public List<SuitResult> getSuitResults() {
        return suitResults;
    }

    public void setSuitResults(List<SuitResult> suitResults) {
        this.suitResults = suitResults;
        countTestResultStatistics(suitResults);
        setStatus(calculateStatus(suitResults));
        setDuration(calculateDuration(suitResults));
    }

    public Project getProject() {
        return project;
    }

    /**
     * Summarize amount of Passed, Skipped and Failed tests of Tests executions.
     *
     * @param results list of {@link AbstractResult}
     */
    private void countTestResultStatistics(List<? extends AbstractResult> results) {
        int amountOfPassed = 0;
        int amountOfSkipped = 0;
        int amountOfFailed = 0;

        for (AbstractResult suitResult : results) {
            switch (suitResult.getStatus()) {
                case PASSED:
                    amountOfPassed++;
                    break;
                case SKIPPED:
                    amountOfSkipped++;
                    break;
                case FAILED:
                    amountOfFailed++;
                    break;
            }
        }

        setAmountOfPassed(amountOfPassed);
        setAmountOfFailed(amountOfFailed);
        setAmountOfSkipped(amountOfSkipped);
    }

}
