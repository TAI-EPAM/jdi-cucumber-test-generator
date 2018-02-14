package com.epam.test_generator.controllers;

import com.epam.test_generator.dto.ChangeUserRoleDTO;
import com.epam.test_generator.dto.ProjectDTO;
import com.epam.test_generator.dto.UserDTO;
import com.epam.test_generator.services.AdminService;
import com.epam.test_generator.services.ProjectService;
import com.epam.test_generator.services.UserService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * Controls performing of administration actions such as change user role or remove project.
 * All included actions require user to have role ADMIN.
 */
@RestController
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProjectService projectService;

    @ApiImplicitParam(name = "Authorization", value = "add here your token", paramType = "header", dataType = "string", required = true)
    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/admin/changeroles", method = RequestMethod.PUT)
    public ResponseEntity changeUserRole(@RequestBody @Valid ChangeUserRoleDTO changeUserRoleDTO) {

        adminService.changeUserRole(changeUserRoleDTO);

        return new ResponseEntity(HttpStatus.OK);
    }

    @ApiImplicitParam(name = "Authorization", value = "add here your token", paramType = "header", dataType = "string", required = true)
    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/admin/users", method = RequestMethod.GET)
    public ResponseEntity<List<UserDTO>> getUsers() {
        return new ResponseEntity<>(userService.getUsers(), HttpStatus.OK);
    }

    @ApiOperation(value = "Get all projects", nickname = "getProjects")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK",
            response = ProjectDTO.class, responseContainer = "List")
    })
    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/admin/projects", method = RequestMethod.GET, produces = "application/json")
    @ApiImplicitParam(name = "Authorization", value = "add here your token", paramType = "header", dataType = "string", required = true)
    public ResponseEntity<List<ProjectDTO>> getProjects() {
        return new ResponseEntity<>(projectService.getProjects(), HttpStatus.OK);
    }

    @ApiOperation(value = "Delete project by id", nickname = "removeProject")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "project not found")
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "projectId", value = "ID of project to delete",
            required = true, dataType = "long", paramType = "path"),
        @ApiImplicitParam(name = "Authorization", value = "add here your token", paramType = "header", dataType = "string", required = true)
    })
    @Secured({"ROLE_ADMIN"})
    @RequestMapping(value = "admin/projects/{projectId}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> removeProject(@PathVariable("projectId") long projectId) {
        projectService.removeProject(projectId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
