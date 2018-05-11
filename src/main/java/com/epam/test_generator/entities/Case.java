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
import javax.persistence.*;

import com.epam.test_generator.entities.api.Taggable;
import org.springframework.data.domain.Persistable;
import org.springframework.statemachine.annotation.WithStateMachine;


/**
 * This class represents Test case essence. Test case is a set of actions that are used for checking
 * some software's behavior. Case consists of some simple fields like id of the case, it's name,
 * description, history information, result of testing and etc, also it includes sequence of steps
 * and tags. List of {@link Step} objects represents steps that must be done for verification within
 * current case. List of {@link Tag} objects represents types of current case.
 */
@Entity
@WithStateMachine
public class Case implements CaseTrait, JiraSuitAndCaseTrait, Taggable, Serializable {

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

    @ElementCollection
    @CollectionTable(
            name = "case_tags",
            joinColumns = @JoinColumn(name = "case_id"))
    @AttributeOverrides({
            @AttributeOverride(name="name", column=@Column(name="tag"))
    })
    private Set<Tag> tags;

    private String comment;

    public Case() {
        creationDate = ZonedDateTime.now();
        updateDate = creationDate;
        status = Status.NOT_DONE;
    }


    public Case(Long id, String name, String description, List<Step> steps,
                Integer priority, Set<Tag> tags, String comment) {
        creationDate = ZonedDateTime.now();
        updateDate = creationDate;
        this.id = id;
        this.name = name;
        this.description = description;
        this.steps = steps;
        this.priority = priority;
        this.tags = tags;
        this.comment = comment;
        status = steps.isEmpty() ? Status.NOT_DONE : Status.NOT_RUN;
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
        creationDate = ZonedDateTime.now();
        updateDate = creationDate;
        this.name = name;
        this.description = description;
        this.steps = steps;
        this.priority = priority;
        this.tags = tags;
        this.comment = comment;
        this.status = status;
    }

    public Long getId() {
        return id;
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

    /**
     * Equals for Case entity, as a business key using only id, name, description,
     * priority and status. Using collections in equals and hasCode methods are not recommended.
     *
     * @return true if equals
     */
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
            Objects.equals(priority, aCase.priority) &&
            Objects.equals(status, aCase.status);
    }

    /**
     * HashCode for Case entity, as a business key using only id, name, description,
     * priority and status. Using collections in equals and hasCode methods are not recommended.
     *
     * @return int hashCode value
     */
    @Override
    public int hashCode() {

        return Objects
            .hash(id, name, description, priority, status);
    }
}