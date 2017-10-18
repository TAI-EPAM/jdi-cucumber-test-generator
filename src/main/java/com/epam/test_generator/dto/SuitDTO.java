package com.epam.test_generator.dto;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class SuitDTO {


    private Long id;

    @NotNull
    @Size(min = 1, max = 255)
    private String name;

    @Size(max = 255)
    private String description;

    @Valid
    private List<CaseDTO> cases;

    @NotNull
    @Min(value = 1)
    @Max(value = 5)
    private Integer priority;

    private String creationDate;

    @Size(max = 255)
    private String tags;

    public SuitDTO() {
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        creationDate = formatter.format(Calendar.getInstance().getTime());
    }

    public SuitDTO(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public SuitDTO(Long id, String name, String description, List<CaseDTO> cases, Integer priority, String creationDate, String tags) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.cases = cases;
        this.priority = priority;
        this.creationDate = creationDate;
        this.tags = tags;
    }

    public SuitDTO(Long id, String name, String description, List<CaseDTO> cases, Integer priority, String tags) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.cases = cases;
        this.priority = priority;
        this.tags = tags;
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

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SuitDTO)) return false;

        SuitDTO suitDTO = (SuitDTO) o;

        if (id != null ? !id.equals(suitDTO.id) : suitDTO.id != null) return false;
        if (name != null ? !name.equals(suitDTO.name) : suitDTO.name != null) return false;
        if (description != null ? !description.equals(suitDTO.description) : suitDTO.description != null) return false;
        if (cases != null ? !cases.equals(suitDTO.cases) : suitDTO.cases != null) return false;
        if (priority != null ? !priority.equals(suitDTO.priority) : suitDTO.priority != null) return false;
        if (creationDate != null ? !creationDate.equals(suitDTO.creationDate) : suitDTO.creationDate != null)
            return false;
        return tags != null ? tags.equals(suitDTO.tags) : suitDTO.tags == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (cases != null ? cases.hashCode() : 0);
        result = 31 * result + (priority != null ? priority.hashCode() : 0);
        result = 31 * result + (creationDate != null ? creationDate.hashCode() : 0);
        result = 31 * result + (tags != null ? tags.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Suit{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", cases=" + cases +
                '}';
    }
}

