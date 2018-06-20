package com.epam.test_generator.services;

import static com.epam.test_generator.services.utils.UtilsService.checkNotNull;

import com.epam.test_generator.controllers.caze.CaseTransformer;
import com.epam.test_generator.controllers.caze.request.CaseCreateDTO;
import com.epam.test_generator.controllers.caze.request.CaseRowNumberUpdateDTO;
import com.epam.test_generator.controllers.caze.request.CaseUpdateDTO;
import com.epam.test_generator.controllers.caze.response.CaseDTO;
import com.epam.test_generator.controllers.version.caze.CaseVersionTransformer;
import com.epam.test_generator.controllers.version.caze.response.CaseVersionDTO;
import com.epam.test_generator.dao.interfaces.CaseDAO;
import com.epam.test_generator.dao.interfaces.CaseVersionDAO;
import com.epam.test_generator.dao.interfaces.RemovedIssueDAO;
import com.epam.test_generator.dao.interfaces.SuitVersionDAO;
import com.epam.test_generator.entities.Case;
import com.epam.test_generator.entities.Event;
import com.epam.test_generator.entities.RemovedIssue;
import com.epam.test_generator.entities.Status;
import com.epam.test_generator.entities.Suit;
import com.epam.test_generator.pojo.CaseVersion;
import com.epam.test_generator.services.exceptions.BadRequestException;
import com.epam.test_generator.services.exceptions.NotFoundException;
import com.epam.test_generator.state.machine.StateMachineAdapter;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class CaseService {

    @Autowired
    private CaseTransformer caseTransformer;

    @Autowired
    private CaseDAO caseDAO;

    @Autowired
    private SuitService suitService;

    @Autowired
    private StepSuggestionService stepSuggestionService;

    @Autowired
    private CaseVersionDAO caseVersionDAO;

    @Autowired
    private SuitVersionDAO suitVersionDAO;

    @Autowired
    private StateMachineAdapter stateMachineAdapter;

    @Autowired
    private RemovedIssueDAO removedIssueDAO;

    @Autowired
    private CaseVersionTransformer caseVersionTransformer;

    public Case getCase(Long projectId, Long suitId, Long caseId) {
        Suit suit = suitService.getSuit(projectId, suitId);
        Case caze = caseDAO.findById(caseId).orElseThrow(NotFoundException::new);
        throwExceptionIfCaseIsNotInSuit(suit, caze);
        return caze;
    }

    public CaseDTO getCaseDTO(Long projectId, Long suitId, Long caseId) {
        return caseTransformer.toDto(getCase(projectId, suitId, caseId));
    }

    /**
     * Adds case to existing suit
     *
     * @param projectId id of project where to add case
     * @param suitId id of suit where to add case
     * @param caseCreateDTO case to add
     * @return {@link CaseDTO} of added case to suit
     */
    public CaseDTO addCaseToSuit(Long projectId, Long suitId, @Valid CaseCreateDTO caseCreateDTO) {
        Suit suit = suitService.getSuit(projectId, suitId);

        Case caze = caseTransformer.fromDto(caseCreateDTO);

        caze.setJiraParentKey(suit.getJiraKey());
        caze.setJiraProjectKey(suit.getJiraProjectKey());
        ZonedDateTime currentTime = ZonedDateTime.now();
        caze.setCreationDate(currentTime);
        caze.setUpdateDate(currentTime);
        caze.setLastModifiedDate(currentTime);
        caze.setStatus(Status.NOT_DONE);

        suit.addCase(caze);

        caze = caseDAO.save(caze);
        caseVersionDAO.save(caze);
        suitVersionDAO.save(suit);

        return caseTransformer.toDto(caze);
    }

    /**
     * Updates case info to info specified in CaseUpdateDTO
     *
     * @param projectId id of project where to update case
     * @param suitId id of suit where to update case
     * @param caseId id of case which to update
     * @param caseUpdateDTO info to update
     * @return {@link CaseDTO}
     */
    public CaseDTO updateCase(Long projectId, Long suitId, Long caseId,
                              CaseUpdateDTO caseUpdateDTO) {
        Suit suit = suitService.getSuit(projectId, suitId);

        Case caze = caseDAO.findById(caseId).orElseThrow(NotFoundException::new);

        throwExceptionIfCaseIsNotInSuit(suit, caze);

        Case updatedCase = caseTransformer.updateFromDto(caseUpdateDTO, caze);

        CaseDTO updatedCaseDTO = caseTransformer.toDto(updatedCase);

        caseVersionDAO.save(caze);
        suitVersionDAO.save(suit);

        return updatedCaseDTO;
    }

    /**
     * Deletes multiple cases by ids
     *
     * @param projectId id of project where to delete cases
     * @param suitId id of suit where to delete cases
     * @param caseIds list of cases ids to delete
     * @return removed list of {@link CaseDTO}
     */
    public List<CaseDTO> removeCases(Long projectId, Long suitId, List<Long> caseIds) {
        Suit suit = suitService.getSuit(projectId, suitId);

        List<Case> removedCases = new ArrayList<>();

        suit.getCases().stream()
            .filter(caze -> caseIds.stream()
                .anyMatch(id -> id.equals(caze.getId())))
            .forEach(caze -> {
                stepSuggestionService.removeSteps(projectId, caze.getSteps());
                caseDAO.deleteById(caze.getId());

                caseVersionDAO.delete(caze);

                removedCases.add(caze);
                saveIssueToDeleteInJira(caze);

            });

        removedCases.forEach(suit::removeCase);

        return caseTransformer.toDtoList(removedCases);
    }

    private void saveIssueToDeleteInJira(Case caze) {
        if (caze.isImportedFromJira()) {
            removedIssueDAO.save(new RemovedIssue(caze.getJiraKey()));
        }
    }

    public List<CaseVersionDTO> getCaseVersions(Long projectId, Long suitId, Long caseId) {
        Case caze = getCase(projectId, suitId, caseId);

        List<CaseVersion> caseVersions = caseVersionDAO.findAll(caze.getId());

        return caseVersionTransformer.toListDto(caseVersions);
    }

    /**
     * Restores case to previous version by caseId and commitId
     *
     * @param projectId id of project where to restore case
     * @param suitId id of suit where to restore case
     * @param caseId id of case to restore
     * @param commitId id of commit to restore version
     * @return {@link CaseDTO} of restored by the commitId case
     */
    public CaseDTO restoreCase(Long projectId, Long suitId, Long caseId, String commitId) {
        Suit suit = suitService.getSuit(projectId, suitId);
        Case caze = getCase(projectId, suitId, caseId);

        Case caseToRestore = checkNotNull(
            caseVersionDAO.findByCommitId(caze.getId(), commitId));

        Case restoredCase = caseDAO.save(caseToRestore);
        caseVersionDAO.save(caseToRestore);
        suitVersionDAO.save(suit);

        return caseTransformer.toDto(restoredCase);
    }

    public Status performEvent(Long projectId, Long suitId, Long caseId, Event event)
        throws Exception {
        Case cs = getCase(projectId, suitId, caseId);
        StateMachine<Status, Event> stateMachine = stateMachineAdapter.restore(cs);
        if (stateMachine.sendEvent(event)) {
            stateMachineAdapter.persist(stateMachine, cs);

            caseVersionDAO.save(cs);
            suitVersionDAO.save(suitService.getSuit(projectId, suitId));

            return cs.getStatus();
        } else {
            throw new BadRequestException();
        }
    }

    /**
     * Updates cases'rowNumbers by cases' ids specified in List of CaseRowNumberUpdateDTOs
     *
     * @param rowNumberUpdates List of CaseRowNumberUpdateDTOs
     * @return list of {@link CaseRowNumberUpdateDTO} to check on the frontend
     */
    public List<CaseRowNumberUpdateDTO> updateCaseRowNumber(Long projectId, Long suitId,
                                                            List<CaseRowNumberUpdateDTO>
                                                                rowNumberUpdates) {
        Suit suit = suitService.getSuit(projectId, suitId);

        for(CaseRowNumberUpdateDTO dto : rowNumberUpdates) {
            Case caze = suit.getCaseById(dto.getId());
            if( caze == null ) {
                continue;
            }
            caze.setRowNumber(dto.getRowNumber());
            caseVersionDAO.save(caze);
        }

        suitVersionDAO.save(suit);

        return rowNumberUpdates;
    }

    private void throwExceptionIfCaseIsNotInSuit(Suit suit, Case caze) {
        if (!suit.hasCase(caze)) {
            throw new NotFoundException(
                String.format("Error: Suit %s does not have case %d", suit.getName(),
                    caze.getId()));
        }
    }
}