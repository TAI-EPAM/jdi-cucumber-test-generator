package com.epam.test_generator.services;

import static com.epam.test_generator.services.utils.UtilsService.checkNotNull;

import com.epam.test_generator.dao.interfaces.CaseDAO;
import com.epam.test_generator.dao.interfaces.CaseVersionDAO;
import com.epam.test_generator.dao.interfaces.RemovedIssueDAO;
import com.epam.test_generator.dao.interfaces.SuitVersionDAO;
import com.epam.test_generator.dto.CaseDTO;
import com.epam.test_generator.dto.CaseUpdateDTO;
import com.epam.test_generator.dto.CaseVersionDTO;
import com.epam.test_generator.dto.EditCaseDTO;
import com.epam.test_generator.dto.StepDTO;
import com.epam.test_generator.dto.SuitUpdateDTO;
import com.epam.test_generator.entities.Case;
import com.epam.test_generator.entities.Event;
import com.epam.test_generator.entities.RemovedIssue;
import com.epam.test_generator.entities.Status;
import com.epam.test_generator.entities.Suit;
import com.epam.test_generator.pojo.CaseVersion;
import com.epam.test_generator.services.exceptions.BadRequestException;
import com.epam.test_generator.state.machine.StateMachineAdapter;
import com.epam.test_generator.transformers.CaseTransformer;
import com.epam.test_generator.transformers.CaseVersionTransformer;
import com.epam.test_generator.transformers.TagTransformer;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.MethodArgumentNotValidException;

@Transactional
@Service
public class CaseService {

    @Autowired
    private CaseTransformer caseTransformer;

    @Autowired
    private Validator validator;

    @Autowired
    private CaseVersionTransformer caseVersionTransformer;

    @Autowired
    private CaseDAO caseDAO;

    @Autowired
    private SuitService suitService;

    @Autowired
    private CascadeUpdateService cascadeUpdateService;

    @Autowired
    private CaseVersionDAO caseVersionDAO;

    @Autowired
    private SuitVersionDAO suitVersionDAO;

    @Autowired
    private StateMachineAdapter stateMachineAdapter;

    @Autowired
    private TagTransformer tagTransformer;

    @Autowired
    private RemovedIssueDAO removedIssueDAO;



    public List<Case> getCases() {
        return caseDAO.findAll();
    }

    public Case getCase(Long projectId, Long suitId, Long caseId) {
        Suit suit = suitService.getSuit(projectId, suitId);
        Case caze = checkNotNull(caseDAO.findOne(caseId));
        if (suit.hasCase(caze)) {
            return caze;
        } else {
            throw new BadRequestException(
                String.format("Error: suit %s does not have case %s", suit.getName(),
                    caze.getName()));
        }
    }

    public CaseDTO getCaseDTO(Long projectId, Long suitId, Long caseId) {
        return caseTransformer.toDto(getCase(projectId, suitId, caseId));
    }

    /**
     * Adds case to existing suit
     *
     * @param projectId id of project where to add case
     * @param suitId    id of suit where to add case
     * @param caseDTO   case to add
     * @return {@link CaseDTO} of added case to suit
     */
    public CaseDTO addCaseToSuit(Long projectId, Long suitId, @Valid CaseDTO caseDTO) {
        Suit suit = suitService.getSuit(projectId, suitId);

        caseDTO.setJiraParentKey(suit.getJiraKey());
        caseDTO.setJiraProjectKey(suit.getJiraProjectKey());

        Case caze = caseTransformer.fromDto(caseDTO);

        Date currentTime = Calendar.getInstance().getTime();

        caze.setCreationDate(currentTime);
        caze.setUpdateDate(currentTime);
        caze.setLastModifiedDate(LocalDateTime.now());
        caze = caseDAO.save(caze);

        suit.addCase(caze);

        caseVersionDAO.save(caze);
        suitVersionDAO.save(suit);

        return caseTransformer.toDto(caze);
    }


    /**
     * Adds case to existing suit using editCaseDTO
     * @param projectId id of project where to add case
     * @param suitId id of suit where to add case
     * @param editCaseDTO case to add
     * @return {@link CaseDTO} of added case to suit
     */
    public CaseDTO addCaseToSuit(Long projectId, Long suitId, EditCaseDTO editCaseDTO)
        throws MethodArgumentNotValidException {
        CaseDTO caseDTO = new CaseDTO(editCaseDTO.getId(), editCaseDTO.getName(),
            editCaseDTO.getDescription(), new ArrayList<>(),
            editCaseDTO.getPriority(), new HashSet<>(), editCaseDTO.getStatus(), editCaseDTO.getComment());

        BeanPropertyBindingResult beanPropertyBindingResult =
            new BeanPropertyBindingResult(caseDTO, CaseDTO.class.getSimpleName());

        validator.validate(caseDTO, beanPropertyBindingResult);
        if (beanPropertyBindingResult.hasErrors()) {
            throw new MethodArgumentNotValidException(null, beanPropertyBindingResult);
        }
        return addCaseToSuit(projectId, suitId, caseDTO);
    }

    /**
     * Updates case info to info specified in editCaseDTO
     * @param projectId id of project where to update case
     * @param suitId id of suit where to update case
     * @param caseId id of case which to update
     * @param editCaseDTO info to update
     * @return {@link SuitUpdateDTO} which contains {@link CaseDTO} and {@link List<Long>}
     * (in fact id of {@link StepDTO} with FAILED {@link Status} which belong this suit)
     */
    public CaseUpdateDTO updateCase(Long projectId, Long suitId, Long caseId, EditCaseDTO editCaseDTO) {
        Suit suit = suitService.getSuit(projectId, suitId);

        Case caze = getCase(projectId, suitId, caseId);
        List<Long> failedStepIds = cascadeUpdateService
            .cascadeCaseStepsUpdate(projectId, suitId, caseId, editCaseDTO);
        caze.setUpdateDate(Calendar.getInstance().getTime());
        if (editCaseDTO.getTags() != null) {
            caze.setTags(new HashSet<>(tagTransformer.fromDtoList(editCaseDTO.getTags())));
        }
        caze.setDescription(editCaseDTO.getDescription());
        caze.setPriority(editCaseDTO.getPriority());
        caze.setStatus(editCaseDTO.getStatus());
        caze.setName(editCaseDTO.getName());
        caze.setLastModifiedDate(LocalDateTime.now());

        caze = caseDAO.save(caze);

        CaseDTO updatedCaseDTO = caseTransformer.toDto(caze);
        CaseUpdateDTO updatedCaseDTOwithFailedStepIds = new CaseUpdateDTO(updatedCaseDTO,
            failedStepIds);

        caseVersionDAO.save(caze);
        suitVersionDAO.save(suit);
        return updatedCaseDTOwithFailedStepIds;
    }

    /**
     * Deletes one case by id
     * @param projectId id of project where to delete case
     * @param suitId id of suit where to delete case
     * @param caseId id of case to delete
     * @return removed {@link CaseDTO}
     */
    public CaseDTO removeCase(Long projectId, Long suitId, Long caseId) {
        Suit suit = suitService.getSuit(projectId, suitId);

        Case caze = getCase(projectId, suitId, caseId);

        suit.removeCase(caze);

        saveIssueToDeleteInJira(caze);

        caseDAO.delete(caseId);

        caseVersionDAO.delete(caze);
        suitVersionDAO.save(suit);

        return caseTransformer.toDto(caze);
    }

    /**
     * Deletes multiple cases by ids
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
                caseDAO.delete(caze.getId());

                caseVersionDAO.delete(caze);

                removedCases.add(caze);
                saveIssueToDeleteInJira(caze);

            });

        removedCases.forEach(suit::removeCase);
        suitVersionDAO.save(suit);

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

        return caseVersionTransformer.toDtoList(caseVersions);
    }

    /**
     * Restores case to previous version by caseId and commitId
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

    /**
     * Updates all cases to specified in list of editCaseDTOS
     * @param projectId id of project where to update cases
     * @param suitId id of suit where to update cases
     * @param editCaseDTOS list of cases to update
     * @return list {@link CaseDTO} with all changed cases
     * @throws MethodArgumentNotValidException
     */
    public List<CaseDTO> updateCases(Long projectId, long suitId, List<EditCaseDTO> editCaseDTOS)
        throws MethodArgumentNotValidException {
        List<CaseDTO> updatedCases = new ArrayList<>();
        for (EditCaseDTO caseDTO : editCaseDTOS) {
            switch (caseDTO.getAction()) {
                case DELETE:
                    if (caseDTO.getId() == null) {
                        throw new BadRequestException("No id in Case to remove");
                    }
                    updatedCases.add(removeCase(projectId, suitId, caseDTO.getId()));
                    break;
                case CREATE:
                    updatedCases.add(addCaseToSuit(projectId, suitId, caseDTO));
                    break;
                case UPDATE:
                    if (caseDTO.getId() == null) {
                        throw new BadRequestException("No id in Case to update");
                    }
                    CaseDTO updatedCaseDTO = updateCase(projectId, suitId, caseDTO.getId(), caseDTO)
                        .getUpdatedCaseDto();
                    updatedCases.add(updatedCaseDTO);
                    break;
                default:
                    throw new BadRequestException("Wrong action argument");
            }
        }

        return updatedCases;
    }

    public Status performEvent(Long projectId, Long suitId, Long caseId, Event event)
        throws Exception {
        Case cs = getCase(projectId, suitId, caseId);
        StateMachine<Status, Event> stateMachine = stateMachineAdapter.restore(cs);
        if (stateMachine.sendEvent(event)) {
            stateMachineAdapter.persist(stateMachine, cs);
            caseDAO.save(cs);

            caseVersionDAO.save(cs);
            suitVersionDAO.save(suitService.getSuit(projectId, suitId));

            return cs.getStatus();
        } else {
            throw new BadRequestException();
        }
    }
}