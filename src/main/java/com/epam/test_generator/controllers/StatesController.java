package com.epam.test_generator.controllers;

import com.epam.test_generator.entities.Event;
import com.epam.test_generator.entities.Status;
import com.epam.test_generator.services.StatesService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * This controller handle case events.
 */
@RestController
@RequestMapping("/events")
public class StatesController {

    @Autowired
    private StatesService statesService;


    @ApiOperation(value = "Get list of case events available for given status",
        nickname = "getAvailableEvents")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK", response = Event.class,
            responseContainer = "List"),
        @ApiResponse(code = 400, message = "Invalid status name")
    })
    @ApiImplicitParam(name = "status", value = "case status",
        required = true, dataType = "String", paramType = "path")
    @Secured({"ROLE_ADMIN", "ROLE_TEST_ENGINEER", "ROLE_TEST_LEAD", "ROLE_GUEST"})
    @GetMapping("/{status}")
    public ResponseEntity<List<Event>> getAvailableEvents(
        @PathVariable("status") Status status) {

        return new ResponseEntity<>(statesService
            .availableTransitions(status),
            HttpStatus.OK);

    }

    @ApiOperation(value = "Get list of case events", nickname = "getEvents")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK", response = Event.class, responseContainer = "List")
    })
    @Secured({"ROLE_ADMIN", "ROLE_TEST_ENGINEER", "ROLE_TEST_LEAD", "ROLE_GUEST"})
    @GetMapping
    public ResponseEntity<List<Event>> getEvents() {

        return new ResponseEntity<>(
            Arrays.stream(Event.values()).collect(Collectors.toList()),
            HttpStatus.OK);
    }

    @ApiOperation(value = "Get list of case statuses", nickname = "getEvents")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK", response = Status.class, responseContainer = "List")
    })
    @Secured({"ROLE_ADMIN", "ROLE_TEST_ENGINEER", "ROLE_TEST_LEAD", "ROLE_GUEST"})
    @GetMapping("/statuses")
    public ResponseEntity<List<Status>> getStatuses() {

        return new ResponseEntity<>(
            Arrays.stream(Status.values()).collect(Collectors.toList()),
            HttpStatus.OK);
    }

}
