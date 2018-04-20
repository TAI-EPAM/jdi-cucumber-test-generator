package com.epam.test_generator.controllers.jenkins;


import com.epam.test_generator.controllers.jenkins.response.CommonJenkinsJobDTO;
import com.epam.test_generator.controllers.jenkins.response.ExecutedJenkinsJobDTO;
import com.offbytwo.jenkins.model.Job;
import com.offbytwo.jenkins.model.JobWithDetails;
import com.offbytwo.jenkins.model.QueueReference;
import com.offbytwo.jenkins.model.Executable;
import org.springframework.stereotype.Component;

@Component
public class JenkinsTransformer {

    public ExecutedJenkinsJobDTO toExecutedDTO(Job job, QueueReference queueReference,
                                               Executable executable) {
        ExecutedJenkinsJobDTO executedJenkinsJobDTO = new ExecutedJenkinsJobDTO();
        executedJenkinsJobDTO.setJobName(job.getName());
        executedJenkinsJobDTO.setJobUrl(job.getUrl());
        executedJenkinsJobDTO.setQueueUrl(queueReference.getQueueItemUrlPart());
        executedJenkinsJobDTO.setQueueExecutableId(executable.getNumber());
        executedJenkinsJobDTO.setQueueExecutableUrl(executable.getUrl());
        return executedJenkinsJobDTO;
    }

    public CommonJenkinsJobDTO toCommonDto(Job job) {
        CommonJenkinsJobDTO commonJenkinsJobDTO = new CommonJenkinsJobDTO();
        commonJenkinsJobDTO.setJobUrl(job.getUrl());
        commonJenkinsJobDTO.setJobName(job.getName());
        return commonJenkinsJobDTO;
    }
}
