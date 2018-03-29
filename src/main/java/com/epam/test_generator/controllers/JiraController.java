package com.epam.test_generator.controllers;

import com.epam.test_generator.dto.ProjectDTO;
import com.epam.test_generator.pojo.JiraFilter;
import com.epam.test_generator.pojo.JiraProject;
import com.epam.test_generator.pojo.JiraStory;
import com.epam.test_generator.services.JiraService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


/**
 * Allow communication with Jira
 */
@RestController
@RequestMapping(value = "/jira")
public class JiraController {

    @Autowired
    private JiraService jiraService;


    @Secured({"ROLE_ADMIN", "ROLE_TEST_LEAD"})
    @ApiImplicitParam(name = "Authorization", value = "add here your token",
        paramType = "header", dataType = "string", required = true)
    @RequestMapping(value = "/{jiraSettingsId}/jiraFilters", method = RequestMethod.GET,
        produces = "application/json")
    public ResponseEntity<List<JiraFilter>> getFilters(
        @PathVariable("jiraSettingsId") Long clientId) {
        return new ResponseEntity<>(jiraService.getAllFilters(clientId), HttpStatus.OK);
    }

    @Secured({"ROLE_ADMIN", "ROLE_TEST_LEAD"})
    @ApiImplicitParam(name = "Authorization", value = "add here your token", paramType = "header", dataType = "string", required = true)
    @RequestMapping(value = "/{jiraSettingsId}/projects", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<JiraProject>> getProjects(@PathVariable("jiraSettingsId") Long id) {

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
        @PathVariable("jiraSettingsId") Long id) {

        return new ResponseEntity<>(
            jiraService.getJiraStoriesFromJiraProjectByProjectId(id, jiraProjectKey),
            HttpStatus.OK);
    }


    @Secured({"ROLE_ADMIN"})
    @ApiImplicitParam(name = "Authorization", value = "add here your token", paramType = "header", dataType = "string", required = true)
    @RequestMapping(value = "/{jiraSettingsId}/{jiraKey}/project", method = RequestMethod.POST, consumes =
        "application/json")
    public ResponseEntity<String> createProjectWithAttFromJira(
        @PathVariable("jiraKey") String jiraProjectKey,
        @RequestBody(required = false) List<JiraStory> jiraStories,
        @PathVariable("jiraSettingsId") Long id,
        Authentication auth) {

        jiraService.createProjectWithJiraStories(id, jiraProjectKey, jiraStories, auth);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Secured({"ROLE_ADMIN", "ROLE_TEST_LEAD"})
    @ApiImplicitParam(name = "Authorization", value = "add here your token", paramType = "header",
            dataType = "string", required = true)
    @RequestMapping(value = "/{jiraSettingsId}/{jiraKey}/projectByFilters", method = RequestMethod
        .POST,
        consumes =
        "application/json")
    public ResponseEntity<ProjectDTO> createProjectByFilters(
        @PathVariable("jiraKey") String jiraProjectKey,
        @RequestBody List<JiraFilter> jiraFilters,
        @PathVariable("jiraSettingsId") Long id, Authentication auth) {

        return new ResponseEntity<>
            (jiraService.createProjectWithAttachedFilters(id, jiraProjectKey, jiraFilters, auth),
                HttpStatus.OK);
    }



    @Secured({"ROLE_ADMIN", "ROLE_TEST_LEAD"})
    @ApiImplicitParams({
        @ApiImplicitParam(name = "jiraKey", value = "Key of project", required = true, dataType = "long", paramType = "path"),
        @ApiImplicitParam(name = "Authorization", value = "add here your token", paramType = "header", dataType = "string", required = true)
    })
    @RequestMapping(value = "/project/{jiraKey}/suits", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<String> createStoriesForProject(
        @RequestBody List<JiraStory> jiraStories,
        @PathVariable("jiraKey") String jiraProjectKey) {

        jiraService.addStoriesToExistedProject(jiraStories, jiraProjectKey);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @Secured({"ROLE_ADMIN", "ROLE_TEST_LEAD"})
    @ApiImplicitParam(name = "Authorization", value = "add here your token", paramType = "header", dataType = "string", required = true)
    @RequestMapping(value = "/{jiraSettingsId}/syncFromJira", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<String> syncFromJira(@PathVariable("jiraSettingsId") Long id) {
        jiraService.syncFromJira(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Secured({"ROLE_ADMIN", "ROLE_TEST_LEAD"})
    @ApiImplicitParam(name = "Authorization", value = "add here your token", paramType = "header", dataType = "string", required = true)
    @RequestMapping(value = "/{jiraSettingsId}/syncToJira", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<String> syncToJira(@PathVariable("jiraSettingsId") Long id) {

        jiraService.syncToJira(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

