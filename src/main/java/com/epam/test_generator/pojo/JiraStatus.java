package com.epam.test_generator.pojo;

public enum JiraStatus {
    CLOSED(31),
    RESOLVED(21, "Resolved"),
    REOPENED(71),
    OPEN(null);

    private Integer actionId;
    private String jiraStatusName;

    JiraStatus(Integer actionId) {
        this.actionId = actionId;
    }

    JiraStatus(Integer actionId, String jiraStatusName) {
        this.actionId = actionId;
        this.jiraStatusName = jiraStatusName;
    }

    public String getJiraStatusName() {
        return jiraStatusName;
    }

    public Integer getActionId() {
        return actionId;
    }
}
