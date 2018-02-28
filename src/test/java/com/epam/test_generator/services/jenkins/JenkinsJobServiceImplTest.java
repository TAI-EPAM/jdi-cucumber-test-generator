package com.epam.test_generator.services.jenkins;

import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.model.*;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class JenkinsJobServiceImplTest {

    @Mock
    JenkinsServerFactory jenkinsServerFactory;

    @Mock
    JenkinsServer jenkinsServer;

    @InjectMocks
    JenkinsJobServiceImpl jenkinsJobService;

    Map<String, Job> jobs;
    List<CommonJenkinsJobResponse> expectedJobs;

    @Before
    public void setUp() {
        jobs = new HashMap<>();
        jobs.put("job1", new Job("job1_name", "job1_url"));
        jobs.put("job2", new Job("job2_name", "job2_url"));

        expectedJobs = new ArrayList<>();

        for (Map.Entry<String, Job> entry : jobs.entrySet()) {
            CommonJenkinsJobResponse commonJenkinsJobResponse = new CommonJenkinsJobResponse();
            commonJenkinsJobResponse.setJobName(entry.getValue().getName());
            commonJenkinsJobResponse.setJobUrl(entry.getValue().getUrl());
            expectedJobs.add(commonJenkinsJobResponse);
        }
    }

    @Test
    public void getJobs_Success() throws IOException {
        when(jenkinsServerFactory.getJenkinsServer()).thenReturn(jenkinsServer);
        when(jenkinsServer.getJobs()).thenReturn(jobs);

        List<CommonJenkinsJobResponse> actualJobs = jenkinsJobService.getJobs();

        verify(jenkinsServerFactory).getJenkinsServer();
        verify(jenkinsServer).getJobs();
        assertEquals(expectedJobs, actualJobs);
    }
}