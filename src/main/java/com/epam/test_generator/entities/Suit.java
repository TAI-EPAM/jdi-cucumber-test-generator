package com.epam.test_generator.entities;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import org.springframework.data.domain.Persistable;


@Entity
public class Suit implements Serializable, Persistable<Long> {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String description;

    private Integer priority;

    private Date creationDate;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<Tag> tags;

    @OneToMany(cascade = {CascadeType.ALL})
    private List<Case> cases;

    public Suit() {
        creationDate = Calendar.getInstance().getTime();
    }

    public Suit(Long id, String name, String description, List<Case> cases, Integer priority,
                Set<Tag> tags) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.cases = cases;
        this.priority = priority;
        this.tags = tags;
    }

    public Suit(String name, String description, Integer priority, Date creationDate, Set<Tag> tags,
                List<Case> cases) {
        this.name = name;
        this.description = description;
        this.priority = priority;
        this.creationDate = creationDate;
        this.tags = tags;
        this.cases = cases;
    }

    public Suit(Long id, String name, String description, Integer priority, Date creationDate,
                Set<Tag> tags, List<Case> cases) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.priority = priority;
        this.creationDate = creationDate;
        this.tags = tags;
        this.cases = cases;
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
     * Override in order entity manager use persist only when call with suit id = null
     * and all tag ids is null and all cases is new.
     *<br/>
     * For example, with standard behaviour spring data jpa will call persist on this request
     * {"id":null,"description":"4","name":"4","priority":4,"tags":[{"id":10, "name":"soap"}]}
     * and it will cause "detached entity passed to persist" error.
     */
    @Override
    public boolean isNew() {
        boolean isAllTagsWithNullId = tags == null
            || tags.stream().allMatch(tag -> tag.getId() == null);

        boolean isAllCasesNew = cases == null || cases.stream().allMatch(Case::isNew);

        return id == null && isAllTagsWithNullId && isAllCasesNew;
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

    public List<Case> getCases() {
        return cases;
    }

    public void setCases(List<Case> cases) {
        this.cases = cases;
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
            ", creationDate=" + creationDate +
            ", tags='" + tags + '\'' +
            ", cases=" + cases +
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

        final Suit suit = (Suit) o;

        return (id != null ? id.equals(suit.id) : suit.id == null)
            && (name != null ? name.equals(suit.name) : suit.name == null)
            && (description != null ? description.equals(suit.description)
            : suit.description == null)
            && (priority != null ? priority.equals(suit.priority) : suit.priority == null)
            && (tags != null ? tags.equals(suit.tags) : suit.tags == null)
            && (cases != null ? cases.equals(suit.cases) : suit.cases == null);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (priority != null ? priority.hashCode() : 0);
        result = 31 * result + (tags != null ? tags.hashCode() : 0);
        result = 31 * result + (cases != null ? cases.hashCode() : 0);
        return result;
    }
}