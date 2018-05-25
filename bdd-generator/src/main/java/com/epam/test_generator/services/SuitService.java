package com.epam.test_generator.services;

import static com.epam.test_generator.services.utils.UtilsService.checkNotNull;

import com.epam.test_generator.controllers.step.response.StepDTO;
import com.epam.test_generator.controllers.suit.SuitTransformer;
import com.epam.test_generator.controllers.suit.request.SuitCreateDTO;
import com.epam.test_generator.controllers.suit.request.SuitRowNumberUpdateDTO;
import com.epam.test_generator.controllers.suit.request.SuitUpdateDTO;
import com.epam.test_generator.controllers.suit.response.SuitDTO;
import com.epam.test_generator.dao.interfaces.CaseVersionDAO;
import com.epam.test_generator.dao.interfaces.RemovedIssueDAO;
import com.epam.test_generator.dao.interfaces.SuitDAO;
import com.epam.test_generator.dao.interfaces.SuitVersionDAO;
import com.epam.test_generator.dto.SuitVersionDTO;
import com.epam.test_generator.entities.Project;
import com.epam.test_generator.entities.RemovedIssue;
import com.epam.test_generator.entities.Status;
import com.epam.test_generator.entities.Suit;
import com.epam.test_generator.pojo.SuitVersion;
import com.epam.test_generator.services.exceptions.BadRequestException;
import com.epam.test_generator.services.exceptions.NotFoundException;
import com.epam.test_generator.transformers.SuitVersionTransformer;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class SuitService {

    @Autowired
    private SuitDAO suitDAO;

    @Autowired
    private SuitTransformer suitTransformer;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private CaseVersionDAO caseVersionDAO;

    @Autowired
    private RemovedIssueDAO removedIssueDAO;

    @Autowired
    private SuitVersionDAO suitVersionDAO;

    @Autowired
    private SuitVersionTransformer suitVersionTransformer;

    public List<SuitDTO> getSuitsDTO() {
        return suitTransformer.toDtoList(suitDAO.findAll());
    }

    public List<Suit> getSuits() {
        return suitDAO.findAll();
    }

    public Suit getSuit(long projectId, long suitId) {
        Project project = projectService.getProjectByProjectId(projectId);
        Suit suit = suitDAO.findById(suitId).orElseThrow(NotFoundException::new);
        if (project.hasSuit(suit)) {
            return suit;
        } else {
            throw new BadRequestException(
                String.format("Error: project %s does not have suit %s", project.getName(),
                    suit.getName()));
        }
    }

    public SuitDTO getSuitDTO(long projectId, long suitId) {
        return suitTransformer.toDto(getSuit(projectId, suitId));
    }

    /**
     * Adds suit specified in suitDTO to project by id
     *
     * @param projectId if of project where to add case
     * @param suitCreateDTO suit info
     * @return {@link SuitDTO} of added suit
     */
    public SuitDTO addSuit(Long projectId, SuitCreateDTO suitCreateDTO) {
        Project project = projectService.getProjectByProjectId(projectId);
        Suit suit = suitDAO.save(suitTransformer.fromDto(suitCreateDTO));
        suit.setStatus(Status.NOT_DONE);
        ZonedDateTime currentDateTime = ZonedDateTime.now();
        suit.setCreationDate(currentDateTime);
        suit.setUpdateDate(currentDateTime);
        suit.setLastModifiedDate(currentDateTime);

        suitVersionDAO.save(suit);

        project.addSuit(suit);

        caseVersionDAO.save(suit.getCases());

        return suitTransformer.toDto(suit);
    }

    public Suit getSuitByJiraKey(String key) {
        return checkNotNull(suitDAO.findByJiraKey(key));
    }

    /**
     * Updates suit by id with info specified in suitDTO
     *
     * @param projectId id of project
     * @param suitId id of suit to update
     * @param suitUpdateDTO info to update
     * @return {@link SuitDTO}
     */
    public SuitDTO updateSuit(long projectId, long suitId, SuitUpdateDTO suitUpdateDTO) {

        Suit suit = getSuit(projectId, suitId);

        suitTransformer.updateFromDto(suitUpdateDTO, suit);
        ZonedDateTime now = ZonedDateTime.now();
        suit.setUpdateDate(now);
        suit.setLastModifiedDate(now);

        suitDAO.save(suit);
        suitVersionDAO.save(suit);
        caseVersionDAO.save(suit.getCases());

        return suitTransformer.toDto(suit);
    }

    /**
     * Removes suit from project by id
     *
     * @param projectId id of project where to delete case
     * @param suitId id of case to delete
     * @return {@link SuitDTO} of removed suit
     */
    public SuitDTO removeSuit(long projectId, long suitId) {
        Suit suit = getSuit(projectId, suitId);

        if (suit.isImportedFromJira()) {
            removedIssueDAO.save(new RemovedIssue(suit.getJiraKey()));
        }

        suitDAO.delete(suit);

        suitVersionDAO.delete(suit);
        caseVersionDAO.delete(suit.getCases());

        Project project = projectService.getProjectByProjectId(projectId);
        project.removeSuit(suit);
        return suitTransformer.toDto(suit);
    }

    public List<SuitDTO> getSuitsFromProject(Long projectId) {
        Project project = projectService.getProjectByProjectId(projectId);
        return suitTransformer.toDtoList(project.getSuits());
    }

    /**
     * Updates suit's rowNumbers by suit's ids specified in List of SuitRowNumberUpdateDTOs
     *
     * @param rowNumberUpdates List of SuitRowNumberUpdateDTOs
     * @return list of {@link SuitRowNumberUpdateDTO} to check on the frontend
     */
    public List<SuitRowNumberUpdateDTO> updateSuitRowNumber(List<SuitRowNumberUpdateDTO>
                                                                rowNumberUpdates) {
        if (rowNumberUpdates.isEmpty()) {
            throw new BadRequestException("The list has not to be empty");
        }

        for (SuitRowNumberUpdateDTO update : rowNumberUpdates) {
            if (Objects.isNull(update.getId()) || Objects.isNull(update.getRowNumber())) {
                throw new BadRequestException("Id or rowNumber has not to be null");
            }
        }

        Map<Long, Integer> patch = rowNumberUpdates
            .stream()
            .collect(Collectors
                .toMap(SuitRowNumberUpdateDTO::getId, SuitRowNumberUpdateDTO::getRowNumber));

        List<Integer> distinct = patch
            .values()
            .stream()
            .distinct()
            .collect(Collectors.toList());

        if (rowNumberUpdates.size() != distinct.size()) {
            throw new BadRequestException("One or more of the rowNumbers is a duplicate");
        }

        List<Suit> suits = suitDAO.findByIdInOrderById(patch.keySet());

        if (suits.size() != patch.size()) {
            throw new BadRequestException(
                "One or more of the ids is a duplicate or it does not exist in the database");
        }
        for (Suit suit : suits) {
            suit.setRowNumber(patch.get(suit.getId()));
        }

        suitDAO.saveAll(suits);
        suitVersionDAO.save(suits);

        return rowNumberUpdates;
    }

    public List<SuitVersionDTO> getSuitVersions(Long projectId, Long suitId) {
        Suit suit = getSuit(projectId, suitId);

        List<SuitVersion> suitVersions = suitVersionDAO.findAll(suit.getId());
        return suitVersionTransformer.toDtoList(suitVersions);
    }

    /**
     * Restores suit to previous version by suitId and commitId
     *
     * @param projectId id of project where to restore suit
     * @param suitId id of suit to restore
     * @param commitId id of commit to restore version
     * @return {@link SuitDTO} of restored by the commitId case
     */
    public SuitDTO restoreSuit(Long projectId, Long suitId, String commitId) {
        Suit suit = getSuit(projectId, suitId);
        Suit suitToRestore = checkNotNull(suitVersionDAO.findByCommitId(suit.getId(), commitId));

        Suit restoredSuit = suitDAO.save(suitToRestore);
        suitVersionDAO.save(suitToRestore);
        caseVersionDAO.save(suitToRestore.getCases());

        return suitTransformer.toDto(restoredSuit);
    }
}