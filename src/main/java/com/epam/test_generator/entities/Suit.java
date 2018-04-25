package com.epam.test_generator.entities;

import com.epam.test_generator.entities.api.JiraSuitAndCaseTrait;
import com.epam.test_generator.entities.api.SuitTrait;
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


/**
 * This class represents test suit essence. Test suit is a collection of test cases that are
 * intended to be used for testing software's behaviour. Besides some simple fields like id, name,
 * description, history it consists of tags and cases. List of {@Link Case} represents sequence of
 * test cases that are linked to current {@Link Suit}. List of {@Link Tag} represents types of
 * {@Link Suit} object.
 */
@Entity
public class Suit implements SuitTrait, JiraSuitAndCaseTrait, Taggable, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private Integer priority;

    private ZonedDateTime creationDate;

    private ZonedDateTime updateDate;

    private String jiraKey;

    private String jiraProjectKey;

    @Enumerated(EnumType.STRING)
    private Status status;

    private ZonedDateTime lastModifiedDate;

    private ZonedDateTime lastJiraSyncDate;

    @ElementCollection
    @CollectionTable(
            name = "suit_tags",
            joinColumns = @JoinColumn(name = "suit_id"))
    @AttributeOverrides({
            @AttributeOverride(name="name", column=@Column(name="tag"))
    })
    private Set<Tag> tags;

    @OneToMany(cascade = {CascadeType.ALL})
    private List<Case> cases = new ArrayList<>();

    private Integer rowNumber;

    public Suit() {
        this.creationDate = ZonedDateTime.now();
        this.updateDate = this.creationDate;
    }

    public Suit(Long id, String name, String description, List<Case> cases, Integer priority,
                Set<Tag> tags, int rowNumber) {
        this();
        this.id = id;
        this.name = name;
        this.description = description;
        this.cases = cases;
        this.priority = priority;
        this.tags = tags;
        this.rowNumber = rowNumber;
    }

    public Suit(String name, String description, Integer priority, ZonedDateTime creationDate,
                ZonedDateTime updateDate, Set<Tag> tags, List<Case> cases, int rowNumber) {
        this.name = name;
        this.description = description;
        this.priority = priority;
        this.creationDate = creationDate;
        this.updateDate = updateDate;
        this.tags = tags;
        this.cases = cases;
        this.rowNumber = rowNumber;
    }

    public Suit(Long id, String name, String description, Integer priority,
                ZonedDateTime creationDate, ZonedDateTime updateDate, Set<Tag> tags,
                List<Case> cases, int rowNumber) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.priority = priority;
        this.creationDate = creationDate;
        this.updateDate = updateDate;
        this.tags = tags;
        this.cases = cases;
        this.rowNumber = rowNumber;
    }

    public Suit(String name, String description) {
        this();
        this.name = name;
        this.description = description;
    }

    public Suit(long id, String name, String description) {
        this();
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String getJiraKey() {
        return jiraKey;
    }

    public void setJiraKey(String jiraKey) {
        this.jiraKey = jiraKey;
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

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    @Override
    public List<Case> getCases() {
        return cases;
    }

    public void setCases(List<Case> cases) {
        this.cases = cases;
    }

    public void addCase(Case caze) {
        if (cases == null) {
            cases = new ArrayList<>();
        }
        caze.setRowNumber(cases
            .stream()
            .map(Case::getRowNumber)
            .max(Comparator.naturalOrder())
            .orElse(0)
            + 1);
        cases.add(caze);
    }

    public void addTag(Tag tag) {
        if (tags == null) {
            tags = new HashSet<>();
        }
        tags.add(tag);
    }

    public Integer getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(Integer rowNumber) {
        this.rowNumber = rowNumber;
    }

    public String getJiraProjectKey() {
        return jiraProjectKey;
    }

    public void setJiraProjectKey(String jiraProjectKey) {
        this.jiraProjectKey = jiraProjectKey;
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

    public Case getCaseById(Long id) {
        Case result = null;

        for (Case caze : cases) {
            if (caze.getId().equals(id)) {
                result = caze;
                break;
            }
        }

        return result;
    }

    public ZonedDateTime getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(ZonedDateTime updateDate) {
        this.updateDate = updateDate;
    }

    @Override
    public String toString() {
        return String.format(
            "Suit{ id= %s, name= %s, description= %s, priority = %s, status = %s,"
                + " creationDate = %s, updateDate = %s, jiraKey = %s, jiraProjectKey = %s,"
                + " tags= %s, cases = %s, rowNumber = %s};",
            id, name, description, priority, status, creationDate, updateDate, jiraKey,
            jiraProjectKey, tags, cases, rowNumber);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Suit)) {
            return false;
        }
        Suit suit = (Suit) o;
        return Objects.equals(id, suit.id) &&
            Objects.equals(name, suit.name) &&
            Objects.equals(description, suit.description) &&
            Objects.equals(priority, suit.priority) &&
            Objects.equals(status, suit.status) &&
            Objects.equals(jiraKey, suit.jiraKey) &&
            Objects.equals(jiraProjectKey, suit.jiraProjectKey) &&
            Objects.equals(tags, suit.tags) &&
            Objects.equals(cases, suit.cases) &&
            Objects.equals(rowNumber, suit.rowNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, priority, status, jiraKey, jiraProjectKey, tags,
            cases, rowNumber);
    }
}