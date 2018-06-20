package com.epam.test_generator.services;

import static com.epam.test_generator.services.utils.UtilsService.checkNotNull;

import com.epam.test_generator.controllers.step.request.StepCreateDTO;
import com.epam.test_generator.controllers.step.request.StepUpdateDTO;
import com.epam.test_generator.dao.interfaces.CaseVersionDAO;
import com.epam.test_generator.dao.interfaces.StepSuggestionDAO;
import com.epam.test_generator.dao.interfaces.StepDAO;
import com.epam.test_generator.dao.interfaces.SuitVersionDAO;
import com.epam.test_generator.controllers.step.response.StepDTO;
import com.epam.test_generator.entities.Case;
import com.epam.test_generator.entities.Project;
import com.epam.test_generator.entities.Step;
import com.epam.test_generator.entities.StepSuggestion;
import com.epam.test_generator.entities.Suit;
import com.epam.test_generator.controllers.step.StepTransformer;
import com.epam.test_generator.services.exceptions.NotFoundException;
import java.util.List;
import java.util.Objects;
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
    private StepSuggestionService stepSuggestionService;

    @Autowired
    private CaseVersionDAO caseVersionDAO;

    @Autowired
    private SuitVersionDAO suitVersionDAO;

    @Autowired
    private StepSuggestionDAO stepSuggestionDAO;

    @Autowired
    private StepTransformer stepTransformer;

    @Autowired
    private ProjectService projectService;

    /**
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
     * @param projectId id of project
     * @param suitId id of suit
     * @param caseId id of case
     * @param stepId id of step
     * @return StepDTO of specified step
     */
    public StepDTO getStep(Long projectId, Long suitId, Long caseId, Long stepId) {
        Case caze = caseService.getCase(projectId, suitId, caseId);
        Step step = stepDAO.findById(stepId).orElseThrow(NotFoundException::new);

        throwExceptionIfStepIsNotInCase(caze, step);

        return stepTransformer.toDto(step);
    }

    /**
     * Adds step specified in StepCreateDTO to case by id
     *
     * @param projectId id of project
     * @param suitId id of suit
     * @param caseId id of case
     * @param stepCreateDTO DTO where step specified
     * @return id of added step
     */
    public StepDTO addStepToCase(Long projectId, Long suitId, Long caseId,
                                 StepCreateDTO stepCreateDTO) {
        Suit suit = suitService.getSuit(projectId, suitId);
        Case caze = caseService.getCase(projectId, suitId, caseId);

        Step step = stepTransformer.fromDto(stepCreateDTO);
        StepSuggestion stepSuggestion = stepSuggestionService.getStepSuggestion(projectId, step);
        stepSuggestion.add(step);
        caze.addStep(step);
        suit.updateStatus();

        step = stepDAO.save(step);
        caseVersionDAO.save(caze);
        suitVersionDAO.save(suit);

        return stepTransformer.toDto(step);
    }

    /**
     * Updates step specified in StepUpdateDTO by id
     *
     * @param projectId id of project
     * @param suitId id of suit
     * @param caseId id of case
     * @param stepUpdateDTO DTO where step specified
     * @param stepId id of step which to update
     */
    public StepDTO updateStep(Long projectId, Long suitId, Long caseId, Long stepId,
                              StepUpdateDTO stepUpdateDTO) {
        Suit suit = suitService.getSuit(projectId, suitId);
        Case caze = caseService.getCase(projectId, suitId, caseId);

        Step oldStep = stepDAO.findById(stepId).orElseThrow(NotFoundException::new);
        throwExceptionIfStepIsNotInCase(caze, oldStep);
        StepSuggestion oldStepSuggestion = stepSuggestionService.getStepSuggestion(projectId, oldStep);
        Step newStep = stepTransformer.updateFromDto(stepUpdateDTO, oldStep);
        StepSuggestion newStepSuggestion = stepSuggestionService.getStepSuggestion(projectId, newStep);
        stepDAO.save(newStep);
        if(!Objects.equals(oldStepSuggestion, newStepSuggestion)) {
            oldStepSuggestion.remove(oldStep);
            newStepSuggestion.add(newStep);
            stepSuggestionDAO.save(oldStepSuggestion);
            stepSuggestionDAO.save(newStepSuggestion);
        }
        caseVersionDAO.save(caze);
        suitVersionDAO.save(suit);

        return stepTransformer.toDto(newStep);
    }

    /**
     * Deletes step from case by id
     *
     * @param projectId id of project
     * @param suitId id of suit
     * @param caseId id of case
     * @param stepId id of step to delete
     */
    public void removeStep(Long projectId, Long suitId, Long caseId, Long stepId) {
        Suit suit = suitService.getSuit(projectId, suitId);
        Case caze = caseService.getCase(projectId, suitId, caseId);

        Step step = stepDAO.findById(stepId).orElseThrow(NotFoundException::new);
        throwExceptionIfStepIsNotInCase(caze, step);

        StepSuggestion stepSuggestion = stepSuggestionService.getStepSuggestion(projectId, step);

        stepSuggestion.remove(step);
        caze.removeStep(step);
        suit.updateStatus();

        stepDAO.delete(step);
        caseVersionDAO.save(caze);
        suitVersionDAO.save(suit);
    }

    public void removeStep(Long projectId, Long stepId) {
        Project project = checkNotNull(projectService.getProjectByProjectId(projectId));

        for (Suit suit: project.getSuits()) {
            for (Case caze: suit.getCases()) {
                for (Step step: caze.getSteps()) {
                    if(stepId.equals(step.getId())){
                        caze.removeStep(step);
                        suit.updateStatus();
                        stepDAO.delete(step);
                        caseVersionDAO.save(caze);
                        suitVersionDAO.save(suit);
                        return;
                    }
                }
            }
        }

    }

    private void throwExceptionIfStepIsNotInCase(Case aCase, Step step) {
        if (!aCase.hasStep(step)) {
            throw new NotFoundException(
                String.format("Error: Case %s does not have step %d", aCase.getName(),
                    step.getId()));
        }
    }
}
