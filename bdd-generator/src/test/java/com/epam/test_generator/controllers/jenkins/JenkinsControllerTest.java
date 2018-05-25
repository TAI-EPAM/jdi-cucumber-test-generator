package com.epam.test_generator.controllers.jenkins;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epam.test_generator.controllers.GlobalExceptionController;
import com.epam.test_generator.controllers.jenkins.request.ExecuteJenkinsJobDTO;
import com.epam.test_generator.controllers.jenkins.response.CommonJenkinsJobDTO;
import com.epam.test_generator.controllers.jenkins.response.ExecutedJenkinsJobDTO;
import com.epam.test_generator.services.exceptions.JenkinsRuntimeInternalException;
import com.epam.test_generator.services.jenkins.JenkinsJobServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@RunWith(MockitoJUnitRunner.class)
public class JenkinsControllerTest {

    private ObjectMapper mapper = new ObjectMapper();
    private MockMvc mockMvc;
    private String jobName = "jobName";
    private ExecutedJenkinsJobDTO executedJenkinsJobResponse;
    private List<CommonJenkinsJobDTO> jobsList;

    @Mock
    private JenkinsJobServiceImpl jenkinsJobService;

    @InjectMocks
    private JenkinsController jenkinsController;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(jenkinsController)
            .setControllerAdvice(new GlobalExceptionController())
            .build();

        executedJenkinsJobResponse = new ExecutedJenkinsJobDTO();
        executedJenkinsJobResponse.setJobName("jobName");
        executedJenkinsJobResponse.setJobUrl("jobURL");
        executedJenkinsJobResponse.setQueueUrl("queueUrl");
        executedJenkinsJobResponse.setQueueExecutableId(1L);
        executedJenkinsJobResponse.setQueueExecutableUrl("queueExecutableURL");

        jobsList = new ArrayList<>();
    }

    @Test
    public void getJobs_Jobs_StatusOK() throws Exception {
        when(jenkinsJobService.getJobs()).thenReturn(jobsList);
        mockMvc.perform(get("/jenkins/jobs"))
            .andExpect(status().isOk())
            .andExpect(content().string(jobsList.toString()));
    }

    @Test
    public void getJobs_Jobs_StatusInternalServerError() throws Exception {
        when(jenkinsJobService.getJobs()).thenThrow(JenkinsRuntimeInternalException.class);
        mockMvc.perform(get("/jenkins/jobs"))
            .andExpect(status().isInternalServerError());
    }

    @Test
    public void executeJob_Job_StatusOK() throws Exception {
        ExecuteJenkinsJobDTO jobDTO = new ExecuteJenkinsJobDTO();
        jobDTO.setJobName(jobName);

        when(jenkinsJobService.runJob(jobName)).thenReturn(executedJenkinsJobResponse);
        String json = mapper.writeValueAsString(jobDTO);
        mockMvc.perform(
            post("/jenkins/job/execute").contentType(MediaType.APPLICATION_JSON).content(json))
            .andExpect(status().isOk())
            .andExpect(content().string(mapper.writeValueAsString(executedJenkinsJobResponse)));

        verify(jenkinsJobService).runJob(eq(jobName));
        verifyNoMoreInteractions(jenkinsJobService);
    }

    @Test
    public void executeJob_JobWithoutJobName_StatusBadRequest() throws Exception {
        ExecuteJenkinsJobDTO jobDTO = new ExecuteJenkinsJobDTO();

        String json = mapper.writeValueAsString(jobDTO);
        mockMvc.perform(
            post("/jenkins/job/execute").contentType(MediaType.APPLICATION_JSON).content(json))
            .andExpect(status().isBadRequest());
        verifyNoMoreInteractions(jenkinsJobService);
    }

    @Test
    public void executeJob_UnexpectedException_StatusInternalServerErro() throws Exception {
        ExecuteJenkinsJobDTO jobDTO = new ExecuteJenkinsJobDTO();
        jobDTO.setJobName(jobName);

        when(jenkinsJobService.runJob(jobName)).thenThrow(new RuntimeException());
        String json = mapper.writeValueAsString(jobDTO);
        mockMvc.perform(
            post("/jenkins/job/execute").contentType(MediaType.APPLICATION_JSON).content(json))
            .andExpect(status().isInternalServerError());

        verify(jenkinsJobService).runJob(eq(jobName));
        verifyNoMoreInteractions(jenkinsJobService);
    }

}