package com.epam.test_generator.dto;

import com.epam.test_generator.entities.StepType;
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
        if (!(o instanceof StepSuggestionUpdateDTO)) {
            return false;
        }

        StepSuggestionUpdateDTO that = (StepSuggestionUpdateDTO) o;

        return (content != null ? content.equals(that.content) : that.content == null)
            && (type != null ? type.equals(that.type) : that.type == null)
            && (version != null ? version.equals(that.version) : that.version == null);
    }

    @Override
    public int hashCode() {
        int result = content != null ? content.hashCode() : 0;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (version != null ? version.hashCode() : 0);
        return result;
    }
}
