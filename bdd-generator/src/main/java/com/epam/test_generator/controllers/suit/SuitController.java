package com.epam.test_generator.controllers.suit;

import com.epam.test_generator.controllers.caze.response.CaseDTO;
import com.epam.test_generator.controllers.suit.request.SuitCreateDTO;
import com.epam.test_generator.controllers.suit.response.SuitDTO;
import com.epam.test_generator.controllers.suit.request.SuitRowNumberUpdateDTO;
import com.epam.test_generator.controllers.suit.request.SuitUpdateDTO;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Allow work with suits of specified project.
 */
@RestController
@RequestMapping("/projects/{projectId}/suits")
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
    @ApiImplicitParams({
        @ApiImplicitParam(name = "projectId", value = "ID of project with suits",
            required = true, dataType = "long", paramType = "path"),
        @ApiImplicitParam(name = "Authorization", value = "add here your token", paramType = "header", dataType = "string", required = true)
    })
    @Secured({"ROLE_ADMIN", "ROLE_TEST_ENGINEER", "ROLE_TEST_LEAD", "ROLE_GUEST"})
    @GetMapping
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
        @ApiImplicitParam(name = "Authorization", value = "add here your token", paramType = "header", dataType = "string", required = true)
    })
    @Secured({"ROLE_ADMIN", "ROLE_TEST_ENGINEER", "ROLE_TEST_LEAD"})
    @GetMapping("/{suitId}")
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
        @ApiImplicitParam(name = "suitCreateDTO", value = "Added suit object",
            required = true, dataType = "SuitCreateDTO", paramType = "body"),
        @ApiImplicitParam(name = "Authorization", value = "add here your token", paramType = "header", dataType = "string", required = true)
    })
    @Secured({"ROLE_ADMIN", "ROLE_TEST_ENGINEER", "ROLE_TEST_LEAD"})
    @PostMapping
    public ResponseEntity<SuitDTO> createSuit(@PathVariable("projectId") long projectId,
                                              @RequestBody @Valid SuitCreateDTO suitCreateDTO) {
        return new ResponseEntity<>(suitService.addSuit(projectId, suitCreateDTO),
            HttpStatus.CREATED);
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
        @ApiImplicitParam(name = "suitUpdateDTO", value = "Updated suit object",
            required = true, dataType = "SuitUpdateDTO", paramType = "body"),
        @ApiImplicitParam(name = "Authorization", value = "add here your token", paramType = "header", dataType = "string", required = true)
    })
    @Secured({"ROLE_ADMIN", "ROLE_TEST_ENGINEER", "ROLE_TEST_LEAD"})
    @PutMapping("/{suitId}")
    public ResponseEntity<SuitDTO> updateSuit(@PathVariable("projectId") long projectId,
                                              @PathVariable("suitId") long suitId,
                                              @RequestBody @Valid SuitUpdateDTO suitUpdateDTO) {
        SuitDTO updatedSuitDTO = suitService.updateSuit(projectId, suitId, suitUpdateDTO);

        return new ResponseEntity<>(updatedSuitDTO, HttpStatus.OK);
    }


    @ApiOperation(value = "Update suits by rowNumber", nickname = "updateSuitRowNumber")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 400, message = "Invalid input", response = ValidationErrorsDTO.class),
        @ApiResponse(code = 404, message = "Suit not found")
    })
    @ApiImplicitParam(name = "Authorization", value = "add here your token", paramType = "header", dataType = "string", required = true)
    @Secured({"ROLE_ADMIN", "ROLE_TEST_ENGINEER", "ROLE_TEST_LEAD"})
    @PutMapping("/row-numbers")
    public ResponseEntity<List<SuitRowNumberUpdateDTO>> updateSuitRowNumber
        (@RequestBody @Valid List<SuitRowNumberUpdateDTO> rowNumberUpdates) {

        List<SuitRowNumberUpdateDTO> updatedSuitRowNumberUpdateDTOs = suitService
            .updateSuitRowNumber(rowNumberUpdates);
        return new ResponseEntity<>(updatedSuitRowNumberUpdateDTOs, HttpStatus.OK);
    }

    @ApiOperation(value = "Generate and download cucumber feature file", nickname = "downloadFile")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "Project, suit or case not found")
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "projectId", value = "ID of project",
            required = true, dataType = "long", paramType = "path"),
        @ApiImplicitParam(name = "suitId", value = "ID of suit",
            required = true, dataType = "long", paramType = "path"),
        @ApiImplicitParam(name = "caseIds", value = "IDs of cases",
            required = true, dataType = "Array[long]", paramType = "body"),
        @ApiImplicitParam(name = "Authorization", value = "add here your token", paramType = "header", dataType = "string", required = true)
    })
    @Secured({"ROLE_ADMIN", "ROLE_TEST_ENGINEER", "ROLE_TEST_LEAD"})
    @PostMapping("/{suitId}/feature-file")
    public ResponseEntity<String> downloadFile(@PathVariable("projectId") long projectId,
                                               @PathVariable("suitId") long suitId,
                                               @RequestBody List<Long> caseIds)
        throws IOException {
        return new ResponseEntity<>(ioService.generateFile(suitId, caseIds), HttpStatus.OK);
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
        @ApiImplicitParam(name = "Authorization", value = "add here your token", paramType = "header", dataType = "string", required = true)
    })
    @Secured({"ROLE_ADMIN", "ROLE_TEST_ENGINEER", "ROLE_TEST_LEAD"})
    @DeleteMapping("/{suitId}")
    public ResponseEntity<SuitDTO> removeSuit(@PathVariable("projectId") long projectId,
                                              @PathVariable("suitId") long suitId) {
        SuitDTO removedSuitDTO = suitService.removeSuit(projectId, suitId);

        return new ResponseEntity<>(removedSuitDTO, HttpStatus.OK);
    }
}