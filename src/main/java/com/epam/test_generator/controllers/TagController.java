package com.epam.test_generator.controllers;


import com.epam.test_generator.dto.CaseDTO;
import com.epam.test_generator.dto.SuitDTO;
import com.epam.test_generator.dto.TagDTO;
import com.epam.test_generator.services.CaseService;
import com.epam.test_generator.services.SuitService;
import com.epam.test_generator.services.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
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
        SuitDTO suitDTO = suitService.getSuit(suitId);
        if (suitDTO == null) {

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(tagService.getAllTagsFromAllCasesInSuit(suitId), HttpStatus.OK);
    }

    @RequestMapping(value = "/suits/{suitId}/cases/{caseId}/tags", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Set<TagDTO>> getTags(@PathVariable("suitId") long suitId, @PathVariable("caseId") long caseId) {
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

        return new ResponseEntity<>(caseDTO.getTags(), HttpStatus.OK);
    }

    @RequestMapping(value = "/suits/{suitId}/cases/{caseId}/tags", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<Long> addTagToCase(@PathVariable("suitId") long suitId, @PathVariable("caseId") long caseId, @RequestBody TagDTO tagDTO) {
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

        return new ResponseEntity<>(tagService.addTagToCase(tagDTO, caseId), HttpStatus.CREATED);
    }

    @RequestMapping(value = "/suits/{suitId}/cases/{caseId}/tags/{tagId}", method = RequestMethod.PUT, consumes = "application/json")
    public ResponseEntity<Long> updateCase(@PathVariable("suitId") long suitId, @PathVariable("caseId") long caseId, @PathVariable("tagId") long tagId, @RequestBody TagDTO tagDTO) {
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

        TagDTO checkTagDTO = tagService.getTag(tagId);
        if(checkTagDTO == null){

            return new ResponseEntity<>(tagService.addTagToCase(tagDTO, caseId), HttpStatus.CREATED);
        }

        if (!tagBelongsToCase(checkTagDTO, caseDTO)) {

            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        tagService.updateTag(tagId, tagDTO);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/suits/{suitId}/cases/{caseId}/tags/{tagId}", method = RequestMethod.DELETE, produces = "application/json")
    public ResponseEntity<Void> deleteTagFromCase(@PathVariable("suitId") long suitId, @PathVariable("caseId") long caseId, @PathVariable("tagId") long tagId) {
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

        TagDTO tagDTO = tagService.getTag(tagId);
        if(tagDTO == null){

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (!tagBelongsToCase(tagDTO, caseDTO)) {

            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        tagService.removeTag(caseId, tagId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private boolean caseBelongsToSuit(CaseDTO caseDTO, SuitDTO suitDTO) {
        List<CaseDTO> caseDTOList = suitDTO.getCases();

        return caseDTOList != null && caseDTOList.stream().anyMatch(caze -> Objects.equals(caze.getId(), caseDTO.getId()));
    }

    private boolean tagBelongsToCase(TagDTO tagDTO, CaseDTO caseDTO){
        Set<TagDTO> tagDTOSet = caseDTO.getTags();

        return tagDTOSet != null && tagDTOSet.stream().anyMatch(tag -> Objects.equals(tag.getId(), tagDTO.getId()));
    }

}
