package com.epam.test_generator.controllers.suit.response;

import com.epam.test_generator.controllers.caze.response.CaseDTO;
import com.epam.test_generator.controllers.tag.response.TagDTO;
import com.epam.test_generator.entities.Status;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class SuitDTO {

    private Long id;

    @Size(min = 1, max = 255)
    private String name;

    @Size(max = 255)
    private String description;

    @Valid
    private List<CaseDTO> cases;

    @Min(value = 1)
    @Max(value = 5)
    private Integer priority;

    private Date creationDate;

    private Set<TagDTO> tags;

    @NotNull
    private Status status;

    @NotNull
    @Min(value = 1)
    private Integer rowNumber;

    public SuitDTO(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public SuitDTO() {

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

    public List<CaseDTO> getCases() {
        return cases;
    }

    public void setCases(List<CaseDTO> cases) {
        this.cases = cases;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Set<TagDTO> getTags() {
        return tags;
    }

    public void setTags(Set<TagDTO> tags) {
        this.tags = tags;
    }

    public Integer getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(Integer rowNumber) {
        this.rowNumber = rowNumber;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SuitDTO)) {
            return false;
        }

        SuitDTO suitDTO = (SuitDTO) o;

        return (id != null ? id.equals(suitDTO.id) : suitDTO.id == null)
            && (name != null ? name.equals(suitDTO.name) : suitDTO.name == null)
            && (status != null ? status.equals(suitDTO.status) : suitDTO.status == null)
            && (description != null ? description.equals(suitDTO.description)
            : suitDTO.description == null)
            && (cases != null ? cases.equals(suitDTO.cases) : suitDTO.cases == null)
            && (priority != null ? priority.equals(suitDTO.priority) : suitDTO.priority == null)
            && (tags != null ? tags.equals(suitDTO.tags) : suitDTO.tags == null)
            && (rowNumber != null ? rowNumber.equals(suitDTO.rowNumber)
            : suitDTO.rowNumber == null);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (cases != null ? cases.hashCode() : 0);
        result = 31 * result + (priority != null ? priority.hashCode() : 0);
        result = 31 * result + (tags != null ? tags.hashCode() : 0);
        result = 31 * result + (rowNumber != null ? rowNumber.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Suit{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", description='" + description + '\'' +
            ", cases=" + cases +
            ", priority=" + priority +
            ", tags=" + tags +
            ", rowNumber=" + rowNumber +
            '}';
    }
}

