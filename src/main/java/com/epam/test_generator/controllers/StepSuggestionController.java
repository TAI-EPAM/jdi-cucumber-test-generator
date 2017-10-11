package com.epam.test_generator.controllers;

import com.epam.test_generator.dto.StepSuggestionDTO;
import com.epam.test_generator.services.StepSuggestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
    public ResponseEntity<Long> addStepSuggestion(@RequestBody @Valid StepSuggestionDTO stepSuggestionDTO) {

        return new ResponseEntity<>(stepSuggestionService.addStepSuggestion(stepSuggestionDTO), HttpStatus.OK);
    }

    @RequestMapping(value = "/stepSuggestions/{stepSuggestionId}", method = RequestMethod.PUT, produces = "application/json")
    public ResponseEntity<Void> updateStepSuggestion(@PathVariable("stepSuggestionId") long stepSuggestionId,
                                                     @RequestBody @Valid StepSuggestionDTO stepSuggestionDTO) {
        stepSuggestionService.updateStepSuggestion(stepSuggestionId, stepSuggestionDTO);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/stepSuggestions/{stepSuggestionId}", method = RequestMethod.DELETE, produces = "application/json")
    public ResponseEntity<Void> removeStepSuggestion(@PathVariable("stepSuggestionId") long stepSuggestionId) {
        stepSuggestionService.removeStepSuggestion(stepSuggestionId);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
