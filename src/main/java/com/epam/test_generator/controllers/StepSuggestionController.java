package com.epam.test_generator.controllers;

import com.epam.test_generator.dto.StepSuggestionDTO;
import com.epam.test_generator.entities.StepSuggestion;
import com.epam.test_generator.entities.StepType;
import com.epam.test_generator.services.StepSuggestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class StepSuggestionController {

    @Autowired
    private StepSuggestionService stepSuggestionService;

    @RequestMapping(value = "/stepSuggestions", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<StepSuggestionDTO>> getStepsSuggestions() {

        return new ResponseEntity<>(stepSuggestionService.getStepsSuggestions(), HttpStatus.OK);
    }

    @RequestMapping(value = "/stepSuggestions/{typeId}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<StepSuggestionDTO>> getStepsSuggestionsByType(@PathVariable("typeId") long typeId) {

        return new ResponseEntity<>(stepSuggestionService.getStepsSuggestionsByType(typeId), HttpStatus.OK);
    }

    @RequestMapping(value = "/stepSuggestions", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity<Long> addStepSuggestion(@RequestBody StepSuggestionDTO stepSuggestionDTO) {

        return new ResponseEntity<>(stepSuggestionService.addStepSuggestion(stepSuggestionDTO), HttpStatus.OK);
    }

    @RequestMapping(value = "/stepSuggestions/{stepSuggestionId}", method = RequestMethod.PUT, produces = "application/json")
    public ResponseEntity<Long> updateStepSuggestion(@PathVariable("stepSuggestionId") long stepSuggestionId, @RequestBody StepSuggestionDTO stepSuggestionDTO) {
        StepSuggestionDTO checkedStepSuggestion = stepSuggestionService.getStepsSuggestion(stepSuggestionId);
        if (checkedStepSuggestion == null) {

            return new ResponseEntity<>(stepSuggestionService.addStepSuggestion(stepSuggestionDTO), HttpStatus.CREATED);
        }
        stepSuggestionService.updateStepSuggestion(stepSuggestionId, stepSuggestionDTO);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/stepSuggestions/{stepSuggestionId}", method = RequestMethod.DELETE, produces = "application/json")
    public ResponseEntity<Void> removeStepSuggestion(@PathVariable("stepSuggestionId") long stepSuggestionId) {
        if (stepSuggestionService.getStepsSuggestion(stepSuggestionId) == null) {

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        stepSuggestionService.removeStepSuggestion(stepSuggestionId);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
