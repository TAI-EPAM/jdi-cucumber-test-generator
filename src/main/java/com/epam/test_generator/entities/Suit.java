package com.epam.test_generator.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;


@Entity
public class Suit implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String description;

    private Integer priority;

    private String creationDate;

    private String tags;

    @OneToMany(cascade = {CascadeType.ALL})
    private List<Case> cases;

    public Suit() {
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");

        creationDate = formatter.format(Calendar.getInstance().getTime());
    }

    public Suit(Long id, String name, String description, List<Case> cases, Integer priority, String tags) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.cases = cases;
        this.priority = priority;
        this.tags = tags;
    }

    public Suit(String name, String description, Integer priority, String creationDate, String tags, List<Case> cases) {
        this.name = name;
        this.description = description;
        this.priority = priority;
        this.creationDate = creationDate;
        this.tags = tags;
        this.cases = cases;
    }

    public Suit(Long id, String name, String description, Integer priority, String creationDate, String tags, List<Case> cases) {
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

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
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

        for (Case caze: cases) {
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
        if (this == o) return true;
        if (!(o instanceof Suit)) return false;

        Suit suit = (Suit) o;

        if (id != null ? !id.equals(suit.id) : suit.id != null) return false;
        if (name != null ? !name.equals(suit.name) : suit.name != null) return false;
        if (description != null ? !description.equals(suit.description) : suit.description != null) return false;
        if (priority != null ? !priority.equals(suit.priority) : suit.priority != null) return false;
        if (creationDate != null ? !creationDate.equals(suit.creationDate) : suit.creationDate != null) return false;
        if (tags != null ? !tags.equals(suit.tags) : suit.tags != null) return false;
        return cases != null ? cases.equals(suit.cases) : suit.cases == null;
    }
}