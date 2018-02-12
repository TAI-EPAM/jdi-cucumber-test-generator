package com.epam.test_generator.services;

import static com.epam.test_generator.services.utils.UtilsService.caseBelongsToSuit;
import static com.epam.test_generator.services.utils.UtilsService.checkNotNull;

import com.epam.test_generator.dao.interfaces.CaseDAO;
import com.epam.test_generator.dao.interfaces.CaseVersionDAO;
import com.epam.test_generator.dto.CaseDTO;
import com.epam.test_generator.dto.CaseVersionDTO;
import com.epam.test_generator.dto.EditCaseDTO;
import com.epam.test_generator.dto.PropertyDifferenceDTO;
import com.epam.test_generator.entities.Case;
import com.epam.test_generator.entities.Event;
import com.epam.test_generator.entities.Status;
import com.epam.test_generator.entities.Suit;
import com.epam.test_generator.pojo.CaseVersion;
import com.epam.test_generator.services.exceptions.BadRequestException;
import com.epam.test_generator.state.machine.StateMachineAdapter;
import com.epam.test_generator.transformers.CaseTransformer;
import com.epam.test_generator.transformers.CaseVersionTransformer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
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
    private StateMachineAdapter stateMachineAdapter;

    public Case getCase(Long projectId, Long suitId, Long caseId) {
        Suit suit = suitService.getSuit(projectId, suitId);

        Case caze = caseDAO.findOne(caseId);
        checkNotNull(caze);

        caseBelongsToSuit(caze, suit);

        return caze;
    }

    public CaseDTO getCaseDTO(Long projectId, Long suitId, Long caseId) {
        return caseTransformer.toDto(getCase(projectId, suitId, caseId));
    }

    public Long addCaseToSuit(Long projectId, Long suitId, @Valid CaseDTO caseDTO) {
        Suit suit = suitService.getSuit(projectId, suitId);

        Case caze = caseTransformer.fromDto(caseDTO);
        Date currentTime = Calendar.getInstance().getTime();

        caze.setCreationDate(currentTime);
        caze.setUpdateDate(currentTime);

        caze = caseDAO.save(caze);
        suit.getCases().add(caze);

        caseVersionDAO.save(caze);

        return caze.getId();
    }

    public Long addCaseToSuit(Long projectId, Long suitId, EditCaseDTO editCaseDTO)
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

    public List<Long> updateCase(Long projectId, Long suitId, Long caseId, EditCaseDTO editCaseDTO) {
        Suit suit = suitService.getSuit(projectId, suitId);

        Case caze = caseDAO.findOne(caseId);
        checkNotNull(caze);

        caseBelongsToSuit(caze, suit);

        final List<Long> failedStepIds = cascadeUpdateService
            .cascadeCaseStepsUpdate(projectId, suitId, caseId, editCaseDTO);

        caze.setUpdateDate(Calendar.getInstance().getTime());

        caze.setDescription(editCaseDTO.getDescription());
        caze.setPriority(editCaseDTO.getPriority());
        caze.setStatus(editCaseDTO.getStatus());
        caze.setName(editCaseDTO.getName());

        caseDAO.save(caze);
        caseVersionDAO.save(caze);
        return failedStepIds;
    }

    public void removeCase(Long projectId, Long suitId, Long caseId) {
        Suit suit = suitService.getSuit(projectId, suitId);

        Case caze = caseDAO.findOne(caseId);
        checkNotNull(caze);

        caseBelongsToSuit(caze, suit);

        suit.getCases().remove(caze);
        caseDAO.delete(caseId);

        caseVersionDAO.delete(caze);
    }

    public void removeCases(Long projectId, Long suitId, List<Long> caseIds) {
        Suit suit = suitService.getSuit(projectId, suitId);

        suit.getCases().stream()
            .filter(caze -> caseIds.stream()
                .anyMatch(id -> id.equals(caze.getId())))
            .forEach(caze -> {
                caseDAO.delete(caze.getId());
                caseVersionDAO.delete(caze);
            });
    }

    public List<CaseVersionDTO> getCaseVersions(Long projectId, Long suitId, Long caseId) {
        Suit suit = suitService.getSuit(projectId, suitId);

        Case caze = caseDAO.findOne(caseId);
        checkNotNull(caze);

        caseBelongsToSuit(caze, suit);

        List<CaseVersion> caseVersions = caseVersionDAO.findAll(caseId);

        return caseVersionTransformer.toDtoList(caseVersions);
    }

    public void restoreCase(Long projectId, Long suitId, Long caseId, String commitId) {
        Suit suit = suitService.getSuit(projectId, suitId);

        Case caze = caseDAO.findOne(caseId);
        checkNotNull(caze);

        caseBelongsToSuit(caze, suit);

        Case caseToRestore = caseVersionDAO.findByCommitId(caseId, commitId);
        checkNotNull(caseToRestore);

        caseDAO.save(caseToRestore);
        caseVersionDAO.save(caseToRestore);
    }

    public List<Long> updateCases(Long projectId, long suitId, List<EditCaseDTO> editCaseDTOS)
        throws MethodArgumentNotValidException {
        List<Long> newCasesIds = new ArrayList<>();
        for (EditCaseDTO caseDTO : editCaseDTOS) {
            switch (caseDTO.getAction()) {
                case DELETE:
                    if (caseDTO.getId() == null) {
                        throw new BadRequestException("No id in Case to remove");
                    }
                    removeCase(projectId, suitId, caseDTO.getId());
                    break;
                case CREATE:
                    newCasesIds.add(addCaseToSuit(projectId, suitId, caseDTO));
                    break;
                case UPDATE:
                    if (caseDTO.getId() == null) {
                        throw new BadRequestException("No id in Case to update");
                    }
                    updateCase(projectId, suitId, caseDTO.getId(), caseDTO);
                    break;
                default:
                    throw new BadRequestException("Wrong action argument");
            }
        }
        return newCasesIds;
    }

    public Status performEvent(Long projectId, Long suitId, Long caseId, Event event)
        throws Exception {
        Case cs = getCase(projectId, suitId, caseId);
        StateMachine<Status, Event> stateMachine = stateMachineAdapter.restore(cs);
        if (stateMachine.sendEvent(event)) {
            stateMachineAdapter.persist(stateMachine, cs);
            caseDAO.save(cs);
            caseVersionDAO.save(cs);
            return cs.getStatus();
        } else {
            throw new BadRequestException();
        }
    }
}