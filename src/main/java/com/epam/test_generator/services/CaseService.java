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
import com.epam.test_generator.transformers.StepTransformer;
import com.epam.test_generator.transformers.SuitTransformer;
import com.epam.test_generator.transformers.TagTransformer;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;

@Transactional
@Service
public class CaseService {

    @Autowired
    private CaseTransformer caseTransformer;

    @Autowired
    private SuitTransformer suitTransformer;

    @Autowired
    private TagTransformer tagTransformer;

    @Autowired
    private StepTransformer stepTransformer;

    @Autowired
    private Validator validator;

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

    public Long addCaseToSuit(Long suitId, @Valid CaseDTO caseDTO) {
        Suit suit = suitDAO.findOne(suitId);
        checkNotNull(suit);

        Case caze = caseTransformer.fromDto(caseDTO);

        Date currentTime = Calendar.getInstance().getTime();

        caze.setCreationDate(currentTime);
        caze.setUpdateDate(currentTime);

        caze = caseDAO.save(caze);
        suit.getCases().add(caze);

        caseVersionDAO.save(caze);

        return caze.getId();
    }

    public Long addCaseToSuit(Long suitId, EditCaseDTO editCaseDTO)
        throws MethodArgumentNotValidException {
        CaseDTO caseDTO = new CaseDTO(editCaseDTO.getId(), editCaseDTO.getName(),
            editCaseDTO.getDescription(), new ArrayList<>(),
            editCaseDTO.getPriority(), new HashSet<>(), editCaseDTO.getStatus());

        BeanPropertyBindingResult beanPropertyBindingResult =
            new BeanPropertyBindingResult(caseDTO, CaseDTO.class.getSimpleName());

        validator.validate(caseDTO, beanPropertyBindingResult);
        if (beanPropertyBindingResult.hasErrors()) {
            throw new MethodArgumentNotValidException(null, beanPropertyBindingResult);
        }
        return addCaseToSuit(suitId, caseDTO);
    }

    public void updateCase(Long suitId, Long caseId, EditCaseDTO editCaseDTO) {
        Suit suit = suitDAO.findOne(suitId);
        checkNotNull(suit);

        Case caze = caseDAO.findOne(caseId);
        checkNotNull(caze);

        caseBelongsToSuit(caze, suit);

        caze.setUpdateDate(Calendar.getInstance().getTime());

        caze.setDescription(editCaseDTO.getDescription());
        caze.setPriority(editCaseDTO.getPriority());
        caze.setStatus(editCaseDTO.getStatus());
        caze.setName(editCaseDTO.getName());

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

    public List<Long> updateCases(long suitId, List<EditCaseDTO> editCaseDTOS)
        throws MethodArgumentNotValidException {
        List<Long> newCasesIds = new ArrayList<>();
        for (EditCaseDTO caseDTO : editCaseDTOS) {
            switch (caseDTO.getAction()) {
                case DELETE:
                    if (caseDTO.getId() == null) {
                        throw new BadRequestException("No id in Case to remove");
                    }
                    removeCase(suitId, caseDTO.getId());
                    break;
                case CREATE:
                    newCasesIds.add(addCaseToSuit(suitId, caseDTO));
                    break;
                case UPDATE:
                    if (caseDTO.getId() == null) {
                        throw new BadRequestException("No id in Case to update");
                    }
                    updateCase(suitId, caseDTO.getId(), caseDTO);
                    break;
                default:
                    throw new BadRequestException("Wrong action argument");
            }
        }
        return newCasesIds;
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