package com.epam.test_generator.services.jenkins;

import com.epam.test_generator.controllers.jenkins.response.CommonJenkinsJobDTO;
import com.epam.test_generator.controllers.jenkins.response.ExecutedJenkinsJobDTO;
import java.util.List;

public interface JenkinsJobService {


    List<CommonJenkinsJobDTO> getJobs();

    ExecutedJenkinsJobDTO runJob(String jobName);

}
