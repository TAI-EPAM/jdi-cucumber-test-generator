package com.epam.test_generator.dto;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

public class CaseDTO {
    private Long id;

    @NotNull
    @Size(min = 1, max = 255)
    private String description;

    @Valid
    private List<StepDTO> steps;

    private String creationDate;

    private String updateDate;

    @NotNull
    @Min(value = 1)
    @Max(value = 5)
    private Integer priority;

    @Size(max = 5)
    @Valid
    private Set<TagDTO> tags;

    public CaseDTO() {
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        creationDate = formatter.format(Calendar.getInstance().getTime());
        updateDate = formatter.format(Calendar.getInstance().getTime());

    }

    public CaseDTO(Long id, String description, List<StepDTO> steps, Integer priority, Set<TagDTO> tags) {
        this.id = id;
        this.description = description;
        this.steps = steps;
        this.priority = priority;
        this.tags = tags;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CaseDTO)) return false;

        CaseDTO caseDTO = (CaseDTO) o;

        if (id != null ? !id.equals(caseDTO.id) : caseDTO.id != null) return false;
        if (description != null ? !description.equals(caseDTO.description) : caseDTO.description != null) return false;
        if (steps != null ? !steps.equals(caseDTO.steps) : caseDTO.steps != null) return false;
        if (creationDate != null ? !creationDate.equals(caseDTO.creationDate) : caseDTO.creationDate != null)
            return false;
        if (updateDate != null ? !updateDate.equals(caseDTO.updateDate) : caseDTO.updateDate != null) return false;
        if (priority != null ? !priority.equals(caseDTO.priority) : caseDTO.priority != null) return false;
        return tags != null ? tags.equals(caseDTO.tags) : caseDTO.tags == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (steps != null ? steps.hashCode() : 0);
        result = 31 * result + (creationDate != null ? creationDate.hashCode() : 0);
        result = 31 * result + (updateDate != null ? updateDate.hashCode() : 0);
        result = 31 * result + (priority != null ? priority.hashCode() : 0);
        result = 31 * result + (tags != null ? tags.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "CaseDTO{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", steps=" + steps +
                ", creationDate=" + creationDate +
                ", priority=" + priority +
                ", tags='" + tags + '\'' +
                '}';
    }

}



