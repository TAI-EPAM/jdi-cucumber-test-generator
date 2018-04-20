package com.epam.test_generator.controllers.caze.request;

import com.epam.test_generator.controllers.tag.response.TagDTO;
import com.epam.test_generator.controllers.step.response.StepDTO;
import com.epam.test_generator.entities.Action;
import com.epam.test_generator.entities.Status;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;


/**
 * This DTO fully represent {@Link Case} entity. {@Link CaseEditDTO} is used only  for cascade work with cases
 * in situation when it's necessary to manipulate (create, delete or change) multiple cases for one request.
 * In other situations it's better to use {@Link CaseDTO}.
 */
public class CaseEditDTO {

    private Long id;

    @Size(min = 1, max = 250)
    private String name;

    @Size(min = 1, max = 255)
    private String description;

    @Min(value = 1)
    @Max(value = 5)
    private Integer priority;

    private Status status;

    @Valid
    private List<StepDTO> steps;

    @Valid
    private List<TagDTO> tags;

    @NotNull
    private Action action;

    private String comment;

    public CaseEditDTO() {
    }

    public CaseEditDTO(Long id, String description, String name, Integer priority, Status status,
                       List<StepDTO> steps, Action action, String comment) {
        this.description = description;
        this.name = name;
        this.priority = priority;
        this.status = status;
        this.steps = steps;
        this.action = action;
        this.id = id;
        this.comment = comment;
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

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<TagDTO> getTags() {
        return tags;
    }

    public void setTags(List<TagDTO> tags) {
        this.tags = tags;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<StepDTO> getSteps() {
        return steps;
    }

    public void setSteps(List<StepDTO> steps) {
        this.steps = steps;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CaseEditDTO)) {
            return false;
        }

        CaseEditDTO caseEditDTO = (CaseEditDTO) o;

        return (id != null ? id.equals(caseEditDTO.id) : caseEditDTO.id == null)
            && (name != null ? name.equals(caseEditDTO.name) : caseEditDTO.name == null)
            && (description != null ? description.equals(caseEditDTO.description)
            : caseEditDTO.description == null)
            && (priority != null ? priority.equals(caseEditDTO.priority)
            : caseEditDTO.priority == null)
            && (status != null ? status.equals(caseEditDTO.status) : caseEditDTO.status == null)
            && (comment != null ? comment.equals(caseEditDTO.comment) : caseEditDTO.comment == null)
            && (action != null ? action.equals(caseEditDTO.action) : caseEditDTO.action == null)
            && (tags != null ? tags.equals(caseEditDTO.tags) : caseEditDTO.tags == null);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (priority != null ? priority.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (action != null ? action.hashCode() : 0);
        result = 31 * result + (comment != null ? comment.hashCode() : 0);
        result = 31 * result + (tags != null ? tags.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return String.format(
                "CaseEditDTO{ id= %s, name= %s, description= %s, priority= %s, tags= %s, status= %s, comment= %s, action =%s};",
                id, name, description, priority, tags, status, comment, action);
    }

}
