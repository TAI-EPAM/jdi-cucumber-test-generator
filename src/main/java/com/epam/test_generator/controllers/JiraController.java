package com.epam.test_generator.controllers;

import com.epam.test_generator.pojo.JiraProject;
import com.epam.test_generator.pojo.JiraStory;
import com.epam.test_generator.services.JiraService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import net.rcarz.jiraclient.JiraException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * Allow communication with Jira
 */
@RestController
@RequestMapping(value = "/jira")
public class JiraController {

    @Autowired
    private JiraService jiraService;

    @Secured({"ROLE_ADMIN", "ROLE_TEST_LEAD"})
    @ApiImplicitParam(name = "Authorization", value = "add here your token", paramType = "header", dataType = "string", required = true)
    @RequestMapping(value = "/projects", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<JiraProject>> getProjects() throws JiraException {

        return new ResponseEntity<>(jiraService.getNonexistentJiraProjects(), HttpStatus.OK);
    }

    @Secured({"ROLE_ADMIN", "ROLE_TEST_LEAD"})
    @ApiImplicitParams({
        @ApiImplicitParam(name = "jiraKey", value = "Key of project", required = true, dataType = "long", paramType = "path"),
        @ApiImplicitParam(name = "Authorization", value = "add here your token", paramType = "header", dataType = "string", required = true)
    })
    @RequestMapping(value = "/project/{jiraKey}/stories", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<JiraStory>> getAllStories(
        @PathVariable("jiraKey") String jiraProjectKey)
        throws JiraException {

        return new ResponseEntity<>(
            jiraService.getJiraStoriesFromJiraProjectByProjectId(jiraProjectKey), HttpStatus.OK);
    }

    @Secured({"ROLE_ADMIN"})
    @ApiImplicitParam(name = "Authorization", value = "add here your token", paramType = "header", dataType = "string", required = true)
    @RequestMapping(value = "/project", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<String> createProjectWithAttFromJira(
        @RequestBody(required = false) List<JiraStory> jiraStories,
        Authentication auth) throws JiraException {

        jiraService.createProjectWithAttachments(jiraStories, auth);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Secured({"ROLE_ADMIN", "ROLE_TEST_LEAD"})
    @ApiImplicitParams({
        @ApiImplicitParam(name = "jiraKey", value = "Key of project", required = true, dataType = "long", paramType = "path"),
        @ApiImplicitParam(name = "Authorization", value = "add here your token", paramType = "header", dataType = "string", required = true)
    })
    @RequestMapping(value = "/project/{jiraKey}/suits", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<String> createStoriesForProject(
        @RequestBody List<JiraStory> jiraStories,
        @PathVariable("jiraKey") String jiraProjectKey)

        throws JiraException {

        jiraService.addStoriesToExistedProject(jiraStories, jiraProjectKey);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @Secured({"ROLE_ADMIN", "ROLE_TEST_LEAD"})
    @ApiImplicitParam(name = "Authorization", value = "add here your token", paramType = "header", dataType = "string", required = true)
    @RequestMapping(value = "/syncFromJira", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<String> syncFromJira() throws JiraException {
        jiraService.syncFromJira();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Secured({"ROLE_ADMIN", "ROLE_TEST_LEAD"})
    @ApiImplicitParam(name = "Authorization", value = "add here your token", paramType = "header", dataType = "string", required = true)
    @RequestMapping(value = "/syncToJira", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<String> syncToJira() throws JiraException {

        jiraService.syncToJira();
        return new ResponseEntity<>(HttpStatus.OK);
    }


}

