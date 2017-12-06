package com.epam.test_generator.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class StepSuggestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    private StepType type;

    public StepSuggestion(Long id, String content, StepType type) {
        this.id = id;
        this.content = content;
        this.type = type;
    }

    public StepSuggestion(String content, StepType type) {
        this.content = content;
        this.type = type;
    }

    public StepSuggestion() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getType() {
        return type.ordinal();
    }

    public void setType(Integer type) {
        this.type = StepType.values()[type];
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        StepSuggestion that = (StepSuggestion) o;

        return id.equals(that.id) && content.equals(that.content) && type == that.type;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + content.hashCode();
        result = 31 * result + type.hashCode();
        return result;
    }
}
