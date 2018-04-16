package com.epam.test_generator.services;

import static com.epam.test_generator.services.utils.UtilsService.checkNotNull;

import com.epam.test_generator.dao.interfaces.CaseVersionDAO;
import com.epam.test_generator.dao.interfaces.RemovedIssueDAO;
import com.epam.test_generator.dao.interfaces.SuitDAO;
import com.epam.test_generator.dao.interfaces.SuitVersionDAO;
import com.epam.test_generator.dto.StepDTO;
import com.epam.test_generator.dto.SuitDTO;
import com.epam.test_generator.dto.SuitRowNumberUpdateDTO;
import com.epam.test_generator.dto.SuitUpdateDTO;
import com.epam.test_generator.dto.SuitVersionDTO;
import com.epam.test_generator.entities.Project;
import com.epam.test_generator.entities.RemovedIssue;
import com.epam.test_generator.entities.Status;
import com.epam.test_generator.entities.Suit;
import com.epam.test_generator.pojo.SuitVersion;
import com.epam.test_generator.services.exceptions.BadRequestException;
import com.epam.test_generator.transformers.SuitTransformer;
import com.epam.test_generator.transformers.SuitVersionTransformer;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MethodArgumentNotValidException;


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
    private CascadeUpdateService cascadeUpdateService;

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
        Suit suit = checkNotNull(suitDAO.findOne(suitId));
        if (project.hasSuit(suit)){
            return suit;
        }
        else {
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
     * @param suitDTO suit info
     * @return {@link SuitDTO} of added suit
     */
    public SuitDTO addSuit(Long projectId, SuitDTO suitDTO) {
        Project project = projectService.getProjectByProjectId(projectId);
        suitDTO.setJiraProjectKey(project.getJiraKey());
        Suit suit = suitDAO.save(suitTransformer.fromDto(suitDTO));
        suit.setLastModifiedDate(LocalDateTime.now());

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
     * @param suitDTO info to update
     * @return {@link SuitUpdateDTO} which contains {@link SuitDTO} and {@link List<Long>}
     * (in fact id of {@link StepDTO} with FAILED {@link Status} which belong this suit)
     */
    public SuitUpdateDTO updateSuit(long projectId, long suitId, SuitDTO suitDTO)
        throws MethodArgumentNotValidException {
        List<Long> failedStepIds = cascadeUpdateService
            .cascadeSuitCasesUpdate(projectId, suitId, suitDTO);
        Suit suit = getSuit(projectId, suitId);
        SuitDTO simpleSuitDTO = getSimpleSuitDTO(suitDTO);

        suitTransformer.mapDTOToEntity(simpleSuitDTO, suit);
        suit.setLastModifiedDate(LocalDateTime.now());
        suitDAO.save(suit);


        suitVersionDAO.save(suit);
        caseVersionDAO.save(suit.getCases());

        return new SuitUpdateDTO(suitTransformer.toDto(suit), failedStepIds);
    }

    /**
     * Removes suit from project by id
     *
     * @param projectId id of project where to delete case
     * @param suitId id of case to delete
     * @return {@link SuitDTO) of removed suit
     */
    public SuitDTO removeSuit(long projectId, long suitId) {
        Suit suit = getSuit(projectId, suitId);

        if (suit.isImportedFromJira()) {
            removedIssueDAO.save(new RemovedIssue(suit.getJiraKey()));
        }

        suitDAO.delete(suitId);

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

        suitDAO.save(suits);
        suitVersionDAO.save(suits);

        return rowNumberUpdates;
    }

    private SuitDTO getSimpleSuitDTO(SuitDTO suitDTO) {
        SuitDTO snapShot = new SuitDTO();
        snapShot.setId(suitDTO.getId());
        snapShot.setName(suitDTO.getName());
        snapShot.setDescription(suitDTO.getDescription());
        snapShot.setPriority(suitDTO.getPriority());
        snapShot.setTags(suitDTO.getTags());
        snapShot.setStatus(suitDTO.getStatus());
        snapShot.setCreationDate(suitDTO.getCreationDate());
        snapShot.setRowNumber(suitDTO.getRowNumber());
        return snapShot;
    }

    public List<SuitVersionDTO> getSuitVersions(Long projectId, Long suitId) {
        Suit suit = getSuit(projectId, suitId);

        List<SuitVersion> suitVersions = suitVersionDAO.findAll(suit.getId());
        return suitVersionTransformer.toDtoList(suitVersions);
    }

    /**
     * Restores suit to previous version by suitId and commitId
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