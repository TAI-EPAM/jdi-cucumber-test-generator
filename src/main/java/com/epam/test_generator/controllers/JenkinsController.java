package com.epam.test_generator.controllers;

import com.epam.test_generator.dto.ExecuteJenkinsJobDTO;
import com.epam.test_generator.services.jenkins.JenkinsJobService;
import com.epam.test_generator.services.jenkins.JenkinsJobService.CommonJenkinsJobResponse;
import com.epam.test_generator.services.jenkins.JenkinsJobService.ExecuteJenkinsJobResponse;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(path = "/jenkins")
@RestController
public class JenkinsController {

    private JenkinsJobService jenkinsJobService;

    @Autowired
    public JenkinsController(
        JenkinsJobService jenkinsJobService) {
        this.jenkinsJobService = jenkinsJobService;
    }

    /**
     * Get method for receiving all jobs from jenkins server
     * @return list of job's names and urls
     * @throws Exception
     */
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "add here your token", paramType = "header", dataType = "string", required = true)
    })
    @Secured({"ROLE_ADMIN", "ROLE_TEST_ENGINEER", "ROLE_TEST_LEAD", "ROLE_GUEST"})
    @RequestMapping(path = "/job", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<CommonJenkinsJobResponse>> getJobs() throws Exception {
        return new ResponseEntity<>(jenkinsJobService.getJobs(), HttpStatus.OK);
    }

    /**
     * Post method for executing jenkins job on server
     * @param jobDTO
     * @return params of executed job
     * @throws Exception
     */
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "add here your token", paramType = "header", dataType = "string", required = true)
    })
    @Secured({"ROLE_ADMIN", "ROLE_TEST_ENGINEER", "ROLE_TEST_LEAD", "ROLE_GUEST"})
    @RequestMapping(path = "/job/execute", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<ExecuteJenkinsJobResponse> executeJob(
        @RequestBody @Valid ExecuteJenkinsJobDTO jobDTO) throws Exception {
        return new ResponseEntity<>(jenkinsJobService.runJob(jobDTO.getJobName()), HttpStatus.OK);
    }


}
