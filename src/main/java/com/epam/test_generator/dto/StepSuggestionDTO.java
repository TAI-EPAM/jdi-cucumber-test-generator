package com.epam.test_generator.dto;

import com.epam.test_generator.entities.StepType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class StepSuggestionDTO {

    @NotNull
    private Long id;

    @NotNull
    @Size(min = 1, max = 255)
    private String content;

    @NotNull
    private StepType type;

    @NotNull
    private Long version;

    public StepSuggestionDTO(Long id, String content, StepType type, Long version) {
        this.id = id;
        this.content = content;
        this.type = type;
        this.version = version;
    }

    public StepSuggestionDTO(String content, StepType type, Long version) {
        this.content = content;
        this.type = type;
        this.version = version;
    }

    public StepSuggestionDTO() {
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
        if (!(o instanceof StepSuggestionDTO)) {
            return false;
        }

        StepSuggestionDTO that = (StepSuggestionDTO) o;

        return (id != null ? id.equals(that.id) : that.id == null)
            && (content != null ? content.equals(that.content) : that.content == null)
            && (type != null ? type.equals(that.type) : that.type == null)
            && (version != null ? version.equals(that.version) : that.version == null);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (content != null ? content.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (version != null ? version.hashCode() : 0);
        return result;
    }
}
