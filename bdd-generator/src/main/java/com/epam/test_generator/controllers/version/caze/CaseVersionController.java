package com.epam.test_generator.controllers.version.caze;

import com.epam.test_generator.controllers.version.caze.response.CaseVersionDTO;
import com.epam.test_generator.controllers.caze.response.CaseDTO;
import com.epam.test_generator.services.CaseService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * This controller handle case versions.
 */
@RestController
@RequestMapping("/projects/{projectId}/suits/{suitId}/cases/{caseId}/versions")
public class CaseVersionController {

    @Autowired
    private CaseService caseService;

    @ApiOperation(value = "Get case versions by id", nickname = "getCaseVersions")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK", response = CaseVersionDTO.class, responseContainer = "List"),
        @ApiResponse(code = 400, message = "Suit doesn't contain the case"),
        @ApiResponse(code = 404, message = "Suit/Case not found")
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "projectId", value = "ID of project",
            required = true, dataType = "long", paramType = "path"),
        @ApiImplicitParam(name = "suitId", value = "ID of suit which contains the case",
            required = true, dataType = "long", paramType = "path"),
        @ApiImplicitParam(name = "caseId", value = "ID of case",
            required = true, dataType = "long", paramType = "path"),
        @ApiImplicitParam(name = "Authorization", value = "add here your token",
            required = true, dataType = "string", paramType = "header")
    })
    @GetMapping
    public ResponseEntity<List<CaseVersionDTO>> getCaseVersions(
        @PathVariable("projectId") long projectId,
        @PathVariable("suitId") long suitId,
        @PathVariable("caseId") long caseId
    ) {
        return new ResponseEntity<>(caseService.getCaseVersions(projectId, suitId, caseId),
            HttpStatus.OK);
    }

    @ApiOperation(value = "Restore case by commit id", nickname = "restoreCase")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 400, message = "Suit doesn't contain the case"),
        @ApiResponse(code = 404, message = "Suit/Case/CaseToRestore not found")
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "projectId", value = "ID of project",
            required = true, dataType = "long", paramType = "path"),
        @ApiImplicitParam(name = "suitId", value = "ID of suit which contains the case",
            required = true, dataType = "long", paramType = "path"),
        @ApiImplicitParam(name = "caseId", value = "ID of case",
            required = true, dataType = "long", paramType = "path"),
        @ApiImplicitParam(name = "commitId", value = "ID of commit to which state you want restore",
            required = true, dataType = "String", paramType = "path"),
        @ApiImplicitParam(name = "Authorization", value = "add here your token",
            required = true, dataType = "string", paramType = "header")
    })
    @PutMapping("/{commitId:.+}")
    public ResponseEntity<CaseDTO> restoreCase(@PathVariable("projectId") long projectId,
                                               @PathVariable("suitId") long suitId,
                                               @PathVariable("caseId") long caseId,
                                               @PathVariable("commitId") String commitId
    ) {
        CaseDTO restoredCaseDTO = caseService.restoreCase(projectId, suitId, caseId, commitId);
        return new ResponseEntity<>(restoredCaseDTO, HttpStatus.OK);
    }
}
