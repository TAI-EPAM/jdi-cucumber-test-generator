package com.epam.test_generator.services;

import static com.epam.test_generator.services.utils.UtilsService.caseBelongsToSuit;
import static com.epam.test_generator.services.utils.UtilsService.checkNotNull;
import static com.epam.test_generator.services.utils.UtilsService.stepBelongsToCase;

import com.epam.test_generator.dao.interfaces.CaseDAO;
import com.epam.test_generator.dao.interfaces.CaseVersionDAO;
import com.epam.test_generator.dao.interfaces.StepDAO;
import com.epam.test_generator.dao.interfaces.SuitDAO;
import com.epam.test_generator.dto.StepDTO;
import com.epam.test_generator.entities.Action;
import com.epam.test_generator.entities.Case;
import com.epam.test_generator.entities.Step;
import com.epam.test_generator.entities.Suit;
import com.epam.test_generator.transformers.StepTransformer;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class StepService {

    @Autowired
    private StepDAO stepDAO;

    @Autowired
    private CaseDAO caseDAO;

    @Autowired
    private SuitDAO suitDAO;

    @Autowired
    private CaseVersionDAO caseVersionDAO;

    @Autowired
    private StepTransformer stepTransformer;

    public List<StepDTO> getStepsByCaseId(Long suitId, Long caseId) {
        Suit suit = suitDAO.findOne(suitId);
        checkNotNull(suit);

        Case caze = caseDAO.findOne(caseId);
        checkNotNull(caze);

        caseBelongsToSuit(caze, suit);

        return stepTransformer.toDtoList(caze.getSteps());
    }

    public StepDTO getStep(Long suitId, Long caseId, Long stepId) {
        Suit suit = suitDAO.findOne(suitId);
        checkNotNull(suit);

        Case caze = caseDAO.findOne(caseId);
        checkNotNull(caze);

        caseBelongsToSuit(caze, suit);

        Step step = stepDAO.findOne(stepId);
        checkNotNull(step);

        stepBelongsToCase(step, caze);

        return stepTransformer.toDto(step);
    }

    public Long addStepToCase(Long suitId, Long caseId, StepDTO stepDTO) {
        Suit suit = suitDAO.findOne(suitId);
        checkNotNull(suit);

        Case caze = caseDAO.findOne(caseId);
        checkNotNull(caze);

        caseBelongsToSuit(caze, suit);

        Step step = stepTransformer.fromDto(stepDTO);

        step = stepDAO.save(step);
        caze.getSteps().add(step);

        caseVersionDAO.save(caze);

        return step.getId();
    }

    public void updateStep(Long suitId, Long caseId, Long stepId, StepDTO stepDTO) {
        Suit suit = suitDAO.findOne(suitId);
        checkNotNull(suit);

        Case caze = caseDAO.findOne(caseId);
        checkNotNull(caze);

        caseBelongsToSuit(caze, suit);

        Step step = stepDAO.findOne(stepId);
        checkNotNull(step);

        stepBelongsToCase(step, caze);

        stepTransformer.mapDTOToEntity(stepDTO, step);

        stepDAO.save(step);

        caseVersionDAO.save(caze);
    }

    public void removeStep(Long suitId, Long caseId, Long stepId) {
        Suit suit = suitDAO.findOne(suitId);
        checkNotNull(suit);

        Case caze = caseDAO.findOne(caseId);
        checkNotNull(caze);

        caseBelongsToSuit(caze, suit);

        Step step = stepDAO.findOne(stepId);
        checkNotNull(step);

        stepBelongsToCase(step, caze);

        caze.getSteps().remove(step);
        stepDAO.delete(stepId);

        caseVersionDAO.save(caze);
    }

    /**
     * This service method is specialized for 'adding' 'editing' and 'deleting' a list of steps
     *
     * For selecting required action uses "action" field from DTO object
     *
     * @param steps array list of steps
     */
    public void cascadeUpdateSteps(long suitId, long caseId, List<StepDTO> steps) {
        for (StepDTO stepDTO : steps) {
            Action action = stepDTO.getAction();
            if (action != null) {
                switch (action) {
                    case UPDATE:
                        updateStep(suitId, caseId, stepDTO.getId(), stepDTO);
                        break;
                    case CREATE:
                        addStepToCase(suitId, caseId, stepDTO);
                        break;
                    case DELETE:
                        removeStep(suitId, caseId, stepDTO.getId());
                        break;
                    default:
                        // Do nothing
                        break;
                }
            }
        }
    }
}
