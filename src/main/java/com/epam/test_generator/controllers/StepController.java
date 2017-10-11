package com.epam.test_generator.controllers;


import com.epam.test_generator.dto.StepDTO;
import com.epam.test_generator.services.CaseService;
import com.epam.test_generator.services.StepService;
import com.epam.test_generator.services.SuitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class StepController {

    @Autowired
    private SuitService suitService;

    @Autowired
    private CaseService caseService;

    @Autowired
    private StepService stepService;

    @RequestMapping(value = "/suits/{suitId}/cases/{caseId}/steps", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<StepDTO>> getStepsByCaseId(@PathVariable("suitId") long suitId, @PathVariable("caseId") long caseId) {

        return new ResponseEntity<>(stepService.getStepsByCaseId(suitId, caseId), HttpStatus.OK);
    }

    @RequestMapping(value = "/suits/{suitId}/cases/{caseId}/steps/{stepId}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<StepDTO> getStepByStepId(@PathVariable(value = "suitId") long suitId, @PathVariable("caseId") long caseId, @PathVariable(value = "stepId") long stepId) {

        return new ResponseEntity<StepDTO>(stepService.getStep(suitId, caseId, stepId), HttpStatus.OK);

    }

    @RequestMapping(value = "/suits/{suitId}/cases/{caseId}/steps", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity<Long> addStepToCase(@PathVariable("suitId") long suitId, @PathVariable("caseId") long caseId, @RequestBody StepDTO stepDTO) {

        return new ResponseEntity<>(stepService.addStepToCase(suitId, caseId, stepDTO), HttpStatus.CREATED);
    }

    @RequestMapping(value = "/suits/{suitId}/cases/{caseId}/steps/{stepId}", method = RequestMethod.PUT, consumes = "application/json")
    public ResponseEntity<Void> updateStep(@PathVariable("suitId") long suitId, @PathVariable("caseId") long caseId, @PathVariable("stepId") long stepId, @RequestBody StepDTO stepDTO) {
        stepService.updateStep(suitId, caseId, stepId, stepDTO);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/suits/{suitId}/cases/{caseId}/steps/{stepId}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> removeCase(@PathVariable("suitId") long suitId, @PathVariable("caseId") long caseId, @PathVariable("stepId") long stepId) {
        stepService.removeStep(suitId, caseId, stepId);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
