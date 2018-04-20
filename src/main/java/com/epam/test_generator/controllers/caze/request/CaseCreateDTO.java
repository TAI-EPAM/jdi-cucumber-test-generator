package com.epam.test_generator.controllers.caze.request;

import com.epam.test_generator.controllers.tag.response.TagDTO;
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
        if (!(o instanceof CaseCreateDTO)) {
            return false;
        }

        final CaseCreateDTO caseCreateDTO = (CaseCreateDTO) o;

        return ( (name != null ? name.equals(caseCreateDTO.name) : caseCreateDTO.name == null)
                && (description != null ? description.equals(caseCreateDTO.description)
                : caseCreateDTO.description == null)
                && (priority != null ? priority.equals(caseCreateDTO.priority) : caseCreateDTO.priority == null)
                && (comment != null ? comment.equals(caseCreateDTO.comment) : caseCreateDTO.comment == null))
                && (tags != null ? tags.equals(caseCreateDTO.tags) : caseCreateDTO.tags == null);
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (priority != null ? priority.hashCode() : 0);
        result = 31 * result + (comment != null ? comment.hashCode() : 0);
        result = 31 * result + (tags != null ? tags.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return String.format(
                "CaseCreateDTO{ name= %s, description= %s, priority= %s, comment= %s, tags= %s };",
                name, description, priority, comment, tags);
    }
}
