package com.epam.test_generator.controllers.jenkins.response;

import java.util.Objects;

public class ExecutedJenkinsJobDTO extends CommonJenkinsJobDTO {

    private String queueUrl;
    private Long queueExecutableId;
    private String queueExecutableUrl;

    public String getQueueUrl() {
        return queueUrl;
    }

    public void setQueueUrl(String queueUrl) {
        this.queueUrl = queueUrl;
    }

    public Long getQueueExecutableId() {
        return queueExecutableId;
    }

    public void setQueueExecutableId(Long queueExecutableId) {
        this.queueExecutableId = queueExecutableId;
    }

    public String getQueueExecutableUrl() {
        return queueExecutableUrl;
    }

    public void setQueueExecutableUrl(String queueExecutableUrl) {
        this.queueExecutableUrl = queueExecutableUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        ExecutedJenkinsJobDTO that = (ExecutedJenkinsJobDTO) o;
        return Objects.equals(queueUrl, that.queueUrl) &&
            Objects.equals(queueExecutableId, that.queueExecutableId) &&
            Objects.equals(queueExecutableUrl, that.queueExecutableUrl);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), queueUrl, queueExecutableId, queueExecutableUrl);
    }

    @Override
    public String toString() {
        return "ExecuteJenkinsJobResponse{" +
            "queueUrl='" + queueUrl + '\'' +
            ", queueExecutableId=" + queueExecutableId +
            ", queueExecutableUrl='" + queueExecutableUrl + '\'' +
            '}';
    }
}