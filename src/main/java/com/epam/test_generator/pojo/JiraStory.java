package com.epam.test_generator.pojo;

import com.epam.test_generator.entities.Suit;
import com.epam.test_generator.services.exceptions.JiraRuntimeException;
import net.rcarz.jiraclient.Issue;
import net.rcarz.jiraclient.Status;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * Represents stories from Jira. It is used to map stories from Jira to BDD {@link Suit}.
 */
public class JiraStory {

    private String name;

    private String jiraKey;

    private String description;

    private String jiraProjectKey;

    private String assigner;

    private JiraStatus status;

    private String priority;

    private String repotrer;


    public JiraStory(Issue issue) {
        if(issue.getIssueType().isSubtask()) {
            throw new JiraRuntimeException("Cannot create JiraStory from substory [key:" + issue.getKey() + "]", null);
        }

        name = issue.getSummary();
        jiraKey = issue.getKey();
        description = StringUtils.substring(StringUtils.defaultIfEmpty(issue.getDescription(), "No description"), 0, 250);
        jiraProjectKey = issue.getProject() == null ? "No project" :issue.getProject().getKey();
        assigner = issue.getAssignee() == null ? "no assigner" : issue.getAssignee().getDisplayName();
        status = getJiraStatus(issue.getStatus());
        priority = issue.getPriority() == null ? "no priority" : issue.getPriority().getName();
        repotrer = issue.getReporter() == null? "no reporter" : issue.getReporter().getDisplayName();
    }

    public JiraStory() {
    }


    private JiraStatus getJiraStatus(Status status) {
        JiraStatus jiraStatus;
        if (status == null) {
            jiraStatus = JiraStatus.OPEN;
        } else if (status.getName().equals(JiraStatus.RESOLVED.getJiraStatusName())) {
            jiraStatus = JiraStatus.RESOLVED;
        } else {
            jiraStatus = JiraStatus.OPEN;
        }
        return jiraStatus;
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

    public String getJiraProjectKey() {
        return jiraProjectKey;
    }

    public void setJiraProjectKey(String jiraProjectKey) {
        this.jiraProjectKey = jiraProjectKey;
    }

    public String getAssigner() {
        return assigner;
    }

    public void setAssigner(String assigner) {
        this.assigner = assigner;
    }

    public JiraStatus getStatus() {
        return status;
    }

    public void setStatus(JiraStatus status) {
        this.status = status;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getRepotrer() {
        return repotrer;
    }

    public void setRepotrer(String repotrer) {
        this.repotrer = repotrer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JiraStory jiraStory = (JiraStory) o;
        return Objects.equals(name, jiraStory.name) &&
                Objects.equals(jiraKey, jiraStory.jiraKey) &&
                Objects.equals(description, jiraStory.description) &&
                Objects.equals(jiraProjectKey, jiraStory.jiraProjectKey) &&
                Objects.equals(assigner, jiraStory.assigner) &&
                Objects.equals(status, jiraStory.status) &&
                Objects.equals(priority, jiraStory.priority) &&
                Objects.equals(repotrer, jiraStory.repotrer);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name, jiraKey, description, jiraProjectKey, assigner, status, priority, repotrer);
    }
}
