package com.epam.test_generator.dto;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class SuitDTO {


    private Long id;

    private String name;

    private String description;

    private List<CaseDTO> cases;

    private Integer priority;

    private String creationDate;

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
        if (o == null || getClass() != o.getClass()) return false;

        SuitDTO suitDTO = (SuitDTO) o;

        if (!id.equals(suitDTO.id)) return false;
        if (!name.equals(suitDTO.name)) return false;
        if (!description.equals(suitDTO.description)) return false;
        if (!cases.equals(suitDTO.cases)) return false;
        if (!priority.equals(suitDTO.priority)) return false;
        if (!creationDate.equals(suitDTO.creationDate)) return false;
        return tags.equals(suitDTO.tags);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + description.hashCode();
        result = 31 * result + cases.hashCode();
        result = 31 * result + priority.hashCode();
        result = 31 * result + creationDate.hashCode();
        result = 31 * result + tags.hashCode();
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

