package com.epam.test_generator.services;

import static com.epam.test_generator.services.utils.UtilsService.caseBelongsToSuit;
import static com.epam.test_generator.services.utils.UtilsService.checkNotNull;
import static com.epam.test_generator.services.utils.UtilsService.tagBelongsToCase;

import com.epam.test_generator.controllers.tag.request.TagCreateDTO;
import com.epam.test_generator.controllers.tag.request.TagUpdateDTO;
import com.epam.test_generator.dao.interfaces.CaseVersionDAO;
import com.epam.test_generator.dao.interfaces.SuitVersionDAO;
import com.epam.test_generator.dao.interfaces.TagDAO;
import com.epam.test_generator.controllers.tag.response.TagDTO;
import com.epam.test_generator.entities.Case;
import com.epam.test_generator.entities.Suit;
import com.epam.test_generator.entities.Tag;
import com.epam.test_generator.controllers.tag.TagTransformer;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private ProjectService projectService;

    @Autowired
    private TagTransformer tagTransformer;

    @Autowired
    private CaseVersionDAO caseVersionDAO;

    @Autowired
    private SuitVersionDAO sutiVersionDAO;


    /**
     *  Return all tags from project's Suits and Cases.
     */
    public Set<TagDTO> getAllProjectTags(long projectId) {
        Set<TagDTO> tagCases = projectService.getProjectByProjectId(projectId).getSuits()
                .stream()
                .flatMap(suit -> suit.getCases().stream())
                .flatMap(c -> c.getTags().stream())
                .map(tag -> tagTransformer.toDto(tag))
                .collect(Collectors.toSet());

        Set<TagDTO> tagSuits = projectService.getProjectByProjectId(projectId).getSuits()
                .stream()
                .flatMap(suit -> suit.getTags().stream())
                .map(tag -> tagTransformer.toDto(tag))
                .collect(Collectors.toSet());

        return Stream.of(tagCases, tagSuits)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    public Set<TagDTO> getAllTagsFromAllCasesInSuit(Long projectId, Long suitId) {
        Suit suit = suitService.getSuit(projectId, suitId);

        return suit.getCases().stream()
                .flatMap(caze -> caze.getTags().stream())
                .map(tagTransformer::toDto)
                .collect(Collectors.toSet());
    }

    /**
     * Adds tag with info specified in tagDTO
     *
     * @param projectId id of project
     * @param suitId    id of suit
     * @param caseId    id of case
     * @param tagDTO    info to add
     * @return id of tag
     */
    public Long addTagToCase(Long projectId, Long suitId, Long caseId, TagCreateDTO tagDTO) {
        Suit suit = suitService.getSuit(projectId, suitId);

        Case caze = caseService.getCase(projectId, suitId, caseId);

        caseBelongsToSuit(caze, suit);
        Tag tag = tagTransformer.fromDto(tagDTO);

        tag = tagDAO.save(tag);
        caze.getTags().add(tag);

        caseVersionDAO.save(caze);
        sutiVersionDAO.save(suit);

        return tag.getId();
    }

    /**
     * Updates tag by id to info specified in tagDTO
     *
     * @param projectId id of project
     * @param suitId    id of suit
     * @param caseId    id of case
     * @param tagId     id of tag to update
     * @param tagDTO    info to update
     */
    public void updateTag(Long projectId, Long suitId, Long caseId, Long tagId, TagUpdateDTO tagDTO) {
        Suit suit = suitService.getSuit(projectId, suitId);

        Case caze = caseService.getCase(projectId, suitId, caseId);

        caseBelongsToSuit(caze, suit);

        Tag tag = tagDAO.findOne(tagId);
        checkNotNull(tag);

        tagBelongsToCase(tag, caze);

        tag = tagTransformer.updateFromDto(tagDTO, tag);

        tagDAO.save(tag);

        caseVersionDAO.save(caze);
        sutiVersionDAO.save(suit);
    }

    /**
     * Removes tag from case by id and saves case version to database
     *
     * @param projectId id of project
     * @param suitId    id of suit
     * @param caseId    id of case
     * @param tagId     id of tag to delete
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
        sutiVersionDAO.save(suit);
    }
}