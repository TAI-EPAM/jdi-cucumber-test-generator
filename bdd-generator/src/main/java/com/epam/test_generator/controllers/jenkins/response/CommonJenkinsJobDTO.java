package com.epam.test_generator.controllers.jenkins.response;

import java.util.Objects;

public class CommonJenkinsJobDTO {
    private String jobName;
    private String jobUrl;

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobUrl() {
        return jobUrl;
    }

    public void setJobUrl(String jobUrl) {
        this.jobUrl = jobUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CommonJenkinsJobDTO that = (CommonJenkinsJobDTO) o;
        return Objects.equals(jobName, that.jobName) &&
            Objects.equals(jobUrl, that.jobUrl);
    }

    @Override
    public int hashCode() {

        return Objects.hash(jobName, jobUrl);
    }

    @Override
    public String toString() {
        return "CommonJenkinsJobDTO{" +
            "jobName='" + jobName + '\'' +
            ", jobUrl='" + jobUrl + '\'' +
            '}';
    }
}

