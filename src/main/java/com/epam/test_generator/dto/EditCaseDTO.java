package com.epam.test_generator.dto;

import com.epam.test_generator.entities.Action;
import com.epam.test_generator.entities.Status;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class EditCaseDTO {

    private Long id;

    @Size(min = 1, max = 250)
    private String name;

    @Size(min = 1, max = 255)
    private String description;

    @Min(value = 1)
    @Max(value = 5)
    private Integer priority;

    private Status status;

    @NotNull
    private Action action;

    public EditCaseDTO() {
    }

    public EditCaseDTO(String description, Integer priority) {
        this.description = description;
        this.priority = priority;
    }

    public EditCaseDTO(String description, String name, Integer priority, Status status,
                       Action action) {
        this.description = description;
        this.name = name;
        this.priority = priority;
        this.status = status;
        this.action = action;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EditCaseDTO)) {
            return false;
        }

        EditCaseDTO editCaseDTO = (EditCaseDTO) o;

        return (id != null ? id.equals(editCaseDTO.id) : editCaseDTO.id == null)
            && (name != null ? name.equals(editCaseDTO.name) : editCaseDTO.name == null)
            && (description != null ? description.equals(editCaseDTO.description)
            : editCaseDTO.description == null)
            && (priority != null ? priority.equals(editCaseDTO.priority)
            : editCaseDTO.priority == null)
            && (status != null ? status.equals(editCaseDTO.status) : editCaseDTO.status == null)
            && (action != null ? action.equals(editCaseDTO.action) : editCaseDTO.action == null);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (priority != null ? priority.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (action != null ? action.hashCode() : 0);
        return result;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
