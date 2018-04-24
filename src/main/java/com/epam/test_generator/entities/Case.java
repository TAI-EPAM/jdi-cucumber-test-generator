package com.epam.test_generator.entities;

import com.epam.test_generator.entities.api.CaseTrait;
import com.epam.test_generator.entities.api.JiraSuitAndCaseTrait;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import org.springframework.data.domain.Persistable;
import org.springframework.statemachine.annotation.WithStateMachine;


/**
 * This class represents Test case essence. Test case is a set of actions that are used for checking
 * some software's behavior. Case consists of some simple fields like id of the case, it's name,
 * description, history information, result of testing and etc, also it includes sequence of steps
 * and tags. List of {@link Step} objects represents steps that must be done for verification within
 * current case. List of {@Link Tag} objects represents types of current case.
 */
@Entity
@WithStateMachine
public class Case implements Serializable, Persistable<Long>, CaseTrait, JiraSuitAndCaseTrait {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private String jiraKey;

    private String jiraProjectKey;

    private String jiraParentKey;

    private ZonedDateTime lastModifiedDate;

    private ZonedDateTime lastJiraSyncDate;


    @OneToMany(cascade = {CascadeType.ALL})
    private List<Step> steps;

    private ZonedDateTime creationDate;

    private ZonedDateTime updateDate;

    private Integer priority;

    private Integer rowNumber;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<Tag> tags;

    private String comment;

    public Case() {
        creationDate = ZonedDateTime.now();
        updateDate = creationDate;
    }


    public Case(Long id, String name, String description, List<Step> steps,
                Integer priority, Set<Tag> tags, String comment) {
        this();
        this.id = id;
        this.name = name;
        this.description = description;
        this.steps = steps;
        this.priority = priority;
        this.tags = tags;
        this.comment = comment;
    }

    public Case(String name, String description, List<Step> steps, ZonedDateTime creationDate,
                ZonedDateTime updateDate,
                Integer priority, Set<Tag> tags, Status status, String comment) {
        this.name = name;
        this.description = description;
        this.steps = steps;
        this.creationDate = creationDate;
        this.updateDate = updateDate;
        this.priority = priority;
        this.tags = tags;
        this.comment = comment;
        this.status = status;
    }

    public Case(String name, String description, List<Step> steps,
                Integer priority, Set<Tag> tags, Status status, String comment) {
        this();
        this.name = name;
        this.description = description;
        this.steps = steps;
        this.priority = priority;
        this.tags = tags;
        this.comment = comment;
        this.status = status;
    }

    @Override
    public Long getId() {
        return id;
    }

    /**
     * Override in order entity manager use merge instead of persist when call with case id = null
     * and one of tag ids is not null. <br/> For example, with standard behaviour spring data jpa
     * will call persist on this request {"id":null,"description":"4","name":"4","priority":4,"tags":[{"id":10,
     * "name":"soap"}]} and it will cause "detached entity passed to persist" error.
     */
    @Override
    public boolean isNew() {
        boolean isAllTagsWithNullId = tags == null
            || tags.stream().allMatch(tag -> tag.getId() == null);

        return id == null && isAllTagsWithNullId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public List<Step> getSteps() {
        if (steps == null) {
            steps = new ArrayList<>();
        }
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    public void addStep(Step step) {
        if (steps == null) {
            steps = new ArrayList<>();
        }
        step.setRowNumber(steps
            .stream()
            .map(Step::getRowNumber)
            .max(Comparator.naturalOrder())
            .orElse(0)
            + 1
        );
        steps.add(step);
    }

    public void addTag(Tag tag) {
        if (tags == null) {
            tags = new HashSet<>();
        }

        tags.add(tag);
    }

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public ZonedDateTime getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(ZonedDateTime updateDate) {
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String getJiraKey() {
        return jiraKey;
    }

    public void setJiraKey(String jiraKey) {
        this.jiraKey = jiraKey;
    }

    public String getJiraProjectKey() {
        return jiraProjectKey;
    }

    public void setJiraProjectKey(String jiraProjectKey) {
        this.jiraProjectKey = jiraProjectKey;
    }

    public String getJiraParentKey() {
        return jiraParentKey;
    }

    public void setJiraParentKey(String jiraParentKey) {
        this.jiraParentKey = jiraParentKey;
    }

    public ZonedDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(ZonedDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public ZonedDateTime getLastJiraSyncDate() {
        return lastJiraSyncDate;
    }

    public void setLastJiraSyncDate(ZonedDateTime lastJiraSyncDate) {
        this.lastJiraSyncDate = lastJiraSyncDate;
    }

    public Integer getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(Integer rowNumber) {
        this.rowNumber = rowNumber;
    }

    @Override
    public String toString() {
        return String.format(
            "Case{ id= %s ,name= %s, description= %s, steps= %s, creationDate= %s, " +
                "priority= %s, tags= %s, status= %s, comment= %s, rowNumber=%s};",
            id, name, description, steps, creationDate, priority, tags, status, comment, rowNumber);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Case aCase = (Case) o;
        return Objects.equals(id, aCase.id) &&
            Objects.equals(name, aCase.name) &&
            Objects.equals(description, aCase.description) &&
            Objects.equals(jiraKey, aCase.jiraKey) &&
            Objects.equals(jiraProjectKey, aCase.jiraProjectKey) &&
            Objects.equals(jiraParentKey, aCase.jiraParentKey) &&
            Objects.equals(steps, aCase.steps) &&
            Objects.equals(priority, aCase.priority) &&
            status == aCase.status &&
            Objects.equals(tags, aCase.tags) &&
            Objects.equals(comment, aCase.comment) &&
            Objects.equals(rowNumber, aCase.rowNumber);
    }

    @Override
    public int hashCode() {

        return Objects
            .hash(id, name, description, jiraKey, jiraProjectKey, jiraParentKey, steps, priority,
                status, tags, comment, rowNumber);
    }
}