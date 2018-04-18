package com.epam.test_generator.controllers.jenkins;

import com.epam.test_generator.controllers.jenkins.request.ExecuteJenkinsJobDTO;
import com.epam.test_generator.controllers.jenkins.response.CommonJenkinsJobDTO;
import com.epam.test_generator.controllers.jenkins.response.ExecutedJenkinsJobDTO;
import com.epam.test_generator.services.jenkins.JenkinsJobService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/jenkins")
public class JenkinsController {

    private JenkinsJobService jenkinsJobService;

    @Autowired
    public JenkinsController(
        JenkinsJobService jenkinsJobService) {
        this.jenkinsJobService = jenkinsJobService;
    }

    /**
     * Get method for receiving all jobs from jenkins server
     *
     * @return list of job's names and urls
     */
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "add here your token", paramType = "header", dataType = "string", required = true)
    })
    @Secured({"ROLE_ADMIN", "ROLE_TEST_ENGINEER", "ROLE_TEST_LEAD", "ROLE_GUEST"})
    @GetMapping("/jobs")
    public ResponseEntity<List<CommonJenkinsJobDTO>> getJobs() throws Exception {
        return new ResponseEntity<>(jenkinsJobService.getJobs(), HttpStatus.OK);
    }

    /**
     * Post method for executing jenkins job on server
     *
     * @return params of executed job
     */
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "add here your token", paramType = "header", dataType = "string", required = true)
    })
    @Secured({"ROLE_ADMIN", "ROLE_TEST_ENGINEER", "ROLE_TEST_LEAD", "ROLE_GUEST"})
    @PostMapping("/job/execute")
    public ResponseEntity<ExecutedJenkinsJobDTO> executeJob(
        @RequestBody @Valid ExecuteJenkinsJobDTO jobDTO) throws Exception {
        return new ResponseEntity<>(jenkinsJobService.runJob(jobDTO.getJobName()), HttpStatus.OK);
    }
}

