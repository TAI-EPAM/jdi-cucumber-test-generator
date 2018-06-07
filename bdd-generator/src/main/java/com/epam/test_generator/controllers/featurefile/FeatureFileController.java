package com.epam.test_generator.controllers.featurefile;

import com.epam.test_generator.controllers.featurefile.request.FeatureFileDTO;
import com.epam.test_generator.dto.ValidationErrorsDTO;
import com.epam.test_generator.dto.wrapper.ListWrapper;
import com.epam.test_generator.services.IOService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Allows select suits with cases and download set of feature files.
 */
@RestController
@RequestMapping("/projects/{projectId}")
public class FeatureFileController {

    @Autowired
    private IOService ioService;


    @ApiOperation(value = "Generate and download cucumber feature file", nickname = "downloadFile")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 400, message = "Invalid input", response = ValidationErrorsDTO.class),
        @ApiResponse(code = 404, message = "Project, suit or case not found")
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "projectId", value = "ID of project",
            required = true, dataType = "long", paramType = "path"),
        @ApiImplicitParam(name = "Authorization", value = "add here your token",
            paramType = "header", dataType = "string", required = true)
    })
    @Secured({"ROLE_ADMIN", "ROLE_TEST_ENGINEER", "ROLE_TEST_LEAD"})
    @PostMapping(value = "/feature-file")
    public ResponseEntity downloadFile(@PathVariable("projectId") long projectId,
                                       @RequestBody @Valid ListWrapper<FeatureFileDTO> suitAndCaseIds,
                                       HttpServletResponse response) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        byte[] bytes = ioService.generateZipFile(projectId, suitAndCaseIds.getList());
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        response.addHeader("Content-Disposition", "attachment; filename=test.zip");
        return new ResponseEntity<>(bytes, HttpStatus.OK);
    }
}