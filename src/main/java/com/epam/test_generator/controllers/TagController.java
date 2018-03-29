package com.epam.test_generator.controllers;


import com.epam.test_generator.dto.CaseDTO;
import com.epam.test_generator.dto.TagDTO;
import com.epam.test_generator.dto.ValidationErrorsDTO;
import com.epam.test_generator.services.CaseService;
import com.epam.test_generator.services.TagService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * Handle tags for cases and suits.
 */
@RestController
public class TagController {

    @Autowired
    private TagService tagService;

    @Autowired
    private CaseService casesService;

    @ApiOperation(value = "Get all tags from project", nickname = "getAllProjectTags")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = TagDTO.class, responseContainer = "Set"),
            @ApiResponse(code = 404, message = "Project not found")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "projectId", value = "ID of project",
                    required = true, dataType = "long", paramType = "path"),
            @ApiImplicitParam(name = "Authorization",
                              value = "add here your token",
                              paramType = "header",
                              dataType = "string",
                              required = true)
    })
    @Secured({"ROLE_ADMIN", "ROLE_TEST_ENGINEER", "ROLE_TEST_LEAD", "ROLE_GUEST"})
    @RequestMapping(value = "/projects/{projectId}/tags",
            method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Set<TagDTO>> getAllProjectTags(@PathVariable("projectId")
                                                                     long projectId) {

        return new ResponseEntity<>(tagService.getAllProjectTags(projectId), HttpStatus.OK);
    }

    @ApiOperation(value = "Get all tags from all cases in suit", nickname = "getAllTagsFromAllCasesInSuit")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = TagDTO.class, responseContainer = "Set"),
            @ApiResponse(code = 404, message = "Suit not found")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "projectId", value = "ID of project",
                    required = true, dataType = "long", paramType = "path"),
            @ApiImplicitParam(name = "suitId", value = "ID of suit which contains cases with tags",
                    required = true, dataType = "long", paramType = "path"),
            @ApiImplicitParam(name = "Authorization",
                              value = "add here your token",
                              paramType = "header",
                              dataType = "string",
                              required = true)
    })
    @Secured({"ROLE_ADMIN", "ROLE_TEST_ENGINEER", "ROLE_TEST_LEAD", "ROLE_GUEST"})
    @RequestMapping(value = "/projects/{projectId}/suits/{suitId}/cases/tags",
            method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Set<TagDTO>> getAllTagsFromAllCasesInSuit(@PathVariable("projectId") long projectId,
                                                                    @PathVariable("suitId") long suitId) {

        return new ResponseEntity<>(tagService.getAllTagsFromAllCasesInSuit(projectId, suitId), HttpStatus.OK);
    }

    @ApiOperation(value = "Get all tags from the case", nickname = "getTags")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = TagDTO.class, responseContainer = "Set"),
            @ApiResponse(code = 404, message = "Suit/Case not found")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "projectId", value = "ID of project",
                    required = true, dataType = "long", paramType = "path"),
            @ApiImplicitParam(name = "suitId", value = "ID of suit which contains the case",
                    required = true, dataType = "long", paramType = "path"),
            @ApiImplicitParam(name = "caseId", value = "ID of case which contains tags",
                    required = true, dataType = "long", paramType = "path"),
            @ApiImplicitParam(name = "Authorization",
                              value = "add here your token",
                              paramType = "header",
                              dataType = "string",
                              required = true)
    })
    @Secured({"ROLE_ADMIN", "ROLE_TEST_ENGINEER", "ROLE_TEST_LEAD", "ROLE_GUEST"})
    @RequestMapping(value = "/projects/{projectId}/suits/{suitId}/cases/{caseId}/tags",
            method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Set<TagDTO>> getTags(@PathVariable("projectId") long projectId,
                                               @PathVariable("suitId") long suitId,
                                               @PathVariable("caseId") long caseId) {
        CaseDTO caseDTO = casesService.getCaseDTO(projectId, suitId, caseId);

        return new ResponseEntity<>(caseDTO.getTags(), HttpStatus.OK);
    }

    @ApiOperation(value = "Add a new tag to the case", nickname = "addTagToCase")
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
            @ApiImplicitParam(name = "caseId", value = "ID of case which will be added a new tag",
                    required = true, dataType = "long", paramType = "path"),
            @ApiImplicitParam(name = "tagDTO", value = "Added tag object",
                    required = true, dataType = "TagDTO", paramType = "body"),
            @ApiImplicitParam(name = "Authorization",
                              value = "add here your token",
                              paramType = "header",
                              dataType = "string", required = true)
    })
    @Secured({"ROLE_ADMIN", "ROLE_TEST_ENGINEER", "ROLE_TEST_LEAD"})
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "/projects/{projectId}/suits/{suitId}/cases/{caseId}/tags",
                    method = RequestMethod.POST,
                    produces = "application/json")
    public ResponseEntity<Long> addTagToCase(@PathVariable("projectId") long projectId,
                                             @PathVariable("suitId") long suitId,
                                             @PathVariable("caseId") long caseId,
                                             @RequestBody TagDTO tagDTO) {

        return new ResponseEntity<>(tagService.addTagToCase(projectId, suitId, caseId, tagDTO),
                HttpStatus.CREATED);
    }

    @ApiOperation(value = "Update tag by id", nickname = "updateTag")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Invalid input", response = ValidationErrorsDTO.class),
            @ApiResponse(code = 404, message = "Suit/Case/Tag not found")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "projectId", value = "ID of project",
                    required = true, dataType = "long", paramType = "path"),
            @ApiImplicitParam(name = "suitId", value = "ID of suit which contains the case",
                    required = true, dataType = "long", paramType = "path"),
            @ApiImplicitParam(name = "caseId", value = "ID of case which contains the tag",
                    required = true, dataType = "long", paramType = "path"),
            @ApiImplicitParam(name = "tagId", value = "ID of tag to update",
                    required = true, dataType = "long", paramType = "path"),
            @ApiImplicitParam(name = "tagDTO", value = "Updated tag object",
                    required = true, dataType = "TagDTO", paramType = "body"),
            @ApiImplicitParam(name = "Authorization",
                              value = "add here your token",
                              paramType = "header",
                              dataType = "string", required = true)
    })
    @Secured({"ROLE_ADMIN", "ROLE_TEST_ENGINEER", "ROLE_TEST_LEAD"})
    @RequestMapping(value = "/projects/{projectId}/suits/{suitId}/cases/{caseId}/tags/{tagId}",
                    method = RequestMethod.PUT,
                    consumes = "application/json")
    public ResponseEntity<Void> updateTag(@PathVariable("projectId") long projectId,
                                          @PathVariable("suitId") long suitId,
                                          @PathVariable("caseId") long caseId,
                                          @PathVariable("tagId") long tagId,
                                          @RequestBody TagDTO tagDTO) {
        tagService.updateTag(projectId, suitId, caseId, tagId, tagDTO);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "Delete tag by id", nickname = "removeTag")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Suit/Case/Tag not found")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "projectId", value = "ID of project",
                    required = true, dataType = "long", paramType = "path"),
            @ApiImplicitParam(name = "suitId", value = "ID of suit which contains the case",
                    required = true, dataType = "long", paramType = "path"),
            @ApiImplicitParam(name = "caseId", value = "ID of case which contains the tag",
                    required = true, dataType = "long", paramType = "path"),
            @ApiImplicitParam(name = "tagId", value = "ID of tag to delete",
                    required = true, dataType = "long", paramType = "path"),
            @ApiImplicitParam(name = "Authorization",
                              value = "add here your token",
                              paramType = "header",
                              dataType = "string",
                              required = true)
    })
    @Secured({"ROLE_ADMIN", "ROLE_TEST_ENGINEER", "ROLE_TEST_LEAD"})
    @RequestMapping(value = "/projects/{projectId}/suits/{suitId}/cases/{caseId}/tags/{tagId}",
            method = RequestMethod.DELETE, produces = "application/json")
    public ResponseEntity<Void> removeTagFromCase(@PathVariable("projectId") long projectId,
                                                  @PathVariable("suitId") long suitId,
                                                  @PathVariable("caseId") long caseId,
                                                  @PathVariable("tagId") long tagId) {
        tagService.removeTag(projectId, suitId, caseId, tagId);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}