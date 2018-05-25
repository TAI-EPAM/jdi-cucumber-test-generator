package com.epam.test_generator.services.jenkins;

import com.epam.test_generator.controllers.jenkins.JenkinsTransformer;
import com.epam.test_generator.controllers.jenkins.response.CommonJenkinsJobDTO;
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
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class JenkinsJobServiceImplTest {

    @Mock
    private JenkinsServerFactory jenkinsServerFactory;

    @Mock
    private JenkinsServer jenkinsServer;

    @Mock
    private JenkinsTransformer jenkinsTransformer;

    @InjectMocks
    private JenkinsJobServiceImpl jenkinsJobService;

    private Map<String, Job> jobs;
    private List<CommonJenkinsJobDTO> expectedJobs;
    private static final String NAME1 = "job1_name";
    private static final String NAME2 = "job2_name";
    private static final String URL1 = "job1_url";
    private static final String URL2 = "job2_url";

    private Job job1;
    private Job job2;
    private CommonJenkinsJobDTO commonJenkinsJobDTO1;
    private CommonJenkinsJobDTO commonJenkinsJobDTO2;

    @Before
    public void setUp() {
        jobs = new HashMap<>();
        job1 = new Job(NAME1, URL1);
        job2 = new Job(NAME2, URL2);
        jobs.put("job1", job1);
        jobs.put("job2", job2);

        expectedJobs = new ArrayList<>();

        commonJenkinsJobDTO1 = new CommonJenkinsJobDTO();
        commonJenkinsJobDTO1.setJobName(NAME1);
        commonJenkinsJobDTO1.setJobUrl(URL1);

        commonJenkinsJobDTO2 = new CommonJenkinsJobDTO();
        commonJenkinsJobDTO2.setJobName(NAME2);
        commonJenkinsJobDTO2.setJobUrl(URL2);

        expectedJobs.add(commonJenkinsJobDTO2);
        expectedJobs.add(commonJenkinsJobDTO1);
    }

    @Test
    public void getJobs_Success() throws IOException {
        when(jenkinsServerFactory.getJenkinsServer()).thenReturn(jenkinsServer);
        when(jenkinsServer.getJobs()).thenReturn(jobs);
        when(jenkinsTransformer.toCommonDto(job1)).thenReturn(commonJenkinsJobDTO1);
        when(jenkinsTransformer.toCommonDto(job2)).thenReturn(commonJenkinsJobDTO2);

        List<CommonJenkinsJobDTO> actualJobs = jenkinsJobService.getJobs();

        verify(jenkinsServerFactory).getJenkinsServer();
        verify(jenkinsServer).getJobs();
        assertEquals(expectedJobs, actualJobs);
    }
}