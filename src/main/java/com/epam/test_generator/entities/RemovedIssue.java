package com.epam.test_generator.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class RemovedIssue {

    @Id
    private String jiraKey;

    public RemovedIssue(String jiraKey) {
        this.jiraKey = jiraKey;
    }

    public RemovedIssue() {
    }

    public String getJiraKey() {
        return jiraKey;
    }

    public void setJiraKey(String jiraKey) {
        this.jiraKey = jiraKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RemovedIssue that = (RemovedIssue) o;
        return Objects.equals(jiraKey, that.jiraKey);
    }

    @Override
    public int hashCode() {

        return Objects.hash(jiraKey);
    }

    @Override
    public String toString() {
        return "RemovedIssue{" +
                "jiraKey='" + jiraKey + '\'' +
                '}';
    }
}
