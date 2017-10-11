package com.epam.test_generator.controllers;


import com.epam.test_generator.dto.CaseDTO;
import com.epam.test_generator.dto.TagDTO;
import com.epam.test_generator.services.CaseService;
import com.epam.test_generator.services.SuitService;
import com.epam.test_generator.services.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Set;

@RestController
public class TagController {

    @Autowired
    private TagService tagService;

    @Autowired
    private CaseService casesService;

    @Autowired
    private SuitService suitService;

    @RequestMapping(value = "/suits/{suitId}/cases/tags", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Set<TagDTO>> getAllTagsFromAllCasesInSuit(@PathVariable("suitId") long suitId) {

        return new ResponseEntity<>(tagService.getAllTagsFromAllCasesInSuit(suitId), HttpStatus.OK);
    }

    @RequestMapping(value = "/suits/{suitId}/cases/{caseId}/tags", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Set<TagDTO>> getTags(@PathVariable("suitId") long suitId, @PathVariable("caseId") long caseId) {
        CaseDTO caseDTO = casesService.getCase(suitId, caseId);

        return new ResponseEntity<>(caseDTO.getTags(), HttpStatus.OK);
    }

    @RequestMapping(value = "/suits/{suitId}/cases/{caseId}/tags", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<Long> addTagToCase(@PathVariable("suitId") long suitId, @PathVariable("caseId") long caseId, @RequestBody TagDTO tagDTO) {

        return new ResponseEntity<>(tagService.addTagToCase(suitId, caseId, tagDTO), HttpStatus.CREATED);
    }

    @RequestMapping(value = "/suits/{suitId}/cases/{caseId}/tags/{tagId}", method = RequestMethod.PUT, consumes = "application/json")
    public ResponseEntity<Void> updateTag(@PathVariable("suitId") long suitId, @PathVariable("caseId") long caseId, @PathVariable("tagId") long tagId, @RequestBody TagDTO tagDTO) {
        tagService.updateTag(suitId, caseId, tagId, tagDTO);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/suits/{suitId}/cases/{caseId}/tags/{tagId}", method = RequestMethod.DELETE, produces = "application/json")
    public ResponseEntity<Void> removeTagFromCase(@PathVariable("suitId") long suitId, @PathVariable("caseId") long caseId, @PathVariable("tagId") long tagId) {
        tagService.removeTag(suitId, caseId, tagId);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}