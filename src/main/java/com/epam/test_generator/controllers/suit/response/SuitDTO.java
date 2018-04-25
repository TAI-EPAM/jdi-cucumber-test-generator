package com.epam.test_generator.controllers.suit.response;

import com.epam.test_generator.controllers.caze.response.CaseDTO;
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

    private long creationDate;

    private long updateDate;

    private Set<TagDTO> tags;

    @NotNull
    private String status;

    @NotNull
    @Min(value = 1)
    private Integer rowNumber;

    public SuitDTO(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public SuitDTO() {
        creationDate = Instant.now().getEpochSecond();
        updateDate = creationDate;
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

    public long getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(long creationDate) {
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

    public String getDisplayedStatusName() {
        return status;
    }

    public void setDisplayedStatusName(String status) {
        this.status = status;
    }

    public long getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(long updateDate) {
        this.updateDate = updateDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SuitDTO suitDTO = (SuitDTO) o;
        return Objects.equals(id, suitDTO.id) &&
            Objects.equals(name, suitDTO.name) &&
            Objects.equals(description, suitDTO.description) &&
            Objects.equals(cases, suitDTO.cases) &&
            Objects.equals(priority, suitDTO.priority) &&
            Objects.equals(creationDate, suitDTO.creationDate) &&
            Objects.equals(tags, suitDTO.tags) &&
            status == suitDTO.status &&
            Objects.equals(rowNumber, suitDTO.rowNumber);
    }

    @Override
    public int hashCode() {

        return Objects
            .hash(id, name, description, cases, priority, creationDate, tags, status, rowNumber);
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

