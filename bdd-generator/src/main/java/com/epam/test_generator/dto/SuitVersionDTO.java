package com.epam.test_generator.dto;

import com.epam.test_generator.controllers.version.caze.response.PropertyDifferenceDTO;

import java.util.List;
import java.util.Objects;

public class SuitVersionDTO {

    private String commitId;

    private String updatedDate;

    private String author;

    private List<PropertyDifferenceDTO> propertyDifferences;

    public SuitVersionDTO() {
    }

    public SuitVersionDTO(String commitId, String updatedDate, String author,
                          List<PropertyDifferenceDTO> propertyDifferences) {
        this.commitId = commitId;
        this.updatedDate = updatedDate;
        this.author = author;
        this.propertyDifferences = propertyDifferences;
    }

    public String getCommitId() {
        return commitId;
    }

    public void setCommitId(String commitId) {
        this.commitId = commitId;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public List<PropertyDifferenceDTO> getPropertyDifferences() {
        return propertyDifferences;
    }

    public void setPropertyDifferences(
        List<PropertyDifferenceDTO> propertyDifferences) {
        this.propertyDifferences = propertyDifferences;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SuitVersionDTO that = (SuitVersionDTO) o;
        return Objects.equals(commitId, that.commitId) &&
            Objects.equals(updatedDate, that.updatedDate) &&
            Objects.equals(author, that.author) &&
            Objects.equals(propertyDifferences, that.propertyDifferences);
    }

    @Override
    public int hashCode() {

        return Objects.hash(commitId, updatedDate, author, propertyDifferences);
    }
}
