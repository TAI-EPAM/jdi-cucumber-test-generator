package com.epam.test_generator.controllers;

import com.epam.test_generator.dto.SuitDTO;
import com.epam.test_generator.dto.SuitVersionDTO;
import com.epam.test_generator.services.SuitService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SuitVersionController {

    @Autowired
    private SuitService suitService;

    @ApiOperation(value = "Get suit versions by id", nickname = "getSuitVersions")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK",
            response = SuitVersionDTO.class, responseContainer = "List"),
        @ApiResponse(code = 400, message = "Project doesn't contain the suit"),
        @ApiResponse(code = 404, message = "Suit not found")
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "projectId", value = "ID of project",
            required = true, dataType = "long", paramType = "path"),
        @ApiImplicitParam(name = "suitId", value = "ID of suit which contains the case",
            required = true, dataType = "long", paramType = "path"),
        @ApiImplicitParam(name = "Authorization", value = "add here your token",
            paramType = "header", dataType = "string", required = true)
    })
    @RequestMapping(value = "/projects/{projectId}/suits/{suitId}/versions",
        method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<SuitVersionDTO>> getCaseVersions(
        @PathVariable("projectId") long projectId,
        @PathVariable("suitId") long suitId) {

        return new ResponseEntity<>(suitService.getSuitVersions(projectId, suitId), HttpStatus.OK);
    }

    @ApiOperation(value = "Restore suit by commit id", nickname = "restoreSuit")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 400, message = "Project doesn't contain the suit"),
        @ApiResponse(code = 404, message = "Suit not found")
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "projectId", value = "ID of project",
            required = true, dataType = "long", paramType = "path"),
        @ApiImplicitParam(name = "suitId", value = "ID of suit which contains the case",
            required = true, dataType = "long", paramType = "path"),
        @ApiImplicitParam(name = "commitId", value = "ID of commit to which state you want restore",
            required = true, dataType = "String", paramType = "path"),
        @ApiImplicitParam(name = "Authorization", value = "add here your token", paramType = "header",
            dataType = "string", required = true)
    })
    @RequestMapping(value = "/projects/{projectId}/suits/{suitId}/versions/{commitId:.+}",
        method = RequestMethod.PUT)
    public ResponseEntity<SuitDTO> restoreCase(@PathVariable("projectId") long projectId,
                                               @PathVariable("suitId") long suitId,
                                               @PathVariable("commitId") String commitId) {
        SuitDTO restoredCaseDTO = suitService.restoreSuit(projectId, suitId, commitId);
        return new ResponseEntity<>(restoredCaseDTO, HttpStatus.OK);
    }
}

