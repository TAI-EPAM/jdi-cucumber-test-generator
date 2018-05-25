package com.epam.test_generator.controllers.caze.response;

import com.epam.test_generator.controllers.step.response.StepDTO;
import com.epam.test_generator.controllers.tag.response.TagDTO;
import com.epam.test_generator.entities.Status;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
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

    private long creationDate;

    private long updateDate;

    private Integer rowNumber;

    @NotNull
    @Min(value = 1)
    @Max(value = 5)
    private Integer priority;

    @Size(max = 5)
    @Valid
    private Set<TagDTO> tags;

    @NotNull
    private String status;

    private String comment;

    public CaseDTO() {
        creationDate = Instant.now().getEpochSecond();
        updateDate = creationDate;
    }

    public CaseDTO(Long id, String name, String description, List<StepDTO> steps, Integer priority,
                   Set<TagDTO> tags, String status, String comment, Integer rowNumber) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.steps = steps;
        this.priority = priority;
        this.tags = tags;
        this.status = status;
        this.comment = comment;
        this.rowNumber = rowNumber;
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

    public long getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(long creationDate) {
        this.creationDate = creationDate;
    }

    public long getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(long updateDate) {
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

    public String getDisplayedStatusName() {
        return status;
    }

    public void setDisplayedStatusName(String status) {
        this.status = status;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(Integer rowNumber) {
        this.rowNumber = rowNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CaseDTO caseDTO = (CaseDTO) o;
        return Objects.equals(id, caseDTO.id) &&
            Objects.equals(name, caseDTO.name) &&
            Objects.equals(description, caseDTO.description) &&
            Objects.equals(steps, caseDTO.steps) &&
            Objects.equals(priority, caseDTO.priority) &&
            Objects.equals(tags, caseDTO.tags) &&
            status == caseDTO.status &&
            Objects.equals(comment, caseDTO.comment) &&
            Objects.equals(rowNumber, caseDTO.rowNumber);
    }

    @Override
    public int hashCode() {
        return Objects
            .hash(id, name, description, steps, priority, tags, status, comment, rowNumber);
    }

    @Override
    public String toString() {
        return String.format(
            "CaseDTO{ id= %s ,name= %s, description= %s, steps= %s, creationDate= %s, " +
                "priority= %s, tags= %s, status= %s, comment= %s, rowNumber=%s};",
            id, name, description, steps, creationDate, priority, tags, status, comment, rowNumber);
    }
}



