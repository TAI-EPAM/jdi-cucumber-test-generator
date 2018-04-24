package com.epam.test_generator.controllers.test.result.response;

import java.util.List;
import java.util.Objects;

public class SuitResultDTO {

    private String name;
    private long duration;
    private String status;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<CaseResultDTO> getCaseResults() {
        return caseResults;
    }

    public void setCaseResults(List<CaseResultDTO> caseResults) {
        this.caseResults = caseResults;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SuitResultDTO that = (SuitResultDTO) o;
        return duration == that.duration &&
            Objects.equals(name, that.name) &&
            status == that.status &&
            Objects.equals(caseResults, that.caseResults);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name, duration, status, caseResults);
    }
}
