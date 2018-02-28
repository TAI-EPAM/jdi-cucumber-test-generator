package com.epam.test_generator.services.jenkins;

import java.util.List;

public interface JenkinsJobService {


    List<CommonJenkinsJobResponse> getJobs();

    ExecuteJenkinsJobResponse runJob(String jobName);

    public static class CommonJenkinsJobResponse {

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            CommonJenkinsJobResponse that = (CommonJenkinsJobResponse) o;

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
        public String toString() {
            return "CommonJenkinsJobResponse{" +
                    "jobName='" + jobName + '\'' +
                    ", jobUrl='" + jobUrl + '\'' +
                    '}';
        }
    }

    public static class ExecuteJenkinsJobResponse extends CommonJenkinsJobResponse {

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
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            if (!super.equals(o)) return false;

            ExecuteJenkinsJobResponse that = (ExecuteJenkinsJobResponse) o;

            if (getQueueUrl() != null ? !getQueueUrl().equals(that.getQueueUrl()) : that.getQueueUrl() != null)
                return false;
            if (getQueueExecutableId() != null ? !getQueueExecutableId().equals(that.getQueueExecutableId()) : that.getQueueExecutableId() != null)
                return false;
            return getQueueExecutableUrl() != null ? getQueueExecutableUrl().equals(that.getQueueExecutableUrl()) : that.getQueueExecutableUrl() == null;
        }

        @Override
        public int hashCode() {
            int result = super.hashCode();
            result = 31 * result + (getQueueUrl() != null ? getQueueUrl().hashCode() : 0);
            result = 31 * result + (getQueueExecutableId() != null ? getQueueExecutableId().hashCode() : 0);
            result = 31 * result + (getQueueExecutableUrl() != null ? getQueueExecutableUrl().hashCode() : 0);
            return result;
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
}
