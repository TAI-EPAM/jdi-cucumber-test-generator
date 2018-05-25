package com.epam.test_generator.controllers.stepsuggestion;

import com.epam.test_generator.controllers.stepsuggestion.response.StepSuggestionDTO;
import com.epam.test_generator.entities.StepType;
import com.epam.test_generator.services.DefaultStepSuggestionService;
import com.epam.test_generator.services.exceptions.BadRequestException;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Handle step suggestions.
 */
@RestController
@RequestMapping("/step-suggestions")
public class DefaultStepSuggestionController {

    @Autowired
    private DefaultStepSuggestionService defaultStepSuggestionService;

    @ApiOperation(
            value = "Get default step suggestions by type and pages",
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
            @RequestParam(value = "stepType") StepType stepType,
            @RequestParam(value = "page") Integer pageNumber,
            @RequestParam(value = "size") Integer pageSize) {

        return new ResponseEntity<>(
            defaultStepSuggestionService.getStepsSuggestionsByTypeAndPage(stepType, pageNumber,
            pageSize), HttpStatus.OK);
    }

    @ApiOperation(value = "Get default step suggestions by type",
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

        return new ResponseEntity<>(defaultStepSuggestionService.getStepsSuggestionsByType(stepType),
                HttpStatus.OK);
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
            dataType = "int", allowableValues = "range[1, 5]", required = true)
    })
    @Secured({"ROLE_ADMIN", "ROLE_TEST_ENGINEER", "ROLE_TEST_LEAD", "ROLE_GUEST"})
    @GetMapping(value = "/search")
    public ResponseEntity<List<StepSuggestionDTO>> searchStepsSuggestions(
        @RequestParam("text") String searchString,
        @RequestParam("limit") int limit) {

        if (limit < 1) {
            throw new BadRequestException("Limit must not be less than one!");
        }

        List<StepSuggestionDTO> foundStepsSuggestions = defaultStepSuggestionService
            .findStepsSuggestions(searchString, limit);

        return new ResponseEntity<>(foundStepsSuggestions, HttpStatus.OK);
    }
}
