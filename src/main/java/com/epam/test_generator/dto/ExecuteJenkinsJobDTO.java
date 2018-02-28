package com.epam.test_generator.dto;

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
}
