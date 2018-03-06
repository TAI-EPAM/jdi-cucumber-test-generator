package com.epam.test_generator.controllers;

import com.epam.test_generator.dto.ExecuteJenkinsJobDTO;
import com.epam.test_generator.services.jenkins.JenkinsJobService;
import com.epam.test_generator.services.jenkins.JenkinsJobService.ExecuteJenkinsJobResponse;
import com.epam.test_generator.services.jenkins.JenkinsJobServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class JenkinsControllerTest {

    private ObjectMapper mapper = new ObjectMapper();
    private MockMvc mockMvc;
    private String jobName = "jobName";
    private ExecuteJenkinsJobResponse executeJenkinsJobResponse;
    private List<JenkinsJobService.CommonJenkinsJobResponse> jobsList;

    @Mock
    private JenkinsJobServiceImpl jenkinsJobService;

    @InjectMocks
    private JenkinsController jenkinsController;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(jenkinsController)
            .setControllerAdvice(new GlobalExceptionController())
            .build();

        executeJenkinsJobResponse = new ExecuteJenkinsJobResponse();
        executeJenkinsJobResponse.setJobName("jobName");
        executeJenkinsJobResponse.setJobUrl("jobURL");
        executeJenkinsJobResponse.setQueueUrl("queueUrl");
        executeJenkinsJobResponse.setQueueExecutableId(1L);
        executeJenkinsJobResponse.setQueueExecutableUrl("queueExecutableURL");

        jobsList = new ArrayList<>();
    }

    @Test
    public void getJobs_Jobs_StatusOK() throws Exception {
        when(jenkinsJobService.getJobs()).thenReturn(jobsList);
        mockMvc.perform(get("/jenkins/job/"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().string(jobsList.toString()));
    }

    @Test
    public void getJobs_Jobs_StatusInternalServerError() throws Exception {
        when(jenkinsJobService.getJobs()).thenThrow(Exception.class);
        mockMvc.perform(get("/jenkins/job/"))
            .andDo(print())
            .andExpect(status().isInternalServerError());
    }

    @Test
    public void executeJob_Job_StatusOK() throws Exception {
        ExecuteJenkinsJobDTO jobDTO = new ExecuteJenkinsJobDTO();
        jobDTO.setJobName(jobName);

        when(jenkinsJobService.runJob(jobName)).thenReturn(executeJenkinsJobResponse);
        String json = mapper.writeValueAsString(jobDTO);
        mockMvc.perform(
            post("/jenkins/job/execute").contentType(MediaType.APPLICATION_JSON).content(json))
            .andDo(print()).andExpect(status().isOk())
            .andExpect(content().string(mapper.writeValueAsString(executeJenkinsJobResponse)));

        verify(jenkinsJobService).runJob(eq(jobName));
        verifyNoMoreInteractions(jenkinsJobService);
    }


    @Test
    public void executeJob_JobWithoutJobName_StatusBadRequest() throws Exception {
        ExecuteJenkinsJobDTO jobDTO = new ExecuteJenkinsJobDTO();

        when(jenkinsJobService.runJob(jobName)).thenReturn(executeJenkinsJobResponse);
        String json = mapper.writeValueAsString(jobDTO);
        mockMvc.perform(
            post("/jenkins/job/execute").contentType(MediaType.APPLICATION_JSON).content(json))
            .andDo(print()).andExpect(status().isBadRequest());
        verifyNoMoreInteractions(jenkinsJobService);
    }

    @Test
    public void executeJob_UnexpectedException_StatusInternalServerError() throws Exception {
        ExecuteJenkinsJobDTO jobDTO = new ExecuteJenkinsJobDTO();
        jobDTO.setJobName(jobName);

        when(jenkinsJobService.runJob(jobName)).thenThrow(new RuntimeException());
        String json = mapper.writeValueAsString(jobDTO);
        mockMvc.perform(
            post("/jenkins/job/execute").contentType(MediaType.APPLICATION_JSON).content(json))
            .andDo(print()).andExpect(status().isInternalServerError());

        verify(jenkinsJobService).runJob(eq(jobName));
        verifyNoMoreInteractions(jenkinsJobService);
    }

}