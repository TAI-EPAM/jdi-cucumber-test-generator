package com.epam.test_generator.controllers.suit.request;

import com.epam.test_generator.controllers.tag.response.TagDTO;
import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

public class SuitCreateDTO {

    @NotNull
    @Size(min = 1, max = 255)
    private String name;

    @NotNull
    @Min(value = 1)
    @Max(value = 5)
    @ApiModelProperty(allowableValues = "range[1, 5]", example = "1")
    private Integer priority;

    @Size(max = 255)
    private String description;

    private Set<TagDTO> tags;

    public SuitCreateDTO() {
    }

    public SuitCreateDTO(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Set<TagDTO> getTags() {
        return tags;
    }

    public void setTags(Set<TagDTO> tags) {
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

    @Override
    public String toString() {
        return "Suit{name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", priority=" + priority +
                ", tags=" + tags +
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
        SuitCreateDTO that = (SuitCreateDTO) o;
        return Objects.equals(name, that.name) &&
            Objects.equals(priority, that.priority) &&
            Objects.equals(description, that.description) &&
            Objects.equals(tags, that.tags);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name, priority, description, tags);
    }
}
