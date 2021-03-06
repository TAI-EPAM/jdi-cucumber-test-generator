package com.epam.test_generator.controllers.stepsuggestion;

import com.epam.test_generator.controllers.stepsuggestion.request.StepSuggestionCreateDTO;
import com.epam.test_generator.controllers.stepsuggestion.request.StepSuggestionUpdateDTO;
import com.epam.test_generator.controllers.stepsuggestion.response.StepSuggestionDTO;
import com.epam.test_generator.dto.TokenDTO;
import com.epam.test_generator.entities.StepType;
import com.epam.test_generator.services.StepSuggestionService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/projects/{projectId}/step-suggestions")
public class StepSuggestionController {

    /**
     * Handle step suggestions for specific project.
     */

    @Autowired
    private StepSuggestionService stepSuggestionService;

    @ApiOperation(
        value = "Get step suggestions by type and pages",
        nickname = "getStepsSuggestions")
    @ApiResponses(value = {
        @ApiResponse(
            code = 200,
            message = "OK",
            response = StepSuggestionDTO.class,
            responseContainer = "List")
    })
    @ApiImplicitParams({
        @ApiImplicitParam(
            name = TokenDTO.TOKEN_HEADER,
            value = "add here your token",
            paramType = "header",
            dataType = "string",
            required = true),
        @ApiImplicitParam(
            name = "projectId",
            value = "Id of project",
            paramType = "path",
            dataType = "long",
            required = true),
        @ApiImplicitParam(
            name = "stepType",
            value = "Type of step suggestion that we want to return",
            dataType = "StepType",
            paramType = "query",
            required = true),
        @ApiImplicitParam(
            name = "page",
            value = "page",
            paramType = "query",
            dataType = "string",
            required = true),
        @ApiImplicitParam(
            name = "size",
            value = "size",
            paramType = "query",
            dataType = "string",
            required = true)
    })
    @Secured({"ROLE_ADMIN", "ROLE_TEST_ENGINEER", "ROLE_TEST_LEAD", "ROLE_GUEST"})
    @GetMapping
    public ResponseEntity<List<StepSuggestionDTO>> getStepsSuggestions(
        @PathVariable("projectId") Long projectId,
        @RequestParam(value = "stepType") StepType stepType,
        @RequestParam(value = "page") Integer pageNumber,
        @RequestParam(value = "size") Integer pageSize) {

        return new ResponseEntity<>(
            stepSuggestionService.getStepsSuggestions(projectId, stepType, pageNumber, pageSize),
            HttpStatus.OK);
    }

    @ApiOperation(value = "Get all step suggestions by type",
        nickname = "getStepsSuggestionsByType")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK",
            response = StepSuggestionDTO.class, responseContainer = "List")
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "projectId", value = "Id of project", paramType = "path",
            dataType = "long", required = true),
        @ApiImplicitParam(name = "stepType",
            value = "Type of step suggestion that we want to return",
            required = true, dataType = "StepType", paramType = "path"),
        @ApiImplicitParam(name = TokenDTO.TOKEN_HEADER, value = "add here your token",
            paramType = "header", dataType = "string", required = true)
    })
    @Secured({"ROLE_ADMIN", "ROLE_TEST_ENGINEER", "ROLE_TEST_LEAD", "ROLE_GUEST"})
    @GetMapping("/{stepType}")
    public ResponseEntity<List<StepSuggestionDTO>> getStepsSuggestionsByType(
        @PathVariable("projectId") Long projectId,
        @PathVariable("stepType") StepType stepType
    ) {
        return new ResponseEntity<>(
            stepSuggestionService.getStepsSuggestionsByType(projectId, stepType),
            HttpStatus.OK
        );
    }

    @ApiOperation(value = "Add a new step suggestion", nickname = "addStepSuggestion")
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "Created", response = Long.class),
        @ApiResponse(code = 400, message = "Invalid input")
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "projectId", value = "Id of project", paramType = "path",
            dataType = "long", required = true),
        @ApiImplicitParam(name = "stepSuggestionCreateDTO", value = "Added step suggestion object",
            required = true, dataType = "StepSuggestionCreateDTO", paramType = "body"),
        @ApiImplicitParam(name = TokenDTO.TOKEN_HEADER, value = "add here your token",
            paramType = "header", dataType = "string", required = true)
    })
    @Secured({"ROLE_ADMIN", "ROLE_TEST_ENGINEER", "ROLE_TEST_LEAD"})
    @PostMapping
    public ResponseEntity<StepSuggestionDTO> addStepSuggestion(
        @PathVariable("projectId") Long projectId,
        @RequestBody @Valid StepSuggestionCreateDTO stepSuggestionCreateDTO) {

        return new ResponseEntity<>(
            stepSuggestionService.addStepSuggestion(projectId, stepSuggestionCreateDTO),
            HttpStatus.CREATED);
    }

    @ApiOperation(value = "Update step suggestion by id", nickname = "updateStepSuggestion")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 400, message = "Invalid input"),
        @ApiResponse(code = 404, message = "DefaultStepSuggestion not found"),
        @ApiResponse(code = 409, message = "DefaultStepSuggestion already modified")
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "projectId", value = "Id of project", paramType = "path",
            dataType = "long", required = true),
        @ApiImplicitParam(name = "stepSuggestionId", value = "ID of step suggestion to update",
            required = true, dataType = "long", paramType = "path"),
        @ApiImplicitParam(name = "stepSuggestionUpdateDTO", value = "Updated step suggestion object",
            required = true, dataType = "StepSuggestionUpdateDTO", paramType = "body"),
        @ApiImplicitParam(name = TokenDTO.TOKEN_HEADER, value = "add here your token",
            paramType = "header", dataType = "string", required = true)
    })
    @Secured({"ROLE_ADMIN", "ROLE_TEST_ENGINEER", "ROLE_TEST_LEAD"})
    @PutMapping("/{stepSuggestionId}")
    public ResponseEntity<Void> updateStepSuggestion(
        @PathVariable("projectId") Long projectId,
        @PathVariable("stepSuggestionId") long stepSuggestionId,
        @RequestBody @Valid StepSuggestionUpdateDTO stepSuggestionUpdateDTO) {
        stepSuggestionService.updateStepSuggestion(
            projectId,
            stepSuggestionId,
            stepSuggestionUpdateDTO
        );

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "Delete step suggestion by id", nickname = "removeStepSuggestion")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 400, message = "Invalid input"),
        @ApiResponse(code = 404, message = "DefaultStepSuggestion not found")
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "projectId", value = "Id of project", paramType = "path",
            dataType = "long", required = true),
        @ApiImplicitParam(name = "stepSuggestionId", value = "ID of step suggestion to delete",
            required = true, dataType = "long", paramType = "path"),
        @ApiImplicitParam(name = TokenDTO.TOKEN_HEADER, value = "add here your token",
            paramType = "header", dataType = "string", required = true)
    })
    @Secured({"ROLE_ADMIN", "ROLE_TEST_ENGINEER", "ROLE_TEST_LEAD"})
    @DeleteMapping("/{stepSuggestionId}")
    public ResponseEntity<Void> removeStepSuggestion(
        @PathVariable("projectId") Long projectId,
        @PathVariable("stepSuggestionId") long stepSuggestionId) {
        stepSuggestionService.removeStepSuggestion(projectId, stepSuggestionId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "Find steps suggestions belong to project", nickname = "findStepsSuggestions")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 400, message = "Invalid input")
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = TokenDTO.TOKEN_HEADER, value = "add here your token",
            paramType = "header", dataType = "string", required = true),
        @ApiImplicitParam(name = "text", value = "String for search", paramType = "query",
            dataType = "string", required = true),
        @ApiImplicitParam(name = "pageNumber", value = "Number of page", paramType = "query",
            dataType = "int", required = true),
        @ApiImplicitParam(name = "pageSize", value = "Page size", paramType = "query",
            dataType = "int", required = true)
    })
    @Secured({"ROLE_ADMIN", "ROLE_TEST_ENGINEER", "ROLE_TEST_LEAD", "ROLE_GUEST"})
    @GetMapping(value = "/search")
    public ResponseEntity<List<StepSuggestionDTO>> searchStepsSuggestions(
        @PathVariable("projectId") Long projectId,
        @RequestParam("text") String searchString,
        @RequestParam("pageNumber") int pageNumber,
        @RequestParam("pageSize") int pageSize) {

        List<StepSuggestionDTO> foundStepsSuggestions = stepSuggestionService
            .findStepsSuggestions(projectId, searchString, pageNumber, pageSize);

        return new ResponseEntity<>(foundStepsSuggestions, HttpStatus.OK);
    }
}
