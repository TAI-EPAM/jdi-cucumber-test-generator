package com.epam.test_generator.controllers.jenkins.request;

import java.util.Objects;
import javax.validation.constraints.NotNull;

public class ExecuteJenkinsJobDTO {

    @NotNull
    private String jobName;

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ExecuteJenkinsJobDTO that = (ExecuteJenkinsJobDTO) o;
        return Objects.equals(jobName, that.jobName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(jobName);
    }
}
