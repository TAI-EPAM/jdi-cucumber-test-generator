package com.epam.test_generator.controllers.project;

import com.epam.test_generator.controllers.project.request.ProjectCreateDTO;
import com.epam.test_generator.controllers.project.request.ProjectUpdateDTO;
import com.epam.test_generator.controllers.project.response.ProjectDTO;
import com.epam.test_generator.controllers.project.response.ProjectFullDTO;
import com.epam.test_generator.dto.ValidationErrorsDTO;
import com.epam.test_generator.services.ProjectService;
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
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Handle all projects of an authorized user.
 */
@RestController
@RequestMapping("/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;


    @ApiOperation(value = "Get all projects of an authorized user", nickname = "getUserProjects")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK",
            response = ProjectDTO.class, responseContainer = "List")
    })
    @Secured({"ROLE_ADMIN", "ROLE_TEST_LEAD", "ROLE_TEST_ENGINEER", "ROLE_GUEST"})
    @ApiImplicitParam(name = "Authorization", value = "add here your token", paramType = "header", dataType = "string", required = true)
    @GetMapping
    public ResponseEntity<List<ProjectDTO>> getUserProjects(Authentication authentication) {
        return new ResponseEntity<>(projectService.getAuthenticatedUserProjects(authentication),
            HttpStatus.OK);
    }


    @ApiOperation(value = "Get project by id", nickname = "getProject")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK", response = ProjectFullDTO.class),
        @ApiResponse(code = 404, message = "project not found")
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "projectId", value = "ID of project to return",
            required = true, dataType = "long", paramType = "path"),
        @ApiImplicitParam(name = "Authorization", value = "add here your token", paramType = "header", dataType = "string", required = true)
    })
    @Secured({"ROLE_ADMIN", "ROLE_TEST_LEAD", "ROLE_TEST_ENGINEER", "ROLE_GUEST"})
    @GetMapping("/{projectId}")
    public ResponseEntity<ProjectFullDTO> getProject(@PathVariable("projectId") long projectId,
                                                     Authentication authentication) {

        return new ResponseEntity<>(
            projectService.getAuthUserFullProject(projectId, authentication),
            HttpStatus.OK);
    }


    @ApiOperation(value = "Create a new project", nickname = "createProject")
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "Created", response = Long.class),
        @ApiResponse(code = 400, message = "Invalid input", response = ValidationErrorsDTO.class)
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "projectDTO", value = "Create a new project",
            required = true, dataType = "ProjectCreateDTO", paramType = "body"),
        @ApiImplicitParam(name = "Authorization", value = "add here your token", paramType = "header", dataType = "string", required = true)
    })
    @ResponseStatus(HttpStatus.CREATED)
    @Secured({"ROLE_ADMIN"})
    @PostMapping
    public ResponseEntity<ProjectDTO> createProject(@RequestBody @Valid ProjectCreateDTO projectDTO,
                                                    Authentication authentication) {

        return new ResponseEntity<>(projectService.createProject(projectDTO, authentication),
            HttpStatus.CREATED);
    }

    @ApiOperation(value = "Update project by id", nickname = "updateProject")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 400, message = "Invalid input", response = ValidationErrorsDTO.class),
        @ApiResponse(code = 404, message = "project not found"),
        @ApiResponse(code = 403, message = "project is closed(readonly)")
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "projectId", value = "ID of project to update",
            required = true, dataType = "long", paramType = "path"),
        @ApiImplicitParam(name = "projectDTO", value = "Updated project object",
            required = true, dataType = "ProjectUpdateDTO", paramType = "body"),
        @ApiImplicitParam(name = "Authorization", value = "add here your token", paramType = "header", dataType = "string", required = true)
    })
    @Secured({"ROLE_ADMIN", "ROLE_TEST_LEAD"})
    @PutMapping("/{projectId}")
    public ResponseEntity<Void> updateProject(@PathVariable("projectId") long projectId,
                                              @RequestBody @Valid ProjectUpdateDTO projectDTO) {
        projectService.updateProject(projectId, projectDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "Close project by id", nickname = "closeProject")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "project not found"),
        @ApiResponse(code = 403, message = "project id closed(readonly)")
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "projectId", value = "ID of project to close",
            required = true, dataType = "long", paramType = "path"),
        @ApiImplicitParam(name = "Authorization", value = "add here your token", paramType = "header", dataType = "string", required = true)
    })
    @Secured({"ROLE_ADMIN", "ROLE_TEST_LEAD"})
    @DeleteMapping("/{projectId}")
    public ResponseEntity<Void> closeProject(@PathVariable("projectId") long projectId) {
        projectService.closeProject(projectId);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @ApiOperation(value = "Assign user to project", nickname = "addUserToProject")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Added", response = Long.class),
        @ApiResponse(code = 404, message = "project or user not found"),
        @ApiResponse(code = 403, message = "project id closed(readonly)")
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "projectId", value = "ID of project to which user will be added",
            required = true, dataType = "long", paramType = "path"),
        @ApiImplicitParam(name = "userId", value = "Id of user to be added",
            required = true, dataType = "Long", paramType = "query"),
        @ApiImplicitParam(name = "Authorization", value = "add here your token", paramType = "header", dataType = "string", required = true)
    })
    @Secured({"ROLE_ADMIN", "ROLE_TEST_LEAD"})
    @PutMapping("/{projectId}/user/{userId}")
    public ResponseEntity<Void> assignUserToProject(@PathVariable("projectId") long projectId,
                                                    @PathVariable("userId") long userId) {
        projectService.addUserToProject(projectId, userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "Remove user from project", nickname = "removeUserFromProject")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Removed", response = Long.class),
        @ApiResponse(code = 404, message = "project or user not found"),
        @ApiResponse(code = 403, message = "project id closed(readonly)")
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "projectId", value = "ID of project",
            required = true, dataType = "long", paramType = "path"),
        @ApiImplicitParam(name = "userId", value = "Id of user to be removed",
            required = true, dataType = "Long", paramType = "query"),
        @ApiImplicitParam(name = "Authorization", value = "add here your token", paramType = "header", dataType = "string", required = true)
    })
    @Secured({"ROLE_ADMIN", "ROLE_TEST_LEAD"})
    @DeleteMapping("/{projectId}/user/{userId}")
    public ResponseEntity<Void> removeUserFromProject(@PathVariable("projectId") long projectId,
                                                      @PathVariable("userId") long userId) {
        projectService.removeUserFromProject(projectId, userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
