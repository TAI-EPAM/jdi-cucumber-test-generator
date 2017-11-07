package com.epam.test_generator.controllers;

import com.epam.test_generator.dto.CaseDTO;
import com.epam.test_generator.dto.SuitDTO;
import com.epam.test_generator.dto.ValidationErrorsDTO;
import com.epam.test_generator.services.SuitService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class SuitController {

    @Autowired
    private SuitService suitService;


    @ApiOperation(value = "Get all suits", nickname = "getSuits")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK", response = SuitDTO.class, responseContainer = "List")
    })
    @RequestMapping(value = "/suits", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<SuitDTO>> getSuits() {

        return new ResponseEntity<>(suitService.getSuits(), HttpStatus.OK);
    }

    @ApiOperation(value = "Get suit by id", nickname = "getSuit")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK", response = SuitDTO.class),
        @ApiResponse(code = 404, message = "Suit not found")
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "suitId", value = "ID of suit to return", required = true, dataType = "long", paramType = "path")
    })
    @RequestMapping(value = "/suits/{suitId}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<SuitDTO> getSuit(@PathVariable("suitId") long suitId) {

        return new ResponseEntity<>(suitService.getSuit(suitId), HttpStatus.OK);
    }

    @ApiOperation(value = "Add a new suit", nickname = "addSuit")
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "Created", response = Long.class),
        @ApiResponse(code = 400, message = "Invalid input", response = ValidationErrorsDTO.class)
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "suitDTO", value = "Added suit object", required = true, dataType = "SuitDTO", paramType = "body")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "/suits", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity<Long> addSuit(@RequestBody @Valid SuitDTO suitDTO) {

        return new ResponseEntity<>(suitService.addSuit(suitDTO), HttpStatus.CREATED);
    }

    @ApiOperation(value = "Update suit by id", nickname = "updateSuit")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 400, message = "Invalid input", response = ValidationErrorsDTO.class),
        @ApiResponse(code = 404, message = "Suit not found")
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "suitId", value = "ID of suit to update", required = true, dataType = "long", paramType = "path"),
        @ApiImplicitParam(name = "suitDTO", value = "Updated suit object", required = true, dataType = "SuitDTO", paramType = "body")
    })
    @RequestMapping(value = "/suits/{suitId}", method = RequestMethod.PUT, consumes = "application/json")
    public ResponseEntity<Void> updateSuit(@PathVariable("suitId") long suitId,
                                           @RequestBody @Valid SuitDTO suitDTO) {
        suitService.updateSuit(suitId, suitDTO);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "Delete suit by id", nickname = "removeSuit")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "Suit not found")
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "suitId", value = "ID of suit to delete", required = true, dataType = "long", paramType = "path")
    })
    @RequestMapping(value = "/suits/{suitId}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> removeSuit(@PathVariable("suitId") long suitId) {
        suitService.removeSuit(suitId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/suits/{suitId}/featureFile", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<String> downloadFile(@RequestBody @Valid SuitDTO suitDTO)
        throws IOException {
        List<Long> caseIds = suitDTO.getCases().stream().map(CaseDTO::getId)
            .collect(Collectors.toList());

        return new ResponseEntity<>(suitService.generateFile(suitDTO.getId(), caseIds),
            HttpStatus.OK);
    }
}