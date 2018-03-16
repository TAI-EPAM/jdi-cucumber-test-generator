package com.epam.test_generator.dto;

import com.epam.test_generator.entities.Status;
import java.util.List;

public class SuitResultDTO {

    private String name;
    private long duration;
    private Status status;
    private List<CaseResultDTO> caseResults;

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

    public List<CaseResultDTO> getCaseResults() {
        return caseResults;
    }

    public void setCaseResults(List<CaseResultDTO> caseResults) {
        this.caseResults = caseResults;
    }
}
