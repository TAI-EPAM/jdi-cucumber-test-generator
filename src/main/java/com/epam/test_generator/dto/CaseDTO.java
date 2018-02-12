package com.epam.test_generator.dto;

import com.epam.test_generator.entities.Status;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CaseDTO {

    private Long id;

    @NotNull
    @Size(min = 1, max = 250)
    private String name;

    @NotNull
    @Size(min = 1, max = 255)
    private String description;

    @Valid
    private List<StepDTO> steps;

    private Date creationDate;

    private Date updateDate;

    @NotNull
    @Min(value = 1)
    @Max(value = 5)
    private Integer priority;

    @Size(max = 5)
    @Valid
    private Set<TagDTO> tags;

    @NotNull
    private Status status;

    private String comment;

    public CaseDTO() {
        creationDate = Calendar.getInstance().getTime();
        updateDate = creationDate;
    }

    public CaseDTO(Long id, String name, String description, List<StepDTO> steps, Integer priority,
                   Set<TagDTO> tags, Status status, String comment) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.steps = steps;
        this.priority = priority;
        this.tags = tags;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public List<StepDTO> getSteps() {
        return steps;
    }

    public void setSteps(List<StepDTO> steps) {
        this.steps = steps;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CaseDTO)) {
            return false;
        }

        final CaseDTO caseDTO = (CaseDTO) o;

        return (id != null ? id.equals(caseDTO.id) : caseDTO.id == null)
            && (name != null ? name.equals(caseDTO.name) : caseDTO.name == null)
            && (description != null ? description.equals(caseDTO.description)
            : caseDTO.description == null)
            && (steps != null ? steps.equals(caseDTO.steps) : caseDTO.steps == null)
            && (priority != null ? priority.equals(caseDTO.priority) : caseDTO.priority == null)
            && (tags != null ? tags.equals(caseDTO.tags) : caseDTO.tags == null)
            && (comment != null ? comment.equals(caseDTO.comment) : caseDTO.comment == null)
            && (status != null ? status.equals(caseDTO.status) : caseDTO.status == null);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (steps != null ? steps.hashCode() : 0);
        result = 31 * result + (priority != null ? priority.hashCode() : 0);
        result = 31 * result + (tags != null ? tags.hashCode() : 0);
        result = 31 * result + (comment != null ? comment.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return String.format(
            "CaseDTO{ id= %s ,name= %s, description= %s, steps= %s, creationDate= %s, priority= %s, tags= %s, status= %s, comment= %s};",
            id, name, description, steps, creationDate, priority, tags, steps, comment);
    }
}



