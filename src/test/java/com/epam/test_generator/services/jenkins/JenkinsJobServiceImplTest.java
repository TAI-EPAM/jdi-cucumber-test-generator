package com.epam.test_generator.services.jenkins;

import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.model.Executable;
import com.offbytwo.jenkins.model.Job;
import com.offbytwo.jenkins.model.JobWithDetails;
import com.offbytwo.jenkins.model.QueueItem;
import com.offbytwo.jenkins.model.QueueReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;

import static com.epam.test_generator.services.jenkins.JenkinsJobService.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class JenkinsJobServiceImplTest {

    @Mock
    private JenkinsServerFactory jenkinsServerFactory;

    @Mock
    private JenkinsServer jenkinsServer;

    @Mock
    private JobWithDetails jobWithDetails;

    @Mock
    private QueueReference queueReference;

    @Mock
    private QueueItem queueItem;

    @InjectMocks
    private JenkinsJobServiceImpl jenkinsJobService;

    @Before
    public void setUp() {
        when(jenkinsServerFactory.getJenkinsServer()).thenReturn(jenkinsServer);
    }

    @Test
    public void getJobs_Success() throws IOException {

        HashMap<String, Job> jobs = new HashMap<>();
        jobs.put("job1", new Job("job1_name", "job1_url"));
        jobs.put("job2", new Job("job2_name", "job2_url"));

        List<CommonJenkinsJobResponse> expectedJobs = new ArrayList<>();

        for (Map.Entry<String, Job> entry : jobs.entrySet()) {
            CommonJenkinsJobResponse commonJenkinsJobResponse = new CommonJenkinsJobResponse();
            commonJenkinsJobResponse.setJobName(entry.getValue().getName());
            commonJenkinsJobResponse.setJobUrl(entry.getValue().getUrl());
            expectedJobs.add(commonJenkinsJobResponse);
        }

        when(jenkinsServer.getJobs()).thenReturn(jobs);
        List<CommonJenkinsJobResponse> actualJobs = jenkinsJobService.getJobs();
        verify(jenkinsServerFactory, times(1)).getJenkinsServer();
        verify(jenkinsServer, times(1)).getJobs();
        assertEquals(expectedJobs, actualJobs);

        verifyNoMoreInteractions(jenkinsServerFactory);
        verifyNoMoreInteractions(jenkinsServer);
    }

    @Test(expected = RuntimeException.class)
    public void getJobs_IOException_RuntimeException() throws IOException {
        when(jenkinsServer.getJobs()).thenThrow(IOException.class);
        List<CommonJenkinsJobResponse> actualJobs = jenkinsJobService.getJobs();
    }

    @Test
    public void runJob_Success() throws Exception {
        Executable executable = new Executable();
        executable.setUrl("executableUrl");
        executable.setNumber(5L);

        when(jenkinsServer.getJob(anyString())).thenReturn(jobWithDetails);
        when(jobWithDetails.build(eq(true))).thenReturn(queueReference);
        when(jenkinsServer.getQueueItem(any())).thenReturn(queueItem);
        when(jobWithDetails.getName()).thenReturn("jobName");
        when(jobWithDetails.getUrl()).thenReturn("jobUrl");
        when(queueReference.getQueueItemUrlPart()).thenReturn("queueItemUrl");
        when(queueItem.getExecutable()).thenReturn(executable);

        ExecuteJenkinsJobResponse response = jenkinsJobService.runJob("jobName");
        assertNotNull(response);
        assertEquals("jobName", response.getJobName());
        assertEquals("jobUrl", response.getJobUrl());
        assertEquals("queueItemUrl", response.getQueueUrl());
        assertEquals("executableUrl", response.getQueueExecutableUrl());
        assertEquals(5L, response.getQueueExecutableId().longValue());

        verify(jenkinsServer, times(1)).getJob(anyString());
        verify(jobWithDetails, times(1)).build(anyBoolean());
        verify(jenkinsServer, times(1)).getQueueItem(any());

        verifyNoMoreInteractions(jenkinsServer);
    }

    @Test(expected = RuntimeException.class)
    public void runJob_IOException_RuntimeException() throws IOException {
        when(jenkinsServer.getJob(anyString())).thenThrow(IOException.class);
        jenkinsJobService.runJob("jobName");
    }

    @Test(expected = RuntimeException.class)
    public void runJob_QueueExecutionIOException_RuntimeException() throws Exception {
        when(jenkinsServer.getJob(anyString())).thenReturn(jobWithDetails);
        when(jobWithDetails.build(eq(true))).thenReturn(queueReference);
        when(jenkinsServer.getQueueItem(any())).thenThrow(IOException.class);
        jenkinsJobService.runJob("jobName");
    }

    @Test(expected = RuntimeException.class)
    public void runJob_QueueExecutionTimeout_RuntimeException() throws Exception {
        when(jenkinsServer.getJob(anyString())).thenReturn(jobWithDetails);
        when(jobWithDetails.build(eq(true))).thenReturn(queueReference);
        jenkinsJobService.runJob("jobName");
    }

}