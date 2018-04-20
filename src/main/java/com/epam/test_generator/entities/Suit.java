package com.epam.test_generator.entities;

import com.epam.test_generator.entities.api.JiraSuitAndCaseTrait;
import com.epam.test_generator.entities.api.SuitTrait;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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


/**
 * This class represents test suit essence. Test suit is a collection of test cases that are
 * intended to be used for testing software's behaviour. Besides some simple fields like id, name,
 * description, history it consists of tags and cases. List of {@Link Case} represents sequence of
 * test cases that are linked to current {@Link Suit}. List of {@Link Tag} represents types of
 * {@Link Suit} object.
 */
@Entity
public class Suit implements Serializable, Persistable<Long>, SuitTrait, JiraSuitAndCaseTrait {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private Integer priority;

    private Date creationDate;

    private String jiraKey;

    private String jiraProjectKey;

    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDateTime lastModifiedDate;

    private LocalDateTime lastJiraSyncDate;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<Tag> tags;

    @OneToMany(cascade = {CascadeType.ALL})
    private List<Case> cases = new ArrayList<>();

    private Integer rowNumber;

    public Suit() {
        creationDate = Calendar.getInstance().getTime();
    }

    public Suit(Long id, String name, String description, List<Case> cases, Integer priority,
                Set<Tag> tags, int rowNumber) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.cases = cases;
        this.priority = priority;
        this.tags = tags;
        this.rowNumber = rowNumber;
    }

    public Suit(String name, String description, Integer priority, Date creationDate, Set<Tag> tags,
                List<Case> cases, int rowNumber) {
        this.name = name;
        this.description = description;
        this.priority = priority;
        this.creationDate = creationDate;
        this.tags = tags;
        this.cases = cases;
        this.rowNumber = rowNumber;
    }

    public Suit(Long id, String name, String description, Integer priority, Date creationDate,
                Set<Tag> tags, List<Case> cases, int rowNumber) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.priority = priority;
        this.creationDate = creationDate;
        this.tags = tags;
        this.cases = cases;
        this.rowNumber = rowNumber;
    }

    public Suit(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Suit(long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    @Override
    public Long getId() {
        return id;
    }

    /**
     * Override in order entity manager use persist only when call with suit id = null and all tag
     * ids is null and all cases is new. <br/> For example, with standard behaviour spring data jpa
     * will call persist on this request {"id":null,"description":"4","name":"4","priority":4,"tags":[{"id":10,
     * "name":"soap"}]} and it will cause "detached entity passed to persist" error.
     */
    @Override
    public boolean isNew() {
        boolean isAllTagsWithNullId = tags == null
            || tags.stream().allMatch(tag -> tag.getId() == null);

        boolean isAllCasesNew = cases == null || cases.stream().allMatch(Case::isNew);

        return id == null && isAllTagsWithNullId && isAllCasesNew;
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

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
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

    @Override
    public String toString() {
        return "Suit{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", description='" + description + '\'' +
            ", priority=" + priority +
            ", status=" + status +
            ", creationDate=" + creationDate +
            ", jiraKey='" + jiraKey + '\'' +
            ", jiraProjectKey='" + jiraProjectKey + '\'' +
            ", tags=" + tags +
            ", cases=" + cases +
            ", rowNumber=" + rowNumber +
            '}';
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

        return Objects
            .hash(id, name, description, priority, status, jiraKey, jiraProjectKey, tags,
                cases,
                rowNumber);
    }
}