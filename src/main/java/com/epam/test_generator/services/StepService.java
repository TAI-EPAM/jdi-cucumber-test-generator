package com.epam.test_generator.services;

import static com.epam.test_generator.services.utils.UtilsService.checkNotNull;

import com.epam.test_generator.dao.interfaces.CaseVersionDAO;
import com.epam.test_generator.dao.interfaces.StepDAO;
import com.epam.test_generator.dao.interfaces.SuitVersionDAO;
import com.epam.test_generator.dto.StepDTO;
import com.epam.test_generator.entities.Action;
import com.epam.test_generator.entities.Case;
import com.epam.test_generator.entities.Step;
import com.epam.test_generator.entities.Suit;
import com.epam.test_generator.services.exceptions.BadRequestException;
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
    private SuitVersionDAO suitVersionDAO;

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
        Case caze = caseService.getCase(projectId, suitId, caseId);
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
        Case caze = caseService.getCase(projectId, suitId, caseId);
        Step step = checkNotNull(stepDAO.findOne(stepId));

        throwExceptionIfStepIsNotInCase(caze, step);

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

        Step step = stepTransformer.fromDto(stepDTO);

        step = stepDAO.save(step);
        caze.addStep(step);

        caseVersionDAO.save(caze);
        suitVersionDAO.save(suit);

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

        Step step = checkNotNull(stepDAO.findOne(stepId));
        throwExceptionIfStepIsNotInCase(caze, step);

        stepTransformer.mapDTOToEntity(stepDTO, step);
        stepDAO.save(step);

        caseVersionDAO.save(caze);
        suitVersionDAO.save(suit);
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

        Step step = checkNotNull(stepDAO.findOne(stepId));
        throwExceptionIfStepIsNotInCase(caze, step);
        caze.removeStep(step);
        stepDAO.delete(stepId);

        caseVersionDAO.save(caze);
        suitVersionDAO.save(suit);
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

    private void throwExceptionIfStepIsNotInCase(Case aCase, Step step) {
        if (!aCase.hasStep(step)) {
            throw new BadRequestException(
                String.format("Error: Case %s does not have step %s", aCase.getName(),
                step.getId()));
        }
    }

}
