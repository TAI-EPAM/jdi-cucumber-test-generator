package com.epam.test_generator.controllers.caze.request;

import com.epam.test_generator.controllers.tag.response.TagDTO;
import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

public class CaseCreateDTO {

    @NotNull
    @Size(min = 1, max = 250)
    private String name;

    @NotNull
    @Size(min = 1, max = 255)
    private String description;

    @NotNull
    @Min(value = 1)
    @Max(value = 5)
    @ApiModelProperty(allowableValues = "range[1, 5]", example = "1")
    private Integer priority;

    private String comment;

    @Size(max = 5)
    @Valid
    private Set<TagDTO> tags;

    public CaseCreateDTO() {
    }

    public CaseCreateDTO(String name, String description, Integer priority, String comment, Set<TagDTO> tags) {
        this.name = name;
        this.description = description;
        this.priority = priority;
        this.comment = comment;
        this.tags = tags;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Set<TagDTO> getTags() {
        return tags;
    }

    public void setTags(Set<TagDTO> tags) {
        this.tags = tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CaseCreateDTO that = (CaseCreateDTO) o;
        return Objects.equals(name, that.name) &&
            Objects.equals(description, that.description) &&
            Objects.equals(priority, that.priority) &&
            Objects.equals(comment, that.comment) &&
            Objects.equals(tags, that.tags);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name, description, priority, comment, tags);
    }

    @Override
    public String toString() {
        return String.format(
                "CaseCreateDTO{ name= %s, description= %s, priority= %s, comment= %s, tags= %s };",
                name, description, priority, comment, tags);
    }
}
