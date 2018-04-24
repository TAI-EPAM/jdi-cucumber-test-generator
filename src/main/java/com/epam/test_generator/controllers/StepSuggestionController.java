package com.epam.test_generator.controllers;

import com.epam.test_generator.dto.StepSuggestionCreateDTO;
import com.epam.test_generator.dto.StepSuggestionDTO;
import com.epam.test_generator.dto.StepSuggestionUpdateDTO;
import com.epam.test_generator.entities.StepType;
import com.epam.test_generator.services.StepSuggestionService;
import com.epam.test_generator.services.exceptions.BadRequestException;
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
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Handle step suggestions.
 */
@RestController
@RequestMapping("/step-suggestions")
public class StepSuggestionController {

    @Autowired
    private StepSuggestionService stepSuggestionService;

    @ApiOperation(
            value = "Get all step suggestions",
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
                    name = "Authorization",
                    value = "add here your token",
                    paramType = "header",
                    dataType = "string",
                    required = true),
            @ApiImplicitParam(
                    name = "stepType",
                    value = "Type of step suggestion that we want to return",
                    dataType = "StepType",
                    paramType = "query"),
            @ApiImplicitParam(
                    name = "page",
                    value = "page",
                    paramType = "query",
                    dataType = "string"),
            @ApiImplicitParam(
                    name = "size",
                    value = "size",
                    paramType = "query",
                    dataType = "string")
    })
    @Secured({"ROLE_ADMIN", "ROLE_TEST_ENGINEER", "ROLE_TEST_LEAD", "ROLE_GUEST"})
    @GetMapping
    public ResponseEntity<List<StepSuggestionDTO>> getStepsSuggestions(
            @RequestParam(value = "stepType", required = false) StepType stepType,
            @RequestParam(value = "page", required = false) Integer pageNumber,
            @RequestParam(value = "size", required = false) Integer pageSize) {

        return new ResponseEntity<>(stepSuggestionService.getStepsSuggestions(stepType, pageNumber,
            pageSize), HttpStatus.OK);
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
            @ApiImplicitParam(name = "Authorization", value = "add here your token",
                paramType = "header", dataType = "string", required = true)
    })
    @Secured({"ROLE_ADMIN", "ROLE_TEST_ENGINEER", "ROLE_TEST_LEAD", "ROLE_GUEST"})
    @GetMapping("/{stepType}")
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
        @ApiImplicitParam(name = "stepSuggestionCreateDTO", value = "Added step suggestion object",
            required = true, dataType = "StepSuggestionCreateDTO", paramType = "body"),
        @ApiImplicitParam(name = "Authorization", value = "add here your token",
            paramType = "header", dataType = "string", required = true)
    })
    @Secured({"ROLE_ADMIN", "ROLE_TEST_ENGINEER", "ROLE_TEST_LEAD"})
    @PostMapping
    public ResponseEntity<Long> addStepSuggestion(
        @RequestBody @Valid StepSuggestionCreateDTO stepSuggestionCreateDTO) {

        return new ResponseEntity<>(stepSuggestionService.addStepSuggestion(stepSuggestionCreateDTO),
            HttpStatus.OK);
    }

    @ApiOperation(value = "Update step suggestion by id", nickname = "updateStepSuggestion")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 400, message = "Invalid input"),
        @ApiResponse(code = 404, message = "StepSuggestion not found"),
        @ApiResponse(code = 409, message = "StepSuggestion already modified")
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "stepSuggestionId", value = "ID of step suggestion to update",
            required = true, dataType = "long", paramType = "path"),
        @ApiImplicitParam(name = "stepSuggestionUpdateDTO", value = "Updated step suggestion object",
            required = true, dataType = "StepSuggestionUpdateDTO", paramType = "body"),
        @ApiImplicitParam(name = "Authorization", value = "add here your token",
            paramType = "header", dataType = "string", required = true)
    })
    @Secured({"ROLE_ADMIN", "ROLE_TEST_ENGINEER", "ROLE_TEST_LEAD"})
    @PutMapping("/{stepSuggestionId}")
    public ResponseEntity<Void> updateStepSuggestion(
        @PathVariable("stepSuggestionId") long stepSuggestionId,
        @RequestBody @Valid StepSuggestionUpdateDTO stepSuggestionUpdateDTO) {
        stepSuggestionService.updateStepSuggestion(stepSuggestionId, stepSuggestionUpdateDTO);

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
        @ApiImplicitParam(name = "Authorization", value = "add here your token",
            paramType = "header", dataType = "string", required = true)
    })
    @Secured({"ROLE_ADMIN", "ROLE_TEST_ENGINEER", "ROLE_TEST_LEAD"})
    @DeleteMapping("/{stepSuggestionId}")
    public ResponseEntity<Void> removeStepSuggestion(
        @PathVariable("stepSuggestionId") long stepSuggestionId) {
        stepSuggestionService.removeStepSuggestion(stepSuggestionId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "Find steps suggestions", nickname = "findStepsSuggestions")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 400, message = "Invalid input")
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "add here your token",
            paramType = "header", dataType = "string", required = true),
        @ApiImplicitParam(name = "text", value = "String for search", paramType = "query",
            dataType = "string", required = true),
        @ApiImplicitParam(name = "limit", value = "Limit count of results", paramType = "query",
            dataType = "int", required = true)
    })
    @Secured({"ROLE_ADMIN", "ROLE_TEST_ENGINEER", "ROLE_TEST_LEAD", "ROLE_GUEST"})
    @GetMapping(value = "/search")
    public ResponseEntity<List<StepSuggestionDTO>> searchStepsSuggestions(
        @RequestParam("text") String searchString,
        @RequestParam("limit") int limit) {
        List<StepSuggestionDTO> foundStepsSuggestions = stepSuggestionService
            .findStepsSuggestions(searchString, limit);

        return new ResponseEntity<>(foundStepsSuggestions, HttpStatus.OK);
    }
}
