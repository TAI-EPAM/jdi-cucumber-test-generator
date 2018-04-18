package com.epam.test_generator.controllers.step;


import com.epam.test_generator.controllers.step.request.StepCreateDTO;
import com.epam.test_generator.controllers.step.request.StepUpdateDTO;
import com.epam.test_generator.controllers.step.response.StepDTO;
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
 * Allow work with steps of specified case.
 */
@RestController
@RequestMapping("/projects/{projectId}/suits/{suitId}/cases/{caseId}/steps")
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
        @ApiImplicitParam(name = "projectId", value = "ID of project",
            required = true, dataType = "long", paramType = "path"),
        @ApiImplicitParam(name = "suitId", value = "ID of suit which contains the case",
            required = true, dataType = "long", paramType = "path"),
        @ApiImplicitParam(name = "caseId", value = "ID of case which contains steps",
            required = true, dataType = "long", paramType = "path"),
        @ApiImplicitParam(name = "Authorization", value = "add here your token", paramType = "header", dataType = "string", required = true)
    })
    @Secured({"ROLE_ADMIN", "ROLE_TEST_ENGINEER", "ROLE_TEST_LEAD", "ROLE_GUEST"})
    @GetMapping
    public ResponseEntity<List<StepDTO>> getStepsByCaseId(@PathVariable("projectId") long projectId,
                                                          @PathVariable("suitId") long suitId,
                                                          @PathVariable("caseId") long caseId) {

        return new ResponseEntity<>(stepService.getStepsByCaseId(projectId, suitId, caseId),
            HttpStatus.OK);
    }

    @ApiOperation(value = "Get step by id", nickname = "getStep")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK", response = StepDTO.class),
        @ApiResponse(code = 404, message = "Suit/Case/Step not found")
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "projectId", value = "ID of project",
            required = true, dataType = "long", paramType = "path"),
        @ApiImplicitParam(name = "suitId", value = "ID of suit which contains the case",
            required = true, dataType = "long", paramType = "path"),
        @ApiImplicitParam(name = "caseId", value = "ID of case which contains the step",
            required = true, dataType = "long", paramType = "path"),
        @ApiImplicitParam(name = "stepId", value = "ID of step to return",
            required = true, dataType = "long", paramType = "path"),
        @ApiImplicitParam(name = "Authorization", value = "add here your token", paramType = "header", dataType = "string", required = true)
    })
    @Secured({"ROLE_ADMIN", "ROLE_TEST_ENGINEER", "ROLE_TEST_LEAD", "ROLE_GUEST"})
    @GetMapping("/{stepId}")
    public ResponseEntity<StepDTO> getStepByStepId(@PathVariable("projectId") long projectId,
                                                   @PathVariable(value = "suitId") long suitId,
                                                   @PathVariable("caseId") long caseId,
                                                   @PathVariable(value = "stepId") long stepId) {

        return new ResponseEntity<>(stepService.getStep(projectId, suitId, caseId, stepId),
            HttpStatus.OK);

    }

    @ApiOperation(value = "Add a new step to the case", nickname = "addStepToCase")
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "Created", response = Long.class),
        @ApiResponse(code = 400, message = "Invalid input", response = ValidationErrorsDTO.class),
        @ApiResponse(code = 404, message = "Suit/Case not found")
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "projectId", value = "ID of project",
            required = true, dataType = "long", paramType = "path"),
        @ApiImplicitParam(name = "suitId", value = "ID of suit which contains the case",
            required = true, dataType = "long", paramType = "path"),
        @ApiImplicitParam(name = "caseId", value = "ID of case which will be added a new step",
            required = true, dataType = "long", paramType = "path"),
        @ApiImplicitParam(name = "stepCreateDTO", value = "Added step object",
            required = true, dataType = "StepCreateDTO", paramType = "body"),
        @ApiImplicitParam(name = "Authorization", value = "add here your token", paramType = "header", dataType = "string", required = true)
    })
    @Secured({"ROLE_ADMIN", "ROLE_TEST_ENGINEER", "ROLE_TEST_LEAD"})
    @PostMapping
    public ResponseEntity<Long> addStepToCase(@PathVariable("projectId") long projectId,
                                              @PathVariable("suitId") long suitId,
                                              @PathVariable("caseId") long caseId,
                                              @RequestBody @Valid StepCreateDTO stepCreateDTO) {

        return new ResponseEntity<>(
            stepService.addStepToCase(projectId, suitId, caseId, stepCreateDTO),
            HttpStatus.CREATED);
    }

    @ApiOperation(value = "Update step by id", nickname = "updateStep")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 400, message = "Invalid input", response = ValidationErrorsDTO.class),
        @ApiResponse(code = 404, message = "Suit/Case/Step not found")
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "projectId", value = "ID of project",
            required = true, dataType = "long", paramType = "path"),
        @ApiImplicitParam(name = "suitId", value = "ID of suit which contains the case",
            required = true, dataType = "long", paramType = "path"),
        @ApiImplicitParam(name = "caseId", value = "ID of case which contains the step",
            required = true, dataType = "long", paramType = "path"),
        @ApiImplicitParam(name = "stepId", value = "ID of step to update",
            required = true, dataType = "long", paramType = "path"),
        @ApiImplicitParam(name = "stepUpdateDTO", value = "Updated step object",
            required = true, dataType = "StepUpdateDTO", paramType = "boy"),
        @ApiImplicitParam(name = "Authorization", value = "add here your token", paramType = "header", dataType = "string", required = true)
    })
    @Secured({"ROLE_ADMIN", "ROLE_TEST_ENGINEER", "ROLE_TEST_LEAD"})
    @PutMapping("/{stepId}")
    public ResponseEntity<Void> updateStep(@PathVariable("projectId") long projectId,
                                           @PathVariable("suitId") long suitId,
                                           @PathVariable("caseId") long caseId,
                                           @PathVariable("stepId") long stepId,
                                           @RequestBody @Valid StepUpdateDTO stepDTO) {
        stepService.updateStep(projectId, suitId, caseId, stepId, stepDTO);

        return new ResponseEntity<>(HttpStatus.OK);
    }


    @ApiOperation(value = "Delete step by id", nickname = "removeCase")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "Suit/Case/Step not found")
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "projectId", value = "ID of project",
            required = true, dataType = "long", paramType = "path"),
        @ApiImplicitParam(name = "suitId", value = "ID of suit which contains the case",
            required = true, dataType = "long", paramType = "path"),
        @ApiImplicitParam(name = "caseId", value = "ID of case which contains the step",
            required = true, dataType = "long", paramType = "path"),
        @ApiImplicitParam(name = "stepId", value = "ID of step to delete",
            required = true, dataType = "long", paramType = "path"),
        @ApiImplicitParam(name = "Authorization", value = "add here your token", paramType = "header", dataType = "string", required = true)
    })
    @Secured({"ROLE_ADMIN", "ROLE_TEST_ENGINEER", "ROLE_TEST_LEAD"})
    @DeleteMapping("/{stepId}")
    public ResponseEntity<Void> removeCase(@PathVariable("projectId") long projectId,
                                           @PathVariable("suitId") long suitId,
                                           @PathVariable("caseId") long caseId,
                                           @PathVariable("stepId") long stepId) {
        stepService.removeStep(projectId, suitId, caseId, stepId);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
