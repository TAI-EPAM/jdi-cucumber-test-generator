package com.epam.test_generator.pojo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SuitVersion {

    private String commitId;

    private Date updatedDate;

    private String author;

    private List<PropertyDifference> propertyDifferences;

    public SuitVersion(String commitId, Date updatedDate, String author,
                       List<PropertyDifference> propertyDifferences) {
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

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public List<PropertyDifference> getPropertyDifferences() {
        return propertyDifferences;
    }

    public void setPropertyDifferences(
        List<PropertyDifference> propertyDifferences) {
        this.propertyDifferences = propertyDifferences;
    }

    public void addPropertyDefference(PropertyDifference propertyDifference) {
        if (propertyDifferences == null) {
            propertyDifferences = new ArrayList<>();
        }

        propertyDifferences.add(propertyDifference);
    }
}
