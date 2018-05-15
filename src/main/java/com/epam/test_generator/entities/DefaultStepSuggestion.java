package com.epam.test_generator.entities;

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;


/**
 * This class represents example of possible steps for user.
 */
@Entity
public class DefaultStepSuggestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @Enumerated(EnumType.STRING)
    private StepType type;

    public DefaultStepSuggestion(Long id, String content, StepType type) {
        this.id = id;
        this.content = content;
        this.type = type;
    }

    public DefaultStepSuggestion(String content, StepType type) {
        this.content = content;
        this.type = type;
    }

    public DefaultStepSuggestion() {
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

    public StepType getType() {
        return type;
    }

    public void setType(StepType type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DefaultStepSuggestion)) {
            return false;
        }
        DefaultStepSuggestion that = (DefaultStepSuggestion) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
