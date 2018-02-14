package com.epam.test_generator.services;

import com.epam.test_generator.dao.interfaces.CaseVersionDAO;
import com.epam.test_generator.transformers.CaseTransformer;

import java.util.Set;
import java.util.stream.Collectors;

import static com.epam.test_generator.services.utils.UtilsService.caseBelongsToSuit;
import static com.epam.test_generator.services.utils.UtilsService.checkNotNull;
import static com.epam.test_generator.services.utils.UtilsService.tagBelongsToCase;

import com.epam.test_generator.dao.interfaces.CaseDAO;
import com.epam.test_generator.dao.interfaces.CaseVersionDAO;
import com.epam.test_generator.dao.interfaces.StepDAO;
import com.epam.test_generator.dao.interfaces.SuitDAO;
import com.epam.test_generator.dao.interfaces.TagDAO;
import com.epam.test_generator.dto.TagDTO;
import com.epam.test_generator.entities.Case;
import com.epam.test_generator.entities.Suit;
import com.epam.test_generator.entities.Tag;
import com.epam.test_generator.transformers.SuitTransformer;
import com.epam.test_generator.transformers.TagTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.epam.test_generator.services.utils.UtilsService.*;

@Transactional
@Service
public class TagService {

    @Autowired
    private SuitService suitService;

    @Autowired
    private CaseService caseService;

    @Autowired
    private TagDAO tagDAO;


    @Autowired
    private TagTransformer tagTransformer;

    @Autowired
    private CaseVersionDAO caseVersionDAO;

    public Set<TagDTO> getAllTagsFromAllCasesInSuit(Long projectId, Long suitId) {
        Suit suit = suitService.getSuit(projectId, suitId);

        return suit.getCases().stream()
                .flatMap(caze -> caze.getTags().stream())
                .map(tagTransformer::toDto)
                .collect(Collectors.toSet());
    }

    /**
     * Adds tag with info specified in tagDTO
     * @param projectId id of project
     * @param suitId id of suit
     * @param caseId id of case
     * @param tagDTO info to add
     * @return id of tag
     */
    public Long addTagToCase(Long projectId, Long suitId, Long caseId, TagDTO tagDTO) {
        Suit suit = suitService.getSuit(projectId, suitId);

        Case caze = caseService.getCase(projectId, suitId, caseId);

        caseBelongsToSuit(caze, suit);
        Tag tag = tagTransformer.fromDto(tagDTO);

        tag = tagDAO.save(tag);
        caze.getTags().add(tag);

        caseVersionDAO.save(caze);

        return tag.getId();
    }

    /**
     * Updates tag by id to info specified in tagDTO
     * @param projectId id of project
     * @param suitId id of suit
     * @param caseId id of case
     * @param tagId id of tag to update
     * @param tagDTO info to update
     */
    public void updateTag(Long projectId, Long suitId, Long caseId, Long tagId, TagDTO tagDTO) {
        Suit suit = suitService.getSuit(projectId, suitId);

        Case caze = caseService.getCase(projectId, suitId, caseId);

        caseBelongsToSuit(caze, suit);

        Tag tag = tagDAO.findOne(tagId);
        checkNotNull(tag);

        tagBelongsToCase(tag, caze);

        tagTransformer.mapDTOToEntity(tagDTO, tag);

        tagDAO.save(tag);

        caseVersionDAO.save(caze);
    }

    /**
     * Removes tag from case by id and saves case version to database
     * @param projectId id of project
     * @param suitId id of suit
     * @param caseId id of case
     * @param tagId id of tag to delete
     */
    public void removeTag(Long projectId, Long suitId, Long caseId, Long tagId) {
        Suit suit = suitService.getSuit(projectId, suitId);

        Case caze = caseService.getCase(projectId, suitId, caseId);

        caseBelongsToSuit(caze, suit);

        Tag tag = tagDAO.findOne(tagId);
        checkNotNull(tag);

        tagBelongsToCase(tag, caze);

        caze.getTags().remove(tag);

        tagDAO.delete(tagId);

        caseVersionDAO.save(caze);
    }
}