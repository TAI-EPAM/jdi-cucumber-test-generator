package com.epam.test_generator.controllers;

import com.epam.test_generator.dto.CaseDTO;
import com.epam.test_generator.dto.SuitDTO;
import com.epam.test_generator.dto.ValidationErrorsDTO;
import com.epam.test_generator.services.CaseService;
import com.epam.test_generator.services.SuitService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
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
public class CaseController {

    @Autowired
    private CaseService caseService;

    @Autowired
    private SuitService suitService;

    @ApiOperation(value = "Get all cases from the suit", nickname = "getCases")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK", response = CaseDTO.class, responseContainer = "List"),
        @ApiResponse(code = 404, message = "Suit not found")
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "suitId", value = "ID of suit which contains cases", required = true, dataType = "long", paramType = "path")
    })
    @RequestMapping(value = "/suits/{suitId}/cases", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<CaseDTO>> getCases(@PathVariable("suitId") long suitId) {
        SuitDTO suitDTO = suitService.getSuit(suitId);

        return new ResponseEntity<>(suitDTO.getCases(), HttpStatus.OK);
    }

    @ApiOperation(value = "Get case by id", nickname = "getCase")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK", response = CaseDTO.class),
        @ApiResponse(code = 404, message = "Suit/Case not found")
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "suitId", value = "ID of suit which contains the case", required = true, dataType = "long", paramType = "path"),
        @ApiImplicitParam(name = "caseId", value = "ID of case to return", required = true, dataType = "long", paramType = "path")
    })
    @RequestMapping(value = "/suits/{suitId}/cases/{caseId}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<CaseDTO> getCase(@PathVariable("suitId") long suitId,
                                           @PathVariable("caseId") long caseId) {

        return new ResponseEntity<>(caseService.getCase(suitId, caseId), HttpStatus.OK);
    }

    @ApiOperation(value = "Add a new case to the suit", nickname = "addCaseToSuit")
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "Created", response = Long.class),
        @ApiResponse(code = 400, message = "Invalid input", response = ValidationErrorsDTO.class),
        @ApiResponse(code = 404, message = "Suit not found")
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "suitId", value = "ID of suit which will be added a new case", required = true, dataType = "long", paramType = "path"),
        @ApiImplicitParam(name = "caseDTO", value = "Added case object", required = true, dataType = "CaseDTO", paramType = "body")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "/suits/{suitId}/cases", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity<Long> addCaseToSuit(@PathVariable("suitId") long suitId,
                                              @RequestBody @Valid CaseDTO caseDTO) {

        return new ResponseEntity<>(caseService.addCaseToSuit(suitId, caseDTO), HttpStatus.CREATED);
    }

    @ApiOperation(value = "Update case by id", nickname = "updateCase")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 400, message = "Invalid input", response = ValidationErrorsDTO.class),
        @ApiResponse(code = 404, message = "Suit/Case not found")
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "suitId", value = "ID of suit which contains the case", required = true, dataType = "long", paramType = "path"),
        @ApiImplicitParam(name = "caseId", value = "ID of case to update", required = true, dataType = "long", paramType = "path"),
        @ApiImplicitParam(name = "caseDTO", value = "Updated case object", required = true, dataType = "CaseDTO", paramType = "body")
    })
    @RequestMapping(value = "/suits/{suitId}/cases/{caseId}", method = RequestMethod.PUT, consumes = "application/json")
    public ResponseEntity<Void> updateCase(@PathVariable("suitId") long suitId,
                                           @PathVariable("caseId") long caseId,
                                           @RequestBody @Valid CaseDTO caseDTO) {
        caseService.updateCase(suitId, caseId, caseDTO);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "Delete case by id", nickname = "removeCase")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 400, message = "Suit doesn't contain the case"),
        @ApiResponse(code = 404, message = "Suit/Case not found")
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "suitId", value = "ID of suit which contains the case", required = true, dataType = "long", paramType = "path"),
        @ApiImplicitParam(name = "caseId", value = "ID of case to delete", required = true, dataType = "long", paramType = "path")
    })
    @RequestMapping(value = "/suits/{suitId}/cases/{caseId}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> removeCase(@PathVariable("suitId") long suitId,
                                           @PathVariable("caseId") long caseId) {
        caseService.removeCase(suitId, caseId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "Delete cases from suit", nickname = "removeCases")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "Suit not found"),
        @ApiResponse(code = 400, message = "Suit doesn't contains some of cases")
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "suitId", value = "ID of suit which contains the case",
            required = true, dataType = "long", paramType = "path"),
        @ApiImplicitParam(name = "removeCaseIds", value = "IDs of cases to be removed",
            required = true, dataType = "Long[]", paramType = "body")
    })
    @RequestMapping(value = "/suits/{suitId}/cases", method = RequestMethod.DELETE, consumes = "application/json")
    public ResponseEntity<Void> removeCases(@PathVariable("suitId") long suitId,
                                            @RequestBody Long[] removeCaseIds) {
        SuitDTO suitDTO = suitService.getSuit(suitId);
        List<Long> existentSuitCaseIds = suitDTO.getCases().stream()
            .map(CaseDTO::getId)
            .collect(Collectors.toList());
        List<Long> idsToRemove = Arrays.asList(removeCaseIds);

        if (!existentSuitCaseIds.containsAll(idsToRemove)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        caseService.removeCases(suitId, idsToRemove);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}