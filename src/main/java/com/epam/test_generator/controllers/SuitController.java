package com.epam.test_generator.controllers;

import com.epam.test_generator.dto.CaseDTO;
import com.epam.test_generator.dto.SuitDTO;
import com.epam.test_generator.services.SuitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class SuitController {

    @Autowired
    SuitService suitService;


	@RequestMapping(value = "/")
    public String getMainPage() {
        return "/WEB-INF/static/views/newSuits";
    }

    @RequestMapping(value = "/suggestion_manager")
    public String getStepSuggestionsPage() {
        return "/WEB-INF/static/views/stepSuggestions";
    }

    @RequestMapping(value = "/suits", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ResponseEntity<List<SuitDTO>> getSuits() {
		return new ResponseEntity<>(suitService.getSuits(), HttpStatus.OK);
    }

    @RequestMapping(value = "/suit/{suitId}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<SuitDTO> getSuit(@PathVariable("suitId") long id){
        SuitDTO suitDTO = suitService.getSuit(id);

		if (suitDTO != null) {
			return new ResponseEntity<>(suitDTO, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value="/suit/{suitId}", method = RequestMethod.PUT, consumes = "application/json")
    public ResponseEntity<Void> editSuit( @PathVariable("suitId") long id, @RequestBody @Valid SuitDTO suitDTO){
        suitService.updateSuit(suitDTO);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/suit/{suitId}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> removeSuit(@PathVariable("suitId") long id){
        SuitDTO suitDTO = suitService.getSuit(id);

        if (suitDTO != null) {
            suitService.removeSuit(id);

            return new ResponseEntity<>(HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value="/suit", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    @ResponseBody
    public ResponseEntity<SuitDTO> addSuit(@RequestBody @Valid SuitDTO suitDTO) {
        return new ResponseEntity<>(suitService.addSuit(suitDTO), HttpStatus.OK);
    }
    

    @RequestMapping(value = "/downloadFeatureFile", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public ResponseEntity<String> downloadFile(@RequestBody @Valid SuitDTO suitDTO) throws IOException {
        List<Long> caseIds = suitDTO.getCases().stream().map(CaseDTO::getId).collect(Collectors.toList());

        return  new ResponseEntity<>(suitService.generateFile(suitDTO.getId(), caseIds), HttpStatus.OK);
    }
}