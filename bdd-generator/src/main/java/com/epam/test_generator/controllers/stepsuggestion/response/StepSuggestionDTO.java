package com.epam.test_generator.controllers.stepsuggestion.response;

import com.epam.test_generator.controllers.step.response.StepDTO;
import com.epam.test_generator.entities.StepType;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StepSuggestionDTO {

    private Long id;

    private String content;

    private StepType type;

    private Long version;

    private List<StepDTO> steps = new ArrayList<>();

    public StepSuggestionDTO(Long id, String content, StepType type) {
        this.id = id;
        this.content = content;
        this.type = type;
    }

    public StepSuggestionDTO() {
    }

    public StepSuggestionDTO(Long id, String content, StepType type, List<StepDTO> steps,
                             Long version) {
        this.id = id;
        this.content = content;
        this.type = type;
        this.steps = steps;
        this.version = version;
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

    public List<StepDTO> getSteps() {
        return steps;
    }

    public void setSteps(List<StepDTO> steps) {
        this.steps = steps;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "StepSuggestionDTO{" +
            "id=" + id +
            ", content='" + content + '\'' +
            ", type=" + type +
            ", steps=" + steps +
            ", version=" + version +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StepSuggestionDTO that = (StepSuggestionDTO) o;
        return Objects.equals(id, that.id) &&
            Objects.equals(content, that.content) &&
            Objects.equals(type, that.type) &&
            Objects.equals(version, that.version);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, content, type, version);
    }
}
