package com.epam.test_generator.controllers;

import com.epam.test_generator.dto.CaseDTO;
import com.epam.test_generator.dto.SuitDTO;
import com.epam.test_generator.services.CaseService;
import com.epam.test_generator.services.SuitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
public class CaseController {

    @Autowired
    private CaseService casesService;

    @Autowired
    private SuitService suitService;

    @RequestMapping(value = "/suits/{suitId}/cases", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<CaseDTO>> getCases(@PathVariable("suitId") long suitId) {
        SuitDTO suitDTO = suitService.getSuit(suitId);

        if (suitDTO != null) {

            return new ResponseEntity<>(casesService.getCases(suitDTO), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "/suits/{suitId}/cases/{caseId}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<CaseDTO> getCase(@PathVariable("suitId") long suitId, @PathVariable("caseId") long caseId) {
        SuitDTO suitDTO = suitService.getSuit(suitId);

        if (suitDTO == null) {

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        CaseDTO caseDTO = casesService.getCase(caseId);

        if (caseDTO == null) {

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (!caseBelongsToSuit(caseDTO, suitDTO)) {

            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(caseDTO, HttpStatus.OK);
    }

    @RequestMapping(value = "/suits/{suitId}/cases", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity<Long> addCaseToSuit(@PathVariable("suitId") long suitId, @RequestBody CaseDTO caseDTO) {
        SuitDTO suitDTO = suitService.getSuit(suitId);

        if (suitDTO != null) {

            return new ResponseEntity<>(casesService.addCaseToSuit(caseDTO, suitId), HttpStatus.CREATED);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "/suits/{suitId}/cases/{caseId}", method = RequestMethod.PUT, consumes = "application/json")
    public ResponseEntity<Long> updateCase(@PathVariable("suitId") long suitId, @PathVariable("caseId") long caseId, @RequestBody CaseDTO caseDTO) {
        SuitDTO suitDTO = suitService.getSuit(suitId);

        if (suitDTO == null) {

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        CaseDTO checkCaseDTO = casesService.getCase(caseId);

        if (checkCaseDTO == null) {

            return new ResponseEntity<>(casesService.addCaseToSuit(caseDTO, suitId), HttpStatus.CREATED);
        }

        if (!caseBelongsToSuit(checkCaseDTO, suitDTO)) {

            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        casesService.updateCase(suitId, caseDTO);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/suits/{suitId}/cases/{caseId}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> removeCase(@PathVariable("suitId") long suitId, @PathVariable("caseId") long caseId) {
        SuitDTO suitDTO = suitService.getSuit(suitId);

        if (suitDTO == null) {

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        CaseDTO caseDTO = casesService.getCase(caseId);

        if (caseDTO == null) {

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (!caseBelongsToSuit(caseDTO, suitDTO)) {

            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        casesService.removeCase(suitId, caseId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/suits/{suitId}/cases", method = RequestMethod.DELETE, consumes = "application/json")
    public ResponseEntity<Void> removeCases(@PathVariable("suitId") long suitId, @RequestBody SuitDTO suitDTO) {
        SuitDTO checkSuitDTO = suitService.getSuit(suitId);

        if (checkSuitDTO == null) {

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<Long> caseIds = suitDTO.getCases().stream().map(CaseDTO::getId).collect(Collectors.toList());
        casesService.removeCases(suitId, caseIds);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private boolean caseBelongsToSuit(CaseDTO caseDTO, SuitDTO suitDTO) {
        List<CaseDTO> caseDTOList = suitDTO.getCases();

        return caseDTOList != null && caseDTOList.stream().anyMatch(caze -> Objects.equals(caze.getId(), caseDTO.getId()));
    }
}