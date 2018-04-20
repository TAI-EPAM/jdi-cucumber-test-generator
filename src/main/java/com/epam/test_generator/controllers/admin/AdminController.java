package com.epam.test_generator.controllers.admin;

import com.epam.test_generator.controllers.admin.request.UserRoleUpdateDTO;
import com.epam.test_generator.controllers.user.response.UserDTO;
import com.epam.test_generator.controllers.admin.request.JiraSettingsCreateDTO;
import com.epam.test_generator.controllers.admin.response.JiraSettingsDTO;
import com.epam.test_generator.controllers.project.response.ProjectDTO;
import com.epam.test_generator.services.AdminService;
import com.epam.test_generator.services.JiraSettingsService;
import com.epam.test_generator.services.ProjectService;
import com.epam.test_generator.services.UserService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.util.List;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controls performing of administration actions such as change user role or remove project. All
 * included actions require user to have role ADMIN.
 */
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private JiraSettingsService jiraSettingsService;

    @ApiImplicitParam(name = "Authorization", value = "add here your token", paramType = "header", dataType = "string", required = true)
    @Secured("ROLE_ADMIN")
    @PutMapping("/role")
    public ResponseEntity changeUserRole(@RequestBody @Valid UserRoleUpdateDTO userRoleUpdateDTO) {

        adminService.changeUserRole(userRoleUpdateDTO);

        return new ResponseEntity(HttpStatus.OK);
    }

    @ApiImplicitParam(name = "Authorization", value = "add here your token", paramType = "header", dataType = "string", required = true)
    @Secured("ROLE_ADMIN")
    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getUsers() {
        return new ResponseEntity<>(userService.getUsers(), HttpStatus.OK);
    }

    @ApiOperation(value = "Get all projects", nickname = "getProjects")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK", response = ProjectDTO.class, responseContainer = "List")
    })
    @Secured("ROLE_ADMIN")
    @ApiImplicitParam(name = "Authorization", value = "add here your token", paramType = "header", dataType = "string", required = true)
    @GetMapping("/projects")
    public ResponseEntity<List<ProjectDTO>> getProjects() {
        return new ResponseEntity<>(projectService.getProjects(), HttpStatus.OK);
    }

    @ApiOperation(value = "Delete project by id", nickname = "removeProject")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "project not found")
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "projectId", value = "ID of project to delete", required = true, dataType = "long", paramType = "path"),
        @ApiImplicitParam(name = "Authorization", value = "add here your token", paramType = "header", dataType = "string", required = true)
    })
    @Secured({"ROLE_ADMIN"})
    @DeleteMapping("/projects/{projectId}")
    public ResponseEntity<Void> removeProject(@PathVariable("projectId") long projectId) {
        projectService.removeProject(projectId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Secured({"ROLE_ADMIN"})
    @ApiOperation(value = "Create jira settings")
    @ApiImplicitParam(name = "Authorization", value = "add here your token", paramType = "header", dataType = "string", required = true)
    @PostMapping("/jira-settings")
    public ResponseEntity createJiraSettings(
        @RequestBody @Valid JiraSettingsCreateDTO jiraSettingsCreateDTO) {
        jiraSettingsService.createJiraSettings(jiraSettingsCreateDTO);
        return new ResponseEntity(HttpStatus.OK);
    }

    @Secured({"ROLE_ADMIN"})
    @ApiOperation(value = "Get jira settings")
    @ApiImplicitParam(name = "Authorization", value = "add here your token", paramType = "header", dataType = "string", required = true)
    @GetMapping("/jira-settings")
    public ResponseEntity<List<JiraSettingsDTO>> getJiraSettings() {
        List<JiraSettingsDTO> settings = jiraSettingsService.getJiraSettings();
        return new ResponseEntity<>(settings, HttpStatus.OK);
    }
}
