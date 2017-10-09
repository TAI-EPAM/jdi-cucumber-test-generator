package com.epam.test_generator.controllers;


import com.epam.test_generator.dto.CaseDTO;
import com.epam.test_generator.dto.StepDTO;
import com.epam.test_generator.dto.SuitDTO;
import com.epam.test_generator.services.CaseService;
import com.epam.test_generator.services.StepService;
import com.epam.test_generator.services.SuitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

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
        SuitDTO suitDTO = suitService.getSuit(suitId);

        if (suitDTO == null) {

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        CaseDTO caseDTO = caseService.getCase(caseId);
        if (caseDTO == null) {

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (!caseBelongsToSuit(caseDTO, suitDTO)) {

            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }


        return new ResponseEntity<>(stepService.getStepsByCaseId(caseId), HttpStatus.OK);
    }

    @RequestMapping(value = "/suits/{suitId}/cases/{caseId}/steps/{stepId}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<StepDTO> getStepByStepId(@PathVariable(value = "suitId") long suitId, @PathVariable("caseId") long caseId
            , @PathVariable(value = "stepId") long stepId) {
        SuitDTO suitDTO = suitService.getSuit(suitId);


        if (suitDTO == null) {

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        CaseDTO caseDTO = caseService.getCase(caseId);
        if (caseDTO == null) {

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (!caseBelongsToSuit(caseDTO, suitDTO)) {

            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        StepDTO stepDTO = stepService.getStep(stepId);
        if (stepDTO == null) {

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (!stepBelongsToCase(stepDTO, caseDTO)) {

            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<StepDTO>(stepDTO, HttpStatus.OK);

    }

    @RequestMapping(value = "/suits/{suitId}/cases/{caseId}/steps", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity<Long> addStepToCase(@PathVariable("suitId") long suitId, @PathVariable("caseId") long caseId, @RequestBody StepDTO stepDTO) {
        SuitDTO suitDTO = suitService.getSuit(suitId);

        if (suitDTO == null) {

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        CaseDTO caseDTO = caseService.getCase(caseId);
        if (caseDTO == null) {

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (!caseBelongsToSuit(caseDTO, suitDTO) || !stepBelongsToCase(stepDTO, caseDTO)) {

            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(stepService.addStep(stepDTO, caseId), HttpStatus.CREATED);
    }

    @RequestMapping(value = "/suits/{suitId}/cases/{caseId}/steps/{stepId}", method = RequestMethod.PUT, consumes = "application/json")
    public ResponseEntity<Long> updateStep(@PathVariable("caseId") long caseId, @PathVariable("suitId") long suitId, @PathVariable("stepId") long stepId, @RequestBody StepDTO stepDTO) {
        SuitDTO suitDTO = suitService.getSuit(suitId);
        if (suitDTO == null) {

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        CaseDTO caseDTO = caseService.getCase(caseId);
        if (caseDTO == null) {

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (!caseBelongsToSuit(caseDTO, suitDTO)) {

            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        StepDTO checkStepDTO = stepService.getStep(stepId);
        if (!stepBelongsToCase(stepDTO, caseDTO)) {

            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (checkStepDTO == null) {

            return new ResponseEntity<>(stepService.addStep(stepDTO, caseId), HttpStatus.CREATED);
        }
        stepService.updateStep(stepId, stepDTO);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/suits/{suitId}/cases/{caseId}/steps/{stepId}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> removeCase(@PathVariable("suitId") long suitId, @PathVariable("caseId") long caseId
            , @PathVariable("stepId") long stepId) {
        SuitDTO suitDTO = suitService.getSuit(suitId);

        if (suitDTO == null) {

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        CaseDTO caseDTO = caseService.getCase(caseId);
        if (caseDTO == null) {

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (!caseBelongsToSuit(caseDTO, suitDTO)) {

            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        StepDTO checkStepDTO = stepService.getStep(stepId);
        if (checkStepDTO == null) {

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (!stepBelongsToCase(checkStepDTO, caseDTO)) {

            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        stepService.removeStep(stepId);

        return new ResponseEntity<>(HttpStatus.OK);
    }


    private boolean caseBelongsToSuit(CaseDTO caseDTO, SuitDTO suitDTO) {
        List<CaseDTO> caseDTOList = suitDTO.getCases();

        return caseDTOList != null && caseDTOList.stream().anyMatch(caze -> Objects.equals(caze.getId(), caseDTO.getId()));
    }

    private boolean stepBelongsToCase(StepDTO stepDTO, CaseDTO caseDTO) {
        List<StepDTO> stepDTOList = caseDTO.getSteps();

        return stepDTOList != null && stepDTOList.stream().anyMatch(step -> Objects.equals(step.getId(), stepDTO.getId()));
    }

}
