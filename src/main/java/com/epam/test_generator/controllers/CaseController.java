package com.epam.test_generator.controllers;

import com.epam.test_generator.dto.CaseDTO;
import com.epam.test_generator.dto.SuitDTO;
import com.epam.test_generator.services.CaseService;
import com.epam.test_generator.services.SuitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class CaseController {

    @Autowired
    private CaseService caseService;

    @Autowired
    private SuitService suitService;

    @RequestMapping(value = "/suits/{suitId}/cases", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<CaseDTO>> getCases(@PathVariable("suitId") long suitId) {
        SuitDTO suitDTO = suitService.getSuit(suitId);

        return new ResponseEntity<>(suitDTO.getCases(), HttpStatus.OK);
    }

    @RequestMapping(value = "/suits/{suitId}/cases/{caseId}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<CaseDTO> getCase(@PathVariable("suitId") long suitId, @PathVariable("caseId") long caseId) {

        return new ResponseEntity<>(caseService.getCase(suitId, caseId), HttpStatus.OK);
    }

    @RequestMapping(value = "/suits/{suitId}/cases", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity<Long> addCaseToSuit(@PathVariable("suitId") long suitId, @RequestBody @Valid CaseDTO caseDTO) {

        return new ResponseEntity<>(caseService.addCaseToSuit(suitId, caseDTO), HttpStatus.CREATED);
    }

    @RequestMapping(value = "/suits/{suitId}/cases/{caseId}", method = RequestMethod.PUT, consumes = "application/json")
    public ResponseEntity<Void> updateCase(@PathVariable("suitId") long suitId,
                                           @PathVariable("caseId") long caseId,
                                           @RequestBody @Valid CaseDTO caseDTO) {
        caseService.updateCase(suitId, caseId, caseDTO);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/suits/{suitId}/cases/{caseId}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> removeCase(@PathVariable("suitId") long suitId, @PathVariable("caseId") long caseId) {
        caseService.removeCase(suitId, caseId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/suits/{suitId}/cases", method = RequestMethod.DELETE, consumes = "application/json")
    public ResponseEntity<Void> removeCases(@PathVariable("suitId") long suitId, @RequestBody @Valid SuitDTO suitDTO) {
        List<Long> caseIds = suitDTO.getCases().stream().map(CaseDTO::getId).collect(Collectors.toList());
        caseService.removeCases(suitId, caseIds);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}