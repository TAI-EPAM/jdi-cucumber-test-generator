package com.epam.test_generator.controllers.caze;

import com.epam.test_generator.controllers.caze.request.CaseCreateDTO;
import com.epam.test_generator.controllers.caze.request.CaseEditDTO;
import com.epam.test_generator.controllers.caze.request.CaseRowNumberUpdateDTO;
import com.epam.test_generator.controllers.caze.request.CaseUpdateDTO;
import com.epam.test_generator.controllers.caze.response.CaseDTO;
import com.epam.test_generator.controllers.suit.response.SuitDTO;
import com.epam.test_generator.dto.ValidationErrorsDTO;
import com.epam.test_generator.dto.wrapper.ListWrapper;
import com.epam.test_generator.entities.Event;
import com.epam.test_generator.entities.Status;
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
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ResponseStatus;
/**
 * Handle cases in specified suit.
 */
@RestController
@RequestMapping("/projects/{projectId}/suits/{suitId}/cases")
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
        @ApiImplicitParam(name = "projectId", value = "ID of project",
            required = true, dataType = "long", paramType = "path"),
        @ApiImplicitParam(name = "suitId", value = "ID of suit which contains cases",
            required = true, dataType = "long", paramType = "path"),
        @ApiImplicitParam(name = "Authorization", value = "add here your token", paramType = "header", dataType = "string", required = true)

    })
    @Secured({"ROLE_ADMIN", "ROLE_TEST_ENGINEER", "ROLE_TEST_LEAD", "ROLE_GUEST"})
    @GetMapping
    public ResponseEntity<List<CaseDTO>> getCases(@PathVariable("projectId") long projectId,
                                                  @PathVariable("suitId") long suitId) {
        SuitDTO suitDTO = suitService.getSuitDTO(projectId, suitId);

        return new ResponseEntity<>(suitDTO.getCases(), HttpStatus.OK);
    }

    @ApiOperation(value = "Get case by id", nickname = "getCase")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK", response = CaseDTO.class),
        @ApiResponse(code = 404, message = "Suit/Case not found")
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "projectId", value = "ID of project",
            required = true, dataType = "long", paramType = "path"),
        @ApiImplicitParam(name = "suitId", value = "ID of suit which contains the case",
            required = true, dataType = "long", paramType = "path"),
        @ApiImplicitParam(name = "caseId", value = "ID of case to return",
            required = true, dataType = "long", paramType = "path"),
        @ApiImplicitParam(name = "Authorization", value = "add here your token", paramType = "header", dataType = "string", required = true)
    })
    @Secured({"ROLE_ADMIN", "ROLE_TEST_ENGINEER", "ROLE_TEST_LEAD", "ROLE_GUEST"})
    @GetMapping("/{caseId}")
    public ResponseEntity<CaseDTO> getCase(@PathVariable("projectId") long projectId,
                                           @PathVariable("suitId") long suitId,
                                           @PathVariable("caseId") long caseId) {

        return new ResponseEntity<>(caseService.getCaseDTO(projectId, suitId, caseId),
            HttpStatus.OK);
    }

    @ApiOperation(value = "Add a new case to the suit", nickname = "addCaseToSuit")
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "Created", response = Long.class),
        @ApiResponse(code = 400, message = "Invalid input", response = ValidationErrorsDTO.class),
        @ApiResponse(code = 404, message = "Suit not found")
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "projectId", value = "ID of project",
            required = true, dataType = "long", paramType = "path"),
        @ApiImplicitParam(name = "suitId", value = "ID of suit which will be added a new case",
            required = true, dataType = "long", paramType = "path"),
            @ApiImplicitParam(name = "caseCreateDTO", value = "Added case object",
                    required = true, dataType = "CaseCreateDTO", paramType = "body"),
            @ApiImplicitParam(name = "Authorization", value = "add here your token", paramType = "header", dataType = "string", required = true)
    })
    @ResponseStatus(HttpStatus.CREATED)
    @Secured({"ROLE_ADMIN", "ROLE_TEST_ENGINEER", "ROLE_TEST_LEAD"})
    @PostMapping
    public ResponseEntity<CaseDTO> addCaseToSuit(@PathVariable("projectId") long projectId,
                                                 @PathVariable("suitId") long suitId,
                                                 @RequestBody @Valid CaseCreateDTO caseCreateDTO) {

        return new ResponseEntity<>(caseService.addCaseToSuit(projectId, suitId, caseCreateDTO),
            HttpStatus.CREATED);
    }

    @ApiOperation(value = "Update case by id", nickname = "updateCase")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 400, message = "Invalid input", response = ValidationErrorsDTO.class),
        @ApiResponse(code = 404, message = "Suit/Case not found")
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "projectId", value = "ID of project",
            required = true, dataType = "long", paramType = "path"),
        @ApiImplicitParam(name = "suitId", value = "ID of suit which contains the case",
            required = true, dataType = "long", paramType = "path"),
        @ApiImplicitParam(name = "caseId", value = "ID of case to update",
            required = true, dataType = "long", paramType = "path"),
        @ApiImplicitParam(name = "caseUpdateDTO", value = "Updated case object",
            required = true, dataType = "CaseUpdateDTO", paramType = "body"),
        @ApiImplicitParam(name = "Authorization", value = "add here your token",
            paramType = "header", dataType = "string", required = true)
    })
    @Secured({"ROLE_ADMIN", "ROLE_TEST_ENGINEER", "ROLE_TEST_LEAD"})
    @PutMapping("/{caseId}")
    public ResponseEntity<CaseDTO> updateCase(@PathVariable("projectId") long projectId,
                                                             @PathVariable("suitId") long suitId,
                                                             @PathVariable("caseId") long caseId,
                                                             @RequestBody @Valid CaseUpdateDTO caseUpdateDTO) {
        CaseDTO caseDto = caseService
            .updateCase(projectId, suitId, caseId, caseUpdateDTO);

        return new ResponseEntity<>(caseDto, HttpStatus.OK);
    }

    @Deprecated
    @ApiOperation(value = "Update, create or delete list of cases", nickname = "updateCases")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK", response = Long.class, responseContainer = "List"),
        @ApiResponse(code = 400, message = "Bad Request"),
        @ApiResponse(code = 404, message = "Some of Suits/cases not found")
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "projectId", value = "ID of project",
            required = true, dataType = "long", paramType = "path"),
        @ApiImplicitParam(name = "suitId", value = "ID of suit to operate cases",
            required = true, dataType = "long", paramType = "path"),
        @ApiImplicitParam(name = "Authorization", value = "add here your token",
            paramType = "header", dataType = "string", required = true)
    })
    @PutMapping
    public ResponseEntity<List<CaseDTO>> updateCases(@PathVariable("projectId") long projectId,
                                                  @PathVariable("suitId") long suitId,
                                                  @RequestBody @Valid ListWrapper<CaseEditDTO> editDTOList)
        throws MethodArgumentNotValidException {

        List<CaseDTO> updatedCasesDTO = caseService.updateCases(projectId, suitId,
            editDTOList.getList());

        return new ResponseEntity<>(updatedCasesDTO, HttpStatus.OK);
    }

    @Deprecated
    @ApiOperation(value = "Delete case by id", nickname = "removeCase")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 400, message = "Suit doesn't contain the case"),
        @ApiResponse(code = 404, message = "Suit/Case not found")
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "projectId", value = "ID of project",
            required = true, dataType = "long", paramType = "path"),
        @ApiImplicitParam(name = "suitId", value = "ID of suit which contains the case",
            required = true, dataType = "long", paramType = "path"),
        @ApiImplicitParam(name = "caseId", value = "ID of case to delete",
            required = true, dataType = "long", paramType = "path"),
        @ApiImplicitParam(name = "Authorization", value = "add here your token", paramType = "header", dataType = "string", required = true)
    })
    @Secured({"ROLE_ADMIN", "ROLE_TEST_ENGINEER", "ROLE_TEST_LEAD"})
    @DeleteMapping("/{caseId}")
    public ResponseEntity<CaseDTO> removeCase(@PathVariable("projectId") long projectId,
                                           @PathVariable("suitId") long suitId,
                                           @PathVariable("caseId") long caseId) {
        CaseDTO removedCaseDTO = caseService.removeCase(projectId, suitId, caseId);

        return new ResponseEntity<>(removedCaseDTO, HttpStatus.OK);
    }

    @ApiOperation(value = "Delete cases from suit", nickname = "removeCases")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "Suit not found"),
        @ApiResponse(code = 400, message = "Suit doesn't contains some of cases")
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "projectId", value = "ID of project",
            required = true, dataType = "long", paramType = "path"),
        @ApiImplicitParam(name = "suitId", value = "ID of suit which contains the case",
            required = true, dataType = "long", paramType = "path"),
        @ApiImplicitParam(name = "removeCaseIds", value = "IDs of cases to be removed",
            required = true, dataType = "Long[]", paramType = "body"),
        @ApiImplicitParam(name = "Authorization", value = "add here your token", paramType = "header", dataType = "string", required = true)
    })
    @Secured({"ROLE_ADMIN", "ROLE_TEST_ENGINEER", "ROLE_TEST_LEAD"})
    @DeleteMapping
    public ResponseEntity<List<CaseDTO>> removeCases(@PathVariable("projectId") long projectId,
                                            @PathVariable("suitId") long suitId,
                                            @RequestBody Long[] removeCaseIds) {
        SuitDTO suitDTO = suitService.getSuitDTO(projectId, suitId);
        List<Long> existentSuitCaseIds = suitDTO.getCases().stream()
            .map(CaseDTO::getId)
            .collect(Collectors.toList());
        List<Long> idsToRemove = Arrays.asList(removeCaseIds);

        if (!existentSuitCaseIds.containsAll(idsToRemove)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        List<CaseDTO> removedCasesDTO = caseService.removeCases(projectId, suitId, idsToRemove);

        return new ResponseEntity<>(removedCasesDTO, HttpStatus.OK);
    }

    @ApiOperation(value = "Perform status changing event on given case", nickname = "performEvent")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK", response = Status.class),
        @ApiResponse(code = 400, message = "Can't perform given event on current case")
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "projectId", value = "ID of project",
            required = true, dataType = "long", paramType = "path"),
        @ApiImplicitParam(name = "suitId", value = "ID of suit which contains the case",
            required = true, dataType = "long", paramType = "path"),
        @ApiImplicitParam(name = "caseId", value = "IDs of cases to perform event on",
            required = true, dataType = "long", paramType = "path"),
        @ApiImplicitParam(name = "event", value = "Name of event to be performed",
            required = true, dataType = "String", paramType = "path"),
        @ApiImplicitParam(name = "Authorization", value = "add here your token", paramType = "header", dataType = "string", required = true)
    })
    @Secured({"ROLE_ADMIN", "ROLE_TEST_ENGINEER", "ROLE_TEST_LEAD"})
    @PutMapping("/{caseId}/events/{event}")
    public ResponseEntity<Status> performEvent(@PathVariable("projectId") long projectId,
                                               @PathVariable("suitId") long suitId,
                                               @PathVariable("caseId") long caseId,
                                               @PathVariable("event") Event event)
        throws Exception {

        return new ResponseEntity<>(
            caseService.performEvent(projectId, suitId, caseId, event), HttpStatus.OK);
    }

    @ApiOperation(value = "Update cases by rowNumber", nickname = "updateCaseRowNumber")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 400, message = "Invalid input", response = ValidationErrorsDTO.class),
        @ApiResponse(code = 404, message = "Case not found")
    })
    @ApiImplicitParam(name = "Authorization", value = "add here your token", paramType = "header", dataType = "string", required = true)
    @Secured({"ROLE_ADMIN", "ROLE_TEST_ENGINEER", "ROLE_TEST_LEAD"})
    @PutMapping("/row-numbers")
    public ResponseEntity<List<CaseRowNumberUpdateDTO>> updatecaseRowNumber
        (@PathVariable("projectId") long projectId,
         @PathVariable("suitId") long suitId,
         @RequestBody @Valid ListWrapper<CaseRowNumberUpdateDTO> rowNumberUpdates) {

        List<CaseRowNumberUpdateDTO> updatedCaseRowNumberUpdateDTOs = caseService
            .updateCaseRowNumber(projectId, suitId, rowNumberUpdates.getList());
        return new ResponseEntity<>(updatedCaseRowNumberUpdateDTOs, HttpStatus.OK);
    }
}