package com.epam.test_generator.controllers.stepsuggestion.request;

import com.epam.test_generator.entities.StepType;
import java.util.Objects;
import javax.validation.constraints.NotNull;

public class StepSuggestionUpdateDTO {

    private String content;

    private StepType type;

    @NotNull
    private Long version;

    public StepSuggestionUpdateDTO() {
    }

    public StepSuggestionUpdateDTO(String content, StepType type, Long version) {
        this.content = content;
        this.type = type;
        this.version = version;
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

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StepSuggestionUpdateDTO that = (StepSuggestionUpdateDTO) o;
        return Objects.equals(content, that.content) &&
            type == that.type &&
            Objects.equals(version, that.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content, type, version);
    }
}
