package com.epam.test_generator.controllers;


import com.epam.test_generator.dto.StepDTO;
import com.epam.test_generator.dto.ValidationErrorsDTO;
import com.epam.test_generator.services.StepService;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StepController {

    @Autowired
    private StepService stepService;

    @ApiOperation(value = "Get all steps from the case", nickname = "getSteps")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK",
            response = StepDTO.class, responseContainer = "List"),
        @ApiResponse(code = 404, message = "Suit/Case not found")
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "suitId", value = "ID of suit which contains the case",
            required = true, dataType = "long", paramType = "path"),
        @ApiImplicitParam(name = "caseId", value = "ID of case which contains steps",
            required = true, dataType = "long", paramType = "path")
    })
    @RequestMapping(value = "/suits/{suitId}/cases/{caseId}/steps",
        method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<StepDTO>> getStepsByCaseId(@PathVariable("suitId") long suitId,
                                                          @PathVariable("caseId") long caseId) {

        return new ResponseEntity<>(stepService.getStepsByCaseId(suitId, caseId), HttpStatus.OK);
    }

    @ApiOperation(value = "Get step by id", nickname = "getStep")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK", response = StepDTO.class),
        @ApiResponse(code = 404, message = "Suit/Case/Step not found")
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "suitId", value = "ID of suit which contains the case",
            required = true, dataType = "long", paramType = "path"),
        @ApiImplicitParam(name = "caseId", value = "ID of case which contains the step",
            required = true, dataType = "long", paramType = "path"),
        @ApiImplicitParam(name = "stepId", value = "ID of step to return",
            required = true, dataType = "long", paramType = "path")
    })
    @RequestMapping(value = "/suits/{suitId}/cases/{caseId}/steps/{stepId}",
        method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<StepDTO> getStepByStepId(@PathVariable(value = "suitId") long suitId,
                                                   @PathVariable("caseId") long caseId,
                                                   @PathVariable(value = "stepId") long stepId) {

        return new ResponseEntity<>(stepService.getStep(suitId, caseId, stepId),
            HttpStatus.OK);

    }

    @ApiOperation(value = "Add a new step to the case", nickname = "addStepToCase")
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "Created", response = Long.class),
        @ApiResponse(code = 400, message = "Invalid input", response = ValidationErrorsDTO.class),
        @ApiResponse(code = 404, message = "Suit/Case not found")
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "suitId", value = "ID of suit which contains the case",
            required = true, dataType = "long", paramType = "path"),
        @ApiImplicitParam(name = "caseId", value = "ID of case which will be added a new step",
            required = true, dataType = "long", paramType = "path"),
        @ApiImplicitParam(name = "stepDTO", value = "Added step object",
            required = true, dataType = "StepDTO", paramType = "body")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "/suits/{suitId}/cases/{caseId}/steps", method = RequestMethod.POST,
        consumes = "application/json", produces = "application/json")
    public ResponseEntity<Long> addStepToCase(@PathVariable("suitId") long suitId,
                                              @PathVariable("caseId") long caseId,
                                              @RequestBody @Valid StepDTO stepDTO) {

        return new ResponseEntity<>(stepService.addStepToCase(suitId, caseId, stepDTO),
            HttpStatus.CREATED);
    }

    @ApiOperation(value = "Update step by id", nickname = "updateStep")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 400, message = "Invalid input", response = ValidationErrorsDTO.class),
        @ApiResponse(code = 404, message = "Suit/Case/Step not found")
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "suitId", value = "ID of suit which contains the case",
            required = true, dataType = "long", paramType = "path"),
        @ApiImplicitParam(name = "caseId", value = "ID of case which contains the step",
            required = true, dataType = "long", paramType = "path"),
        @ApiImplicitParam(name = "stepId", value = "ID of step to update",
            required = true, dataType = "long", paramType = "path"),
        @ApiImplicitParam(name = "stepDTO", value = "Updated step object",
            required = true, dataType = "StepDTO", paramType = "boy")
    })
    @RequestMapping(value = "/suits/{suitId}/cases/{caseId}/steps/{stepId}", method = RequestMethod.PUT, consumes = "application/json")
    public ResponseEntity<Void> updateStep(@PathVariable("suitId") long suitId,
                                           @PathVariable("caseId") long caseId,
                                           @PathVariable("stepId") long stepId,
                                           @RequestBody @Valid StepDTO stepDTO) {
        stepService.updateStep(suitId, caseId, stepId, stepDTO);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "Delete step by id", nickname = "removeCase")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "Suit/Case/Step not found")
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "suitId", value = "ID of suit which contains the case",
            required = true, dataType = "long", paramType = "path"),
        @ApiImplicitParam(name = "caseId", value = "ID of case which contains the step",
            required = true, dataType = "long", paramType = "path"),
        @ApiImplicitParam(name = "stepId", value = "ID of step to delete",
            required = true, dataType = "long", paramType = "path")
    })
    @RequestMapping(value = "/suits/{suitId}/cases/{caseId}/steps/{stepId}",
        method = RequestMethod.DELETE)
    public ResponseEntity<Void> removeCase(@PathVariable("suitId") long suitId,
                                           @PathVariable("caseId") long caseId,
                                           @PathVariable("stepId") long stepId) {
        stepService.removeStep(suitId, caseId, stepId);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
