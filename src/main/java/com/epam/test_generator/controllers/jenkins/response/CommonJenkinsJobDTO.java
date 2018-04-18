package com.epam.test_generator.controllers.jenkins.response;

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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CommonJenkinsJobDTO that = (CommonJenkinsJobDTO) o;

        if (getJobName() != null ? !getJobName().equals(that.getJobName()) : that.getJobName() != null)
            return false;
        return getJobUrl() != null ? getJobUrl().equals(that.getJobUrl()) : that.getJobUrl() == null;
    }

    @Override
    public int hashCode() {
        int result = getJobName() != null ? getJobName().hashCode() : 0;
        result = 31 * result + (getJobUrl() != null ? getJobUrl().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "CommonJenkinsJobDTO{" +
            "jobName='" + jobName + '\'' +
            ", jobUrl='" + jobUrl + '\'' +
            '}';
    }
}

