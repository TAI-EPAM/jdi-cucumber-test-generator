package com.epam.test_generator.controllers.stepsuggestion.request;

import com.epam.test_generator.entities.StepType;
import java.util.Objects;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class StepSuggestionCreateDTO {

    @NotNull
    @Size(min = 1, max = 255)
    private String content;

    @NotNull
    private StepType type;

    public StepSuggestionCreateDTO() {
    }

    public StepSuggestionCreateDTO(String content, StepType type) {
        this.content = content;
        this.type = type;
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
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StepSuggestionCreateDTO that = (StepSuggestionCreateDTO) o;
        return Objects.equals(content, that.content) &&
            type == that.type;
    }

    @Override
    public int hashCode() {

        return Objects.hash(content, type);
    }
}
