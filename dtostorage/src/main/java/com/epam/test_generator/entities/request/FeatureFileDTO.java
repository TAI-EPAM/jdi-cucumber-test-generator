package com.epam.test_generator.entities.request;

import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class FeatureFileDTO {

    @NotNull
    private Long suitId;

    @NotEmpty
    private List<Long> caseIds;

    public FeatureFileDTO() {
    }

    public Long getSuitId() {
        return suitId;
    }

    public void setSuitId(Long suitId) {
        this.suitId = suitId;
    }

    public List<Long> getCaseIds() {
        return caseIds;
    }

    public void setCaseIds(List<Long> caseIds) {
        this.caseIds = caseIds;
    }
}
