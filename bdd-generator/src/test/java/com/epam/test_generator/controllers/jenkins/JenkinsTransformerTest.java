package com.epam.test_generator.controllers.jenkins;

import com.epam.test_generator.controllers.jenkins.response.CommonJenkinsJobDTO;
import com.epam.test_generator.controllers.jenkins.response.ExecutedJenkinsJobDTO;
import com.offbytwo.jenkins.model.Executable;
import com.offbytwo.jenkins.model.Job;
import com.offbytwo.jenkins.model.QueueReference;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class JenkinsTransformerTest {

    @InjectMocks
    private JenkinsTransformer jenkinsTransformer;

    private static final String NAME = "job";
    private static final String URL = "url";
    private static final Long NUMBER = 1L;
    private Job job;
    @Before
    public void setUp(){
        job = new Job(NAME, URL);
    }

    @Test
    public void toExecutedDto_CorrectData_Success() {
        QueueReference queueReference = new QueueReference(URL);
        Executable executable = new Executable();
        executable.setNumber(NUMBER);
        executable.setUrl(URL);

        ExecutedJenkinsJobDTO executedJenkinsJobDTO = new ExecutedJenkinsJobDTO();
        executedJenkinsJobDTO.setJobName(NAME);
        executedJenkinsJobDTO.setJobUrl(URL);
        executedJenkinsJobDTO.setQueueExecutableId(NUMBER);
        executedJenkinsJobDTO.setQueueExecutableUrl(URL);
        executedJenkinsJobDTO.setQueueUrl(URL);

        ExecutedJenkinsJobDTO resultExecutedDTO = jenkinsTransformer
            .toExecutedDTO(job, queueReference, executable);
        Assert.assertEquals(resultExecutedDTO, executedJenkinsJobDTO);
    }

    @Test
    public void toCommonDto_Job_Success() {
        CommonJenkinsJobDTO commonJenkinsJobDTO = new CommonJenkinsJobDTO();
        commonJenkinsJobDTO.setJobUrl(URL);
        commonJenkinsJobDTO.setJobName(NAME);

        CommonJenkinsJobDTO resultCommonDTO = jenkinsTransformer.toCommonDto(job);
        Assert.assertEquals(resultCommonDTO, commonJenkinsJobDTO);
    }
}
