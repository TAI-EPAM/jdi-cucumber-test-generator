package com.epam.test_generator.services;

import static com.epam.test_generator.services.utils.UtilsService.caseBelongsToSuit;
import static com.epam.test_generator.services.utils.UtilsService.checkNotNull;
import static com.epam.test_generator.services.utils.UtilsService.stepBelongsToCase;

import com.epam.test_generator.dao.interfaces.CaseVersionDAO;
import com.epam.test_generator.dao.interfaces.StepDAO;
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
    private CaseService caseService;

    @Autowired
    private SuitService suitService;

    @Autowired
    private CaseVersionDAO caseVersionDAO;

    @Autowired
    private StepTransformer stepTransformer;

    /**
     *
     * @param projectId id of project
     * @param suitId id of suit
     * @param caseId id of case where to get steps
     * @return List of all StepDTOs for specified case
     */
    public List<StepDTO> getStepsByCaseId(Long projectId, Long suitId, Long caseId) {
        Suit suit = suitService.getSuit(projectId, suitId);

        Case caze = caseService.getCase(projectId, suitId, caseId);

        caseBelongsToSuit(caze, suit);

        return stepTransformer.toDtoList(caze.getSteps());
    }

    /**
     *
     * @param projectId id of project
     * @param suitId id of suit
     * @param caseId id of case
     * @param stepId id of step
     * @return StepDTO of specified step
     */
    public StepDTO getStep(Long projectId, Long suitId, Long caseId, Long stepId) {
        Suit suit = suitService.getSuit(projectId, suitId);

        Case caze = caseService.getCase(projectId, suitId, caseId);

        caseBelongsToSuit(caze, suit);

        Step step = stepDAO.findOne(stepId);
        checkNotNull(step);

        stepBelongsToCase(step, caze);

        return stepTransformer.toDto(step);
    }

    /**
     * Adds step specified in StepDTO to case by id
     * @param projectId id of project
     * @param suitId id of suit
     * @param caseId id of case
     * @param stepDTO DTO where step specified
     * @return id of added step
     */
    public Long addStepToCase(Long projectId, Long suitId, Long caseId, StepDTO stepDTO) {
        Suit suit = suitService.getSuit(projectId, suitId);

        Case caze = caseService.getCase(projectId, suitId, caseId);

        caseBelongsToSuit(caze, suit);

        Step step = stepTransformer.fromDto(stepDTO);

        step = stepDAO.save(step);
        caze.getSteps().add(step);

        caseVersionDAO.save(caze);

        return step.getId();
    }

    /**
     * Updates step specified in StepDTO by id
     * @param projectId id of project
     * @param suitId id of suit
     * @param caseId id of case
     * @param stepDTO DTO where step specified
     * @param stepId id of step which to update
     */
    public void updateStep(Long projectId, Long suitId, Long caseId, Long stepId, StepDTO stepDTO) {
        Suit suit = suitService.getSuit(projectId, suitId);

        Case caze = caseService.getCase(projectId, suitId, caseId);

        caseBelongsToSuit(caze, suit);

        Step step = stepDAO.findOne(stepId);
        checkNotNull(step);

        stepBelongsToCase(step, caze);

        stepTransformer.mapDTOToEntity(stepDTO, step);

        stepDAO.save(step);

        caseVersionDAO.save(caze);
    }

    /**
     * Deletes step from case by id
     * @param projectId id of project
     * @param suitId id of suit
     * @param caseId id of case
     * @param stepId id of step to delete
     */
    public void removeStep(Long projectId, Long suitId, Long caseId, Long stepId) {
        Suit suit = suitService.getSuit(projectId, suitId);

        Case caze = caseService.getCase(projectId, suitId, caseId);

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
    public void cascadeUpdateSteps(Long projectId, long suitId, long caseId, List<StepDTO> steps) {
        for (StepDTO stepDTO : steps) {
            Action action = stepDTO.getAction();
            if (action != null) {
                switch (action) {
                    case UPDATE:
                        updateStep(projectId, suitId, caseId, stepDTO.getId(), stepDTO);
                        break;
                    case CREATE:
                        addStepToCase(projectId, suitId, caseId, stepDTO);
                        break;
                    case DELETE:
                        removeStep(projectId, suitId, caseId, stepDTO.getId());
                        break;
                    default:
                        // Do nothing
                        break;
                }
            }
        }
    }
}
