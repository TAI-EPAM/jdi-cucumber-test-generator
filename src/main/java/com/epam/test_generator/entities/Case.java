package com.epam.test_generator.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
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

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;


/**
 * This class represents Test case essence. Test Case is a set of actions that are used for checking some
 * software's behavior. Case consists of some simple fields like id of the case, it's name, description,
 * history information, result of testing and etc, also it includes sequence of steps and tags. List of
 * {@link Step} objects represents steps that must be done for verification within current case. List of
 * {@Link Tag} objects represents types of current case.
 */
@Entity
@WithStateMachine
public class Case implements Serializable, Persistable<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private String jiraKey;

    private String jiraProjectKey;

    private String jiraParentKey;

    private LocalDateTime lastModifiedDate;

    private LocalDateTime lastJiraSyncDate;


    @OneToMany(cascade = {CascadeType.ALL})
    private List<Step> steps;

    private Date creationDate;

    private Date updateDate;

    private Integer priority;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<Tag> tags;

    private String comment;

    public Case() {
        creationDate = Calendar.getInstance().getTime();
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

    public Case(String name, String description, List<Step> steps, Date creationDate,
                Date updateDate,
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
     * and one of tag ids is not null.
     * <br/>
     * For example, with standard behaviour spring data jpa will call persist on this request
     * {"id":null,"description":"4","name":"4","priority":4,"tags":[{"id":10, "name":"soap"}]}
     * and it will cause "detached entity passed to persist" error.
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

        steps.add(step);
    }

    public void addTag(Tag tag) {
        if (tags == null) {
            tags = new HashSet<>();
        }

        tags.add(tag);
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
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

    public LocalDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public LocalDateTime getLastJiraSyncDate() {
        return lastJiraSyncDate;
    }

    public void setLastJiraSyncDate(LocalDateTime lastJiraSyncDate) {
        this.lastJiraSyncDate = lastJiraSyncDate;
    }

    @Override
    public String toString() {
        return String.format(
                "Case{ id= %s ,name= %s, description= %s, steps= %s, creationDate= %s, priority= %s, tags= %s, status= %s, comment= %s};",
                id, name, description, steps, creationDate, priority, tags, steps, comment);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Case)) {
            return false;
        }

        final Case aCase = (Case) o;

        return (id != null ? id.equals(aCase.id) : aCase.id == null)
                && (name != null ? name.equals(aCase.name) : aCase.name == null)
                && (description != null ? description.equals(aCase.description)
                : aCase.description == null)
                && (steps != null ? steps.equals(aCase.steps) : aCase.steps == null)
                && (priority != null ? priority.equals(aCase.priority) : aCase.priority == null)
                && (status != null ? status.equals(aCase.status) : aCase.status == null)
                && (tags != null ? tags.equals(aCase.tags) : aCase.tags == null)
                && (comment != null ? comment.equals(aCase.comment) : aCase.comment == null);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (steps != null ? steps.hashCode() : 0);
        result = 31 * result + (priority != null ? priority.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (tags != null ? tags.hashCode() : 0);
        result = 31 * result + (comment != null ? comment.hashCode() : 0);
        return result;
    }
}