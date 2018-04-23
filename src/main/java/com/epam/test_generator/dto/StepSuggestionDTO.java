package com.epam.test_generator.dto;

import com.epam.test_generator.entities.StepType;
import java.util.Objects;
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
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StepSuggestionDTO that = (StepSuggestionDTO) o;
        return Objects.equals(id, that.id) &&
            Objects.equals(content, that.content) &&
            type == that.type &&
            Objects.equals(version, that.version);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, content, type, version);
    }
}
