package com.epam.test_generator.dto;

import javax.validation.constraints.*;

public class StepDTO {

    private Long id;

    @NotNull
    @Min(value = 1)
    private int rowNumber;

    @NotNull
    @Size(min = 1, max = 255)
    private String description;

    @NotNull
    @Min(value = 0)
    @Max(value = 5)
    private Integer type;

    public StepDTO() {
    }

    public StepDTO(Long id, int rowNumber, String description, Integer type) {
        this.id = id;
        this.rowNumber = rowNumber;
        this.description = description;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(int rowNumber) {
        this.rowNumber = rowNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "StepDTO{" +
                "id=" + id +
                ", rowNumber=" + rowNumber +
                ", description='" + description + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StepDTO)) return false;

        StepDTO stepDTO = (StepDTO) o;

        if (rowNumber != stepDTO.rowNumber) return false;
        if (id != null ? !id.equals(stepDTO.id) : stepDTO.id != null) return false;
        if (description != null ? !description.equals(stepDTO.description) : stepDTO.description != null) return false;
        return type != null ? type.equals(stepDTO.type) : stepDTO.type == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + rowNumber;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }
}