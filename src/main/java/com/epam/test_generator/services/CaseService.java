package com.epam.test_generator.services;

import static com.epam.test_generator.services.utils.UtilsService.caseBelongsToSuit;
import static com.epam.test_generator.services.utils.UtilsService.checkNotNull;

import com.epam.test_generator.dao.interfaces.CaseDAO;
import com.epam.test_generator.dao.interfaces.CaseVersionDAO;
import com.epam.test_generator.dao.interfaces.SuitDAO;
import com.epam.test_generator.dao.interfaces.TagDAO;
import com.epam.test_generator.dto.CaseDTO;
import com.epam.test_generator.dto.EditCaseDTO;
import com.epam.test_generator.entities.Case;
import com.epam.test_generator.entities.Event;
import com.epam.test_generator.entities.Status;
import com.epam.test_generator.entities.Suit;
import com.epam.test_generator.services.exceptions.BadRequestException;
import com.epam.test_generator.state.machine.StateMachineAdapter;
import com.epam.test_generator.transformers.CaseTransformer;
import com.epam.test_generator.transformers.SuitTransformer;
import java.util.Calendar;
import java.util.List;
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
    private SuitTransformer suitTransformer;

    @Autowired
    private CaseDAO caseDAO;

    @Autowired
    private SuitDAO suitDAO;

    @Autowired
    private TagDAO tagDAO;

    @Autowired
    private CaseVersionDAO caseVersionDAO;

    @Autowired
    private StateMachineAdapter stateMachineAdapter;

    public CaseDTO getCase(Long suitId, Long caseId) {
        Suit suit = suitDAO.findOne(suitId);
        checkNotNull(suit);

        Case caze = caseDAO.findOne(caseId);
        checkNotNull(caze);

        caseBelongsToSuit(caze, suit);

        return caseTransformer.toDto(caze);
    }

    public Long addCaseToSuit(Long suitId, CaseDTO caseDTO) {
        Suit suit = suitDAO.findOne(suitId);
        checkNotNull(suit);

        Case caze = caseTransformer.fromDto(caseDTO);

        caze = caseDAO.save(caze);
        suit.getCases().add(caze);

        caseVersionDAO.save(caze);

        return caze.getId();
    }

    public void updateCase(Long suitId, Long caseId, EditCaseDTO editCaseDTO) {
        Suit suit = suitDAO.findOne(suitId);
        checkNotNull(suit);

        Case caze = caseDAO.findOne(caseId);
        checkNotNull(caze);

        caseBelongsToSuit(caze, suit);

        caze.setUpdateDate(Calendar.getInstance().getTime());

        if (editCaseDTO.getDescription() != null) {
            caze.setDescription(editCaseDTO.getDescription());
        }

        if (editCaseDTO.getPriority() != null) {
            caze.setPriority(editCaseDTO.getPriority());
        }

        caseDAO.save(caze);

        caseVersionDAO.save(caze);
    }

    public void removeCase(Long suitId, Long caseId) {
        Suit suit = suitDAO.findOne(suitId);
        checkNotNull(suit);

        Case caze = caseDAO.findOne(caseId);
        checkNotNull(caze);

        caseBelongsToSuit(caze, suit);

        suit.getCases().remove(caze);
        caseDAO.delete(caseId);

        caseVersionDAO.delete(caze);
    }

    public void removeCases(Long suitId, List<Long> caseIds) {
        Suit suit = suitDAO.findOne(suitId);
        checkNotNull(suit);

        suit.getCases().stream()
            .filter(caze -> caseIds.stream()
                .anyMatch(id -> id.equals(caze.getId())))
            .forEach(caze -> {
                caseDAO.delete(caze.getId());
                caseVersionDAO.delete(caze);
            });
    }

    public Status performEvent(Long suitId, Long caseId, Event event) throws Exception {
        Case cs = caseTransformer.fromDto(getCase(suitId, caseId));
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