package com.epam.test_generator.controllers;

import com.epam.test_generator.dto.CaseDTO;
import com.epam.test_generator.dto.SuitDTO;
import com.epam.test_generator.dto.SuitRowNumberUpdateDTO;
import com.epam.test_generator.dto.SuitUpdateDTO;
import com.epam.test_generator.dto.ValidationErrorsDTO;
import com.epam.test_generator.services.IOService;
import com.epam.test_generator.services.SuitService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Allow work with suits of specified project.
 */
@RestController
public class SuitController {

    @Autowired
    private SuitService suitService;

    @Autowired
    private IOService ioService;


    @ApiOperation(value = "Get all suits", nickname = "getSuits")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK",
            response = SuitDTO.class, responseContainer = "List")
    })

    @Secured({"ROLE_ADMIN", "ROLE_TEST_ENGINEER", "ROLE_TEST_LEAD", "ROLE_GUEST"})
    @RequestMapping(value = "/projects/{projectId}/suits", method = RequestMethod.GET,
        produces = "application/json")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "projectId", value = "ID of project with suits",
            required = true, dataType = "long", paramType = "path"),
        @ApiImplicitParam(name = "Authorization", value = "add here your token",
            paramType = "header", dataType = "string", required = true)
    })
    public ResponseEntity<List<SuitDTO>> getSuits(@PathVariable("projectId") long projectId) {
        return new ResponseEntity<>(suitService.getSuitsFromProject(projectId), HttpStatus.OK);
    }

    @ApiOperation(value = "Get suit by id", nickname = "getProjectSuit")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK", response = SuitDTO.class),
        @ApiResponse(code = 404, message = "Suit not found")
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "projectId", value = "ID of project",
            required = true, dataType = "long", paramType = "path"),
        @ApiImplicitParam(name = "suitId", value = "ID of suit to return",
            required = true, dataType = "long", paramType = "path"),
        @ApiImplicitParam(name = "Authorization", value = "add here your token",
            paramType = "header", dataType = "string", required = true)
    })
    @Secured({"ROLE_ADMIN", "ROLE_TEST_ENGINEER", "ROLE_TEST_LEAD"})
    @RequestMapping(value = "/projects/{projectId}/suits/{suitId}",
        method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<SuitDTO> getProjectSuit(@PathVariable("projectId") long projectId,
                                                  @PathVariable("suitId") long suitId) {

        return new ResponseEntity<>(suitService.getSuitDTO(projectId, suitId), HttpStatus.OK);
    }

    @ApiOperation(value = "Add a new suit to project", nickname = "createSuit")
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "Created", response = Long.class),
        @ApiResponse(code = 400, message = "Invalid input", response = ValidationErrorsDTO.class)
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "projectId", value = "ID of project",
            required = true, dataType = "long", paramType = "path"),
        @ApiImplicitParam(name = "suitDTO", value = "Added suit object",
            required = true, dataType = "SuitDTO", paramType = "body"),
        @ApiImplicitParam(name = "Authorization", value = "add here your token",
            paramType = "header", dataType = "string", required = true)
    })
    @ResponseStatus(HttpStatus.CREATED)
    @Secured({"ROLE_ADMIN", "ROLE_TEST_ENGINEER", "ROLE_TEST_LEAD"})
    @RequestMapping(value = "/projects/{projectId}/suits", method = RequestMethod.POST,
        consumes = "application/json", produces = "application/json")
    public ResponseEntity<SuitDTO> createSuit(@PathVariable("projectId") long projectId,
                                           @RequestBody @Valid SuitDTO suitDTO) {
        return new ResponseEntity<>(suitService.addSuit(projectId, suitDTO), HttpStatus.CREATED);
    }

    @ApiOperation(value = "Update suit by id", nickname = "updateSuit")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 400, message = "Invalid input", response = ValidationErrorsDTO.class),
        @ApiResponse(code = 404, message = "Suit not found")
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "projectId", value = "ID of project",
            required = true, dataType = "long", paramType = "path"),
        @ApiImplicitParam(name = "suitId", value = "ID of suit to update",
            required = true, dataType = "long", paramType = "path"),
        @ApiImplicitParam(name = "suitDTO", value = "Updated suit object",
            required = true, dataType = "SuitDTO", paramType = "body"),
        @ApiImplicitParam(name = "Authorization", value = "add here your token",
            paramType = "header", dataType = "string", required = true)
    })
    @Secured({"ROLE_ADMIN", "ROLE_TEST_ENGINEER", "ROLE_TEST_LEAD"})
    @RequestMapping(value = "/projects/{projectId}/suits/{suitId}", method = RequestMethod.PUT,
        consumes = "application/json", produces = "application/json")
    public ResponseEntity<SuitUpdateDTO> updateSuit(@PathVariable("projectId") long projectId,
                                                    @PathVariable("suitId") long suitId,
                                                    @RequestBody @Valid SuitDTO suitDTO)
        throws MethodArgumentNotValidException {

        final SuitUpdateDTO updatedSuitDTOwithFailedStepIds = suitService.updateSuit(projectId, suitId, suitDTO);

        return new ResponseEntity<>(updatedSuitDTOwithFailedStepIds, HttpStatus.OK);
    }


    @ApiOperation(value = "Update suits by rowNumber", nickname = "updateSuitRowNumber")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 400, message = "Invalid input", response = ValidationErrorsDTO.class),
        @ApiResponse(code = 404, message = "Suit not found")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "projectId", value = "ID of project",
                    required = true, dataType = "long", paramType = "path"),
            @ApiImplicitParam(name = "Authorization", value = "add here your token",
                    paramType = "header", dataType = "string", required = true)
    })
    @Secured({"ROLE_ADMIN", "ROLE_TEST_ENGINEER", "ROLE_TEST_LEAD"})
    @RequestMapping(value = "/projects/{projectId}/suits/updateRowNumbers",
            method = RequestMethod.PUT, consumes = "application/json")
    public ResponseEntity<List<SuitRowNumberUpdateDTO>> updateSuitRowNumber(
        @PathVariable("projectId") long projectId,
             @RequestBody @Valid List<SuitRowNumberUpdateDTO> rowNumberUpdates) {

        final List<SuitRowNumberUpdateDTO> updatedSuitRowNumberUpdateDTOs =
            suitService.updateSuitRowNumber(projectId, rowNumberUpdates);
        return new ResponseEntity<>(updatedSuitRowNumberUpdateDTOs, HttpStatus.OK);
    }

    @ApiOperation(value = "Delete suit by id", nickname = "removeSuit")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "Suit not found")
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "projectId", value = "ID of project",
            required = true, dataType = "long", paramType = "path"),
        @ApiImplicitParam(name = "suitId", value = "ID of suit to delete",
            required = true, dataType = "long", paramType = "path"),
        @ApiImplicitParam(name = "Authorization", value = "add here your token",
            paramType = "header", dataType = "string", required = true)
    })
    @Secured({"ROLE_ADMIN", "ROLE_TEST_ENGINEER", "ROLE_TEST_LEAD"})
    @RequestMapping(value = "/projects/{projectId}/suits/{suitId}", method = RequestMethod.DELETE)
    public ResponseEntity<SuitDTO> removeSuit(@PathVariable("projectId") long projectId,
                                           @PathVariable("suitId") long suitId) {
        SuitDTO removedSuitDTO = suitService.removeSuit(projectId, suitId);

        return new ResponseEntity<>(removedSuitDTO, HttpStatus.OK);
    }

    @Secured({"ROLE_ADMIN", "ROLE_TEST_ENGINEER", "ROLE_TEST_LEAD"})
    @RequestMapping(value = "/projects/{projectId}/suits/{suitId}/featureFile",
        method = RequestMethod.POST, consumes = "application/json")
    @ApiImplicitParam(name = "Authorization", value = "add here your token",
        paramType = "header", dataType = "string", required = true)
    public ResponseEntity<String> downloadFile(@RequestBody @Valid SuitDTO suitDTO)
        throws IOException {
        final List<Long> caseIds = suitDTO.getCases().stream()
            .map(CaseDTO::getId)
            .collect(Collectors.toList());

        return new ResponseEntity<>(ioService.generateFile(suitDTO.getId(), caseIds),
            HttpStatus.OK);
    }
}