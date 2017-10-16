package com.epam.test_generator.controllers;

import com.epam.test_generator.dto.StepSuggestionDTO;
import com.epam.test_generator.services.StepSuggestionService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class StepSuggestionController {

    @Autowired
    private StepSuggestionService stepSuggestionService;

    @ApiOperation(value = "Get all step suggestions", nickname = "getStepsSuggestions")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = StepSuggestionDTO.class, responseContainer = "List")
    })
    @RequestMapping(value = "/stepSuggestions", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<StepSuggestionDTO>> getStepsSuggestions() {

        return new ResponseEntity<>(stepSuggestionService.getStepsSuggestions(), HttpStatus.OK);
    }

    @ApiOperation(value = "Get all step suggestions by type", nickname = "getStepsSuggestionsByType")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = StepSuggestionDTO.class, responseContainer = "List")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "typeId", value = "Type of step suggestion that we want to return", required = true, dataType = "long", paramType = "query")
    })
    @RequestMapping(value = "/stepSuggestions/{typeId}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<StepSuggestionDTO>> getStepsSuggestionsByType(@PathVariable("typeId") long typeId) {

        return new ResponseEntity<>(stepSuggestionService.getStepsSuggestionsByType(typeId), HttpStatus.OK);
    }

    @ApiOperation(value = "Add a new step suggestion", nickname = "addStepSuggestion")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created", response = Long.class),
            @ApiResponse(code = 400, message = "Invalid input")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "stepSuggestionDTO", value = "Added step suggestion object", required = true, dataType = "StepSuggestionDTO", paramType = "body")
    })
    @RequestMapping(value = "/stepSuggestions", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity<Long> addStepSuggestion(@RequestBody @Valid StepSuggestionDTO stepSuggestionDTO) {

        return new ResponseEntity<>(stepSuggestionService.addStepSuggestion(stepSuggestionDTO), HttpStatus.OK);
    }

    @ApiOperation(value = "Update step suggestion by id", nickname = "updateStepSuggestion")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Invalid input"),
            @ApiResponse(code = 404, message = "StepSuggestion not found")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "stepSuggestionId", value = "ID of step suggestion to update", required = true, dataType = "long", paramType = "query"),
            @ApiImplicitParam(name = "stepSuggestionDTO", value = "Updated step suggestion object", required = true, dataType = "StepSuggestionDTO", paramType = "body")
    })
    @RequestMapping(value = "/stepSuggestions/{stepSuggestionId}", method = RequestMethod.PUT, produces = "application/json")
    public ResponseEntity<Void> updateStepSuggestion(@PathVariable("stepSuggestionId") long stepSuggestionId,
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
            @ApiImplicitParam(name = "stepSuggestionId", value = "ID of step suggestion to delete", required = true, dataType = "long", paramType = "query")
    })
    @RequestMapping(value = "/stepSuggestions/{stepSuggestionId}", method = RequestMethod.DELETE, produces = "application/json")
    public ResponseEntity<Void> removeStepSuggestion(@PathVariable("stepSuggestionId") long stepSuggestionId) {
        stepSuggestionService.removeStepSuggestion(stepSuggestionId);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
