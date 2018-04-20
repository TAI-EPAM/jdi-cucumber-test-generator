package com.epam.test_generator.entities.results;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

@Entity
public class SuitResult extends AbstractResult implements ResultTrait {

    private String name;

    @OneToMany(cascade = {CascadeType.ALL})
    private List<CaseResult> caseResults;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CaseResult> getCaseResults() {
        return caseResults;
    }

    public void setCaseResults(List<CaseResult> caseResults) {
        this.caseResults = caseResults;
        setStatus(calculateStatus(caseResults));
        setDuration(calculateDuration(caseResults));
    }
}
