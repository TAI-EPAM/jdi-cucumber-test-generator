package com.epam.test_generator.entities.results;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

@Entity
public class CaseResult extends AbstractResult implements ResultTrait {

    private String name;

    private String comment;
    @OneToMany(cascade = {CascadeType.ALL})
    private List<StepResult> steps;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<StepResult> getSteps() {
        return steps;
    }

    public void setSteps(List<StepResult> steps){
        this.steps = steps;
    }
}
