package com.epam.test_generator.dto;

import javax.validation.constraints.*;

public class StepSuggestionDTO {
    private Long id;

    @NotNull
    @Size(min = 1, max = 255)
    private String content;

    @NotNull
    @Min(value = 0)
    @Max(value = 5)
    private Integer type;

    public StepSuggestionDTO(Long id, String content, Integer type) {
        this.id = id;
        this.content = content;
        this.type = type;
    }

    public StepSuggestionDTO(String content, Integer type) {
        this.content = content;
        this.type = type;
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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
