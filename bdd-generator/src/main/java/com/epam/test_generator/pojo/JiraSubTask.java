package com.epam.test_generator.pojo;

import com.epam.test_generator.entities.Case;
import java.util.Objects;
import net.rcarz.jiraclient.Issue;

/**
 * Represents subtasks from Jira. It is used to map subtask from Jira to BDD {@link Case}.
 */
public class JiraSubTask {
    private String name;

    private String jiraKey;

    private String description;

    private String priority;

    private String jiraProjectKey;

    private String jiraParentKey;

    private String status;

    public JiraSubTask(Issue issue) {
        name = issue.getSummary();
        priority = issue.getPriority() == null ? "no priority" : issue.getPriority().getName();
        description = issue.getDescription() == null ? "No description" : issue.getDescription();
        if (description.length() > 250) description = description.substring(0, 250);
        jiraKey = issue.getKey();
        jiraProjectKey = issue.getProject() == null ? "No project" : issue.getProject().getKey();
        jiraParentKey = issue.getParent() == null ? "No parent" : issue.getParent().getKey();
        status = issue.getStatus() == null ? "No status" : issue.getStatus().getName();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJiraKey() {
        return jiraKey;
    }

    public void setJiraKey(String jiraKey) {
        this.jiraKey = jiraKey;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getJiraParentKey() {
        return jiraParentKey;
    }

    public void setJiraParentKey(String jiraParentKey) {
        this.jiraParentKey = jiraParentKey;
    }

    public String getJiraProjectKey() {
        return jiraProjectKey;
    }

    public void setJiraProjectKey(String jiraProjectKey) {
        this.jiraProjectKey = jiraProjectKey;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        JiraSubTask subTask = (JiraSubTask) o;
        return Objects.equals(name, subTask.name) &&
            Objects.equals(jiraKey, subTask.jiraKey) &&
            Objects.equals(description, subTask.description) &&
            Objects.equals(priority, subTask.priority) &&
            Objects.equals(jiraProjectKey, subTask.jiraProjectKey) &&
            Objects.equals(jiraParentKey, subTask.jiraParentKey) &&
            Objects.equals(status, subTask.status);
    }

    @Override
    public int hashCode() {

        return Objects
            .hash(name, jiraKey, description, priority, jiraProjectKey, jiraParentKey, status);
    }
}
