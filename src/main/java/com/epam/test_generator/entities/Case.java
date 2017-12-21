package com.epam.test_generator.entities;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import org.springframework.statemachine.annotation.WithStateMachine;

@Entity
@WithStateMachine
public class Case implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    private String description;

    @OneToMany(cascade = {CascadeType.ALL})
    private List<Step> steps;

    private String creationDate;

    private String updateDate;

    private Integer priority;

    private Status status;

    @OneToMany(cascade = {CascadeType.ALL})
    private Set<Tag> tags;

    public Case() {
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        creationDate = formatter.format(Calendar.getInstance().getTime());
        updateDate = formatter.format(Calendar.getInstance().getTime());
    }

    public Case(Long id, String description, List<Step> steps, Integer priority, Set<Tag> tags) {
        this();
        this.id = id;
        this.description = description;
        this.steps = steps;
        this.priority = priority;
        this.tags = tags;
    }

    public Case(String description, List<Step> steps, String creationDate, String updateDate,
                Integer priority, Set<Tag> tags, Status status) {
        this.description = description;
        this.steps = steps;
        this.creationDate = creationDate;
        this.updateDate = updateDate;
        this.priority = priority;
        this.tags = tags;

        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Step> getSteps() {
        if (steps == null) {
            steps = new ArrayList<>();
        }
        return steps;
    }

    public void addStep(Step step) {
        if (steps == null) {
            steps = new ArrayList<>();
        }

        steps.add(step);
    }

    public void addTag(Tag tag) {
        if (tags == null) {
            tags = new HashSet<>();
        }

        tags.add(tag);
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Case{" +
            "id=" + id +
            ", description='" + description + '\'' +
            ", steps=" + steps +
            ", creationDate='" + creationDate + '\'' +
            ", priority=" + priority +
            ", tags=" + tags +
            ", status" + status +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Case)) {
            return false;
        }

        Case aCase = (Case) o;

        return (id != null ? id.equals(aCase.id) : aCase.id == null)
            && (description != null ? description.equals(aCase.description)
            : aCase.description == null)
            && (steps != null ? steps.equals(aCase.steps) : aCase.steps == null)
            && (creationDate != null ? creationDate.equals(aCase.creationDate)
            : aCase.creationDate == null)
            && (updateDate != null ? updateDate.equals(aCase.updateDate) : aCase.updateDate == null)
            && (priority != null ? priority.equals(aCase.priority) : aCase.priority == null)
            && (status != null ? status.equals(aCase.status) : aCase.status == null)
            && (tags != null ? tags.equals(aCase.tags) : aCase.tags == null);
    }
}