package com.epam.test_generator.controllers;

import com.epam.test_generator.dto.RawSuitResultDTO;
import com.epam.test_generator.dto.TestResultDTO;
import com.epam.test_generator.services.TestResultService;
import com.epam.test_generator.services.exceptions.BadRequestException;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.Date;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestResultController {

    @Autowired
    private TestResultService testResultService;

    @ApiOperation(value = "Run selected tests", nickname = "runTests")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK")
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "projectId", value = "ID of project",
            required = true, dataType = "long", paramType = "path"),
        @ApiImplicitParam(name = "Authorization", value = "add here your token",
            paramType = "header", dataType = "string", required = true)
    })
    @Secured({"ROLE_ADMIN", "ROLE_TEST_ENGINEER", "ROLE_TEST_LEAD"})
    @RequestMapping(value = "/projects/{projectId}/tests/run",
        method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<Void> runTests(@PathVariable("projectId") long projectId,
                                         @RequestBody @Valid List<RawSuitResultDTO> rawSuitResultDTOS,
                                         Authentication authentication) {

        testResultService.saveResult(projectId, rawSuitResultDTOS, authentication);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "Get test run results from given range",
        nickname = "getTestRunResultsFromTo")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 400, message = "Incorrect given range")
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "projectId", value = "ID of project",
            required = true, dataType = "long", paramType = "path"),
        @ApiImplicitParam(name = "Authorization", value = "add here your token",
            paramType = "header", dataType = "string", required = true),
        @ApiImplicitParam(name = "offset", value = "start of range",
            paramType = "query", dataType = "int"),
        @ApiImplicitParam(name = "limit", value = "end of range",
            paramType = "query", dataType = "int")
    })
    @Secured({"ROLE_ADMIN", "ROLE_TEST_ENGINEER", "ROLE_TEST_LEAD"})
    @RequestMapping(value = "/projects/{projectId}/tests/results/",
        method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<TestResultDTO>> getTestRunResultsFromTo(
        @PathVariable("projectId") long projectId,
        @RequestParam(value = "offset", required = false, defaultValue = "0") Integer offset,
        @RequestParam(value = "limit", required = false) Integer limit) {

        if (limit == null) {
            if (offset == 0) {
                return new ResponseEntity<>(testResultService.getTestResults(projectId),
                    HttpStatus.OK);
            } else {
                throw new BadRequestException("Incorrect given range");
            }
        }

        if (offset < 0 || limit < 0) {
            throw new BadRequestException("Incorrect given range");
        }

        return new ResponseEntity<>(testResultService.getTestResults(projectId, offset, limit),
            HttpStatus.OK);
    }

    @ApiOperation(value = "Get test run results from given range by date",
        nickname = "getTestRunResultsFromDatesRange")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 400, message = "Incorrect date range")
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "projectId", value = "ID of project",
            required = true, dataType = "long", paramType = "path"),
        @ApiImplicitParam(name = "Authorization", value = "add here your token",
            paramType = "header", dataType = "string", required = true),
        @ApiImplicitParam(name = "from", value = "start of range",
            paramType = "query", dataType = "date", required = true),
        @ApiImplicitParam(name = "to", value = "end of range",
            paramType = "query", dataType = "date", required = true)
    })
    @Secured({"ROLE_ADMIN", "ROLE_TEST_ENGINEER", "ROLE_TEST_LEAD"})
    @RequestMapping(value = "/projects/{projectId}/tests/results/dates",
        method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<TestResultDTO>> getTestRunResultsFromToByDate(
        @PathVariable("projectId") long projectId,
        @RequestParam(value = "from") @DateTimeFormat(iso = ISO.DATE) Date from,
        @RequestParam(value = "to") @DateTimeFormat(iso = ISO.DATE) Date to) {

        if (from.after(to)) {
            throw new BadRequestException("Incorrect date range");
        }
        return new ResponseEntity<>(testResultService.getTestResults(projectId, from, to),
            HttpStatus.OK);
    }

}