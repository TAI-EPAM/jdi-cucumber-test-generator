package com.epam.test_generator.controllers.caze.request;

import com.epam.test_generator.controllers.tag.response.TagDTO;
import com.epam.test_generator.entities.Status;
import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.Set;

public class CaseUpdateDTO {

    @Size(min = 1, max = 250)
    private String name;

    @Size(min = 1, max = 255)
    private String description;

    @Min(value = 1)
    @Max(value = 5)
    @ApiModelProperty(allowableValues = "range[1, 5]", example = "1")
    private Integer priority;

    private Status status;

    @Valid
    private Set<TagDTO> tags;

    private String comment;

    public CaseUpdateDTO() {
    }

    public CaseUpdateDTO(String name, String description, Integer priority, Status status, String comment) {
        this.description = description;
        this.name = name;
        this.priority = priority;
        this.status = status;
        this.comment = comment;
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CaseUpdateDTO that = (CaseUpdateDTO) o;
        return Objects.equals(name, that.name) &&
            Objects.equals(description, that.description) &&
            Objects.equals(priority, that.priority) &&
            status == that.status &&
            Objects.equals(tags, that.tags) &&
            Objects.equals(comment, that.comment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, priority, status, tags, comment);
    }

    @Override
    public String toString() {
        return String.format(
                "updateCaseDTO{ name= %s, description= %s, priority= %s, tags= %s, status= %s, comment= %s};",
                name, description, priority, tags, status, comment);
    }

}
