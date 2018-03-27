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
    @RequestMapping(value = "/{jiraSettingsId}/projects", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<JiraProject>> getProjects(@PathVariable("jiraSettingsId") Long id)
        throws JiraException {

        return new ResponseEntity<>(jiraService.getNonexistentJiraProjects(id), HttpStatus.OK);
    }

    @Secured({"ROLE_ADMIN", "ROLE_TEST_LEAD"})
    @ApiImplicitParams({
        @ApiImplicitParam(name = "jiraKey", value = "Key of project", required = true, dataType = "long", paramType = "path"),
        @ApiImplicitParam(name = "Authorization", value = "add here your token", paramType = "header", dataType = "string", required = true)
    })
    @RequestMapping(value = "/{jiraSettingsId}/project/{jiraKey}/stories", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<JiraStory>> getAllStories(
        @PathVariable("jiraKey") String jiraProjectKey,
        @PathVariable("jiraSettingsId") Long id)
        throws JiraException {

        return new ResponseEntity<>(
            jiraService.getJiraStoriesFromJiraProjectByProjectId(id, jiraProjectKey),
            HttpStatus.OK);
    }

    @Secured({"ROLE_ADMIN"})
    @ApiImplicitParam(name = "Authorization", value = "add here your token", paramType = "header", dataType = "string", required = true)
    @RequestMapping(value = "/{jiraSettingsId}/project", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<String> createProjectWithAttFromJira(
        @RequestBody(required = false) List<JiraStory> jiraStories,
        @PathVariable("jiraSettingsId") Long id,
        Authentication auth) throws JiraException {

        jiraService.createProjectWithAttachments(id, jiraStories, auth);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Secured({"ROLE_ADMIN", "ROLE_TEST_LEAD"})
    @ApiImplicitParams({
        @ApiImplicitParam(name = "jiraKey", value = "Key of project", required = true, dataType = "long", paramType = "path"),
        @ApiImplicitParam(name = "Authorization", value = "add here your token", paramType = "header", dataType = "string", required = true)
    })
    @RequestMapping(value = "/{jiraSettingsId}/project/{jiraKey}/suits", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<String> createStoriesForProject(
        @RequestBody List<JiraStory> jiraStories,
        @PathVariable("jiraKey") String jiraProjectKey,
        @PathVariable("jiraSettingsId") Long id
    ) throws JiraException {

        jiraService.addStoriesToExistedProject(id, jiraStories, jiraProjectKey);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @Secured({"ROLE_ADMIN", "ROLE_TEST_LEAD"})
    @ApiImplicitParam(name = "Authorization", value = "add here your token", paramType = "header", dataType = "string", required = true)
    @RequestMapping(value = "/{jiraSettingsId}/syncFromJira", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<String> syncFromJira(@PathVariable("jiraSettingsId") Long id)
        throws JiraException {
        jiraService.syncFromJira(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Secured({"ROLE_ADMIN", "ROLE_TEST_LEAD"})
    @ApiImplicitParam(name = "Authorization", value = "add here your token", paramType = "header", dataType = "string", required = true)
    @RequestMapping(value = "/{jiraSettingsId}/syncToJira", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<String> syncToJira(@PathVariable("jiraSettingsId") Long id)
        throws JiraException {

        jiraService.syncToJira(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}

