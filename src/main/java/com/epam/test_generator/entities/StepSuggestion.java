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
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof StepSuggestion)) return false;

        StepSuggestion stepSuggestion = (StepSuggestion) obj;

        if (type.ordinal() != stepSuggestion.type.ordinal()) return false;
        if (id != null ? !id.equals(stepSuggestion.id) : stepSuggestion.id != null) return false;
        if (content != null ? !content.equals(stepSuggestion.content) : stepSuggestion.content != null) return false;

        return type == stepSuggestion.type;
    }
}
