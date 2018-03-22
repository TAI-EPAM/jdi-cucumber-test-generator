package com.epam.test_generator.dto;

import com.epam.test_generator.entities.StepType;
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
        if (!(o instanceof StepSuggestionCreateDTO)) {
            return false;
        }

        StepSuggestionCreateDTO that = (StepSuggestionCreateDTO) o;

        return (content != null ? content.equals(that.content) : that.content == null)
            && (type != null ? type.equals(that.type) : that.type == null);
    }
}
