package com.epam.test_generator.controllers;

import com.epam.test_generator.dto.StepSuggestionDTO;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Handle step suggestions.
 */
@RestController
public class StepSuggestionController {

    @Autowired
    private StepSuggestionService stepSuggestionService;

    @ApiOperation(value = "Get all step suggestions", nickname = "getStepsSuggestions")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK", response = StepSuggestionDTO.class,
            responseContainer = "List")
    })
    @Secured({"ROLE_ADMIN", "ROLE_TEST_ENGINEER", "ROLE_TEST_LEAD", "ROLE_GUEST"})
    @RequestMapping(value = "/stepSuggestions", method = RequestMethod.GET,
        produces = "application/json")
    @ApiImplicitParam(name = "Authorization", value = "add here your token", paramType = "header", dataType = "string", required = true)
    public ResponseEntity<List<StepSuggestionDTO>> getStepsSuggestions() {

        return new ResponseEntity<>(stepSuggestionService.getStepsSuggestions(), HttpStatus.OK);
    }

    @ApiOperation(value = "Get all step suggestions by type",
        nickname = "getStepsSuggestionsByType")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK",
            response = StepSuggestionDTO.class, responseContainer = "List")
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "stepType",
            value = "Type of step suggestion that we want to return",
            required = true, dataType = "StepType", paramType = "path"),
        @ApiImplicitParam(name = "Authorization", value = "add here your token", paramType = "header", dataType = "string", required = true)
    })
    @Secured({"ROLE_ADMIN", "ROLE_TEST_ENGINEER", "ROLE_TEST_LEAD", "ROLE_GUEST"})
    @RequestMapping(value = "/stepSuggestions/{stepType}",
        method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<StepSuggestionDTO>> getStepsSuggestionsByType(
        @PathVariable("stepType") StepType stepType) {

        return new ResponseEntity<>(stepSuggestionService.getStepsSuggestionsByType(stepType),
            HttpStatus.OK);
    }

    @ApiOperation(value = "Add a new step suggestion", nickname = "addStepSuggestion")
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "Created", response = Long.class),
        @ApiResponse(code = 400, message = "Invalid input")
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "stepSuggestionDTO", value = "Added step suggestion object",
            required = true, dataType = "StepSuggestionDTO", paramType = "body"),
        @ApiImplicitParam(name = "Authorization", value = "add here your token", paramType = "header", dataType = "string", required = true)
    })
    @Secured({"ROLE_ADMIN", "ROLE_TEST_ENGINEER", "ROLE_TEST_LEAD"})
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "/stepSuggestions", method = RequestMethod.POST,
        consumes = "application/json", produces = "application/json")
    public ResponseEntity<Long> addStepSuggestion(
        @RequestBody @Valid StepSuggestionDTO stepSuggestionDTO) {

        return new ResponseEntity<>(stepSuggestionService.addStepSuggestion(stepSuggestionDTO),
            HttpStatus.OK);
    }

    @ApiOperation(value = "Update step suggestion by id", nickname = "updateStepSuggestion")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 400, message = "Invalid input"),
        @ApiResponse(code = 404, message = "StepSuggestion not found")
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "stepSuggestionId", value = "ID of step suggestion to update",
            required = true, dataType = "long", paramType = "path"),
        @ApiImplicitParam(name = "stepSuggestionDTO", value = "Updated step suggestion object",
            required = true, dataType = "StepSuggestionDTO", paramType = "body"),
        @ApiImplicitParam(name = "Authorization", value = "add here your token", paramType = "header", dataType = "string", required = true)
    })
    @Secured({"ROLE_ADMIN", "ROLE_TEST_ENGINEER", "ROLE_TEST_LEAD"})
    @RequestMapping(value = "/stepSuggestions/{stepSuggestionId}", method = RequestMethod.PUT, produces = "application/json")
    public ResponseEntity<Void> updateStepSuggestion(
        @PathVariable("stepSuggestionId") long stepSuggestionId,
        @RequestBody @Valid StepSuggestionDTO stepSuggestionDTO) {
        stepSuggestionService.updateStepSuggestion(stepSuggestionId, stepSuggestionDTO);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "Delete step suggestion by id", nickname = "removeStepSuggestion")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 400, message = "Invalid input"),
        @ApiResponse(code = 404, message = "StepSuggestion not found")
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "stepSuggestionId", value = "ID of step suggestion to delete",
            required = true, dataType = "long", paramType = "path"),
        @ApiImplicitParam(name = "Authorization", value = "add here your token", paramType = "header", dataType = "string", required = true)
    })
    @Secured({"ROLE_ADMIN", "ROLE_TEST_ENGINEER", "ROLE_TEST_LEAD"})
    @RequestMapping(value = "/stepSuggestions/{stepSuggestionId}",
        method = RequestMethod.DELETE, produces = "application/json")
    public ResponseEntity<Void> removeStepSuggestion(
        @PathVariable("stepSuggestionId") long stepSuggestionId) {
        stepSuggestionService.removeStepSuggestion(stepSuggestionId);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
