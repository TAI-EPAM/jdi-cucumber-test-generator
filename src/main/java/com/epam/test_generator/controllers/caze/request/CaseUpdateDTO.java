package com.epam.test_generator.controllers.caze.request;

import com.epam.test_generator.controllers.step.response.StepDTO;
import com.epam.test_generator.controllers.tag.response.TagDTO;
import com.epam.test_generator.entities.Status;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Set;

public class CaseUpdateDTO {

    @Size(min = 1, max = 250)
    private String name;

    @Size(min = 1, max = 255)
    private String description;

    @Min(value = 1)
    @Max(value = 5)
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
        if (!(o instanceof CaseEditDTO)) {
            return false;
        }

        CaseUpdateDTO caseUpdateDTO = (CaseUpdateDTO) o;

        return (name != null ? name.equals(caseUpdateDTO.name) : caseUpdateDTO.name == null)
                && (description != null ? description.equals(caseUpdateDTO.description)
                : caseUpdateDTO.description == null)
                && (priority != null ? priority.equals(caseUpdateDTO.priority)
                : caseUpdateDTO.priority == null)
                && (status != null ? status.equals(caseUpdateDTO.status) : caseUpdateDTO.status == null)
                && (comment != null ? comment.equals(caseUpdateDTO.comment) : caseUpdateDTO.comment == null)
                && (tags != null ? tags.equals(caseUpdateDTO.tags) : caseUpdateDTO.tags == null);
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (priority != null ? priority.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (comment != null ? comment.hashCode() : 0);
        result = 31 * result + (tags != null ? tags.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return String.format(
                "updateCaseDTO{ name= %s, description= %s, priority= %s, tags= %s, status= %s, comment= %s};",
                name, description, priority, tags, status, comment);
    }

}
