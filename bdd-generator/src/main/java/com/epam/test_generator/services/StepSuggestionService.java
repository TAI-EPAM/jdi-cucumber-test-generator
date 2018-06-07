package com.epam.test_generator.services;

import static com.epam.test_generator.services.utils.UtilsService.checkNotNull;

import com.epam.test_generator.controllers.stepsuggestion.StepSuggestionTransformer;
import com.epam.test_generator.controllers.stepsuggestion.request.StepSuggestionCreateDTO;
import com.epam.test_generator.controllers.stepsuggestion.request.StepSuggestionUpdateDTO;
import com.epam.test_generator.controllers.stepsuggestion.response.StepSuggestionDTO;
import com.epam.test_generator.dao.interfaces.ProjectDAO;
import com.epam.test_generator.dao.interfaces.StepSuggestionDAO;
import com.epam.test_generator.dao.interfaces.StepDAO;
import com.epam.test_generator.entities.Project;
import com.epam.test_generator.entities.Step;
import com.epam.test_generator.entities.StepSuggestion;
import com.epam.test_generator.entities.StepType;
import com.epam.test_generator.services.exceptions.AlreadyExistsException;
import com.epam.test_generator.services.exceptions.BadRequestException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@DependsOn("liquibase")
public class StepSuggestionService {

    @Autowired
    private StepSuggestionTransformer stepSuggestionTransformer;

    @Autowired
    private StepSuggestionDAO stepSuggestionDAO;

    @Autowired
    private ProjectDAO projectDAO;

    @Autowired
    private StepDAO stepDAO;

    @Autowired
    private StepService stepService;

    public StepSuggestion getStepSuggestion(Long projectId, Step step) {
        Project project = checkNotNull(projectDAO.getOne(projectId));

        for (StepSuggestion stepSuggestion : project.getStepSuggestions()) {
            if (compareStepToStepSuggestion(stepSuggestion, step)) {
                return stepSuggestion;
            }
        }
        StepSuggestionDTO stepSuggestionDTO = addStepSuggestion(projectId,
            new StepSuggestionCreateDTO(step.getDescription(), step.getType()));
        return stepSuggestionDAO.getOne(stepSuggestionDTO.getId());
    }

    public List<StepSuggestionDTO> getStepsSuggestions(Long projectId) {
        Project project = checkNotNull(projectDAO.getOne(projectId));
        return stepSuggestionTransformer.toDtoList(project.getStepSuggestions());
    }

    public List<StepSuggestionDTO> getStepsSuggestions(Long projectId, StepType stepType,
                                                       int pageNumber, int pageSize) {
        Project project = checkNotNull(projectDAO.getOne(projectId));

        Pageable request = PageRequest.of(pageNumber - 1, pageSize, Sort.Direction.ASC, "id");

        if (stepType == StepType.ANY) {
            return stepSuggestionDAO.findByProject(project, request).getContent().stream()
                .map(stepSuggestionTransformer::toDto)
                .collect(Collectors.toList());
        }
        return stepSuggestionDAO.findByProjectAndType(project, stepType, request)
            .getContent().stream()
            .filter(s -> s.getType() == stepType)
            .map(stepSuggestionTransformer::toDto)
            .collect(Collectors.toList());
    }

    public StepSuggestionDTO getStepSuggestionDTO(Long projectId, Long stepSuggestionId) {
        return stepSuggestionTransformer.toDto(getStepSuggestion(projectId, stepSuggestionId));
    }

    /**
     * Find all step suggestions of particular type in particular project.
     *
     * @param projectId id of a project
     * @param stepType type of step suggestion. If it is StepType.ANY, then step suggestions of all
     * types in given project are returned
     * @return list of {@link StepSuggestionDTO}
     */
    public List<StepSuggestionDTO> getStepsSuggestionsByType(Long projectId,
                                                             StepType stepType) {

        if (stepType == StepType.ANY) {
            return getStepsSuggestions(projectId);
        }

        Project project = checkNotNull(projectDAO.getOne(projectId));
        return project.getStepSuggestions().stream()
            .filter(s -> s.getType() == stepType)
            .map(stepSuggestionTransformer::toDto)
            .collect(Collectors.toList());
    }

    /**
     * Adds step suggestion specified in stepSuggestionDTO
     *
     * @return id of step suggestion
     */
    public StepSuggestionDTO addStepSuggestion(Long projectId,
                                               StepSuggestionCreateDTO stepSuggestionCreateDTO) {
        Project project = checkNotNull(projectDAO.getOne(projectId));
        StepSuggestion stepSuggestion = stepSuggestionTransformer
            .fromDto(stepSuggestionCreateDTO);

        checkStepSuggestionExistence(project, stepSuggestion);

        stepSuggestion = stepSuggestionDAO
            .save(stepSuggestion);
        project.addStepSuggestion(stepSuggestion);

        return stepSuggestionTransformer.toDto(stepSuggestion);
    }

    /**
     * Updates step suggestion specified in stepSuggestionDTO by id
     *
     * @param stepSuggestionId id of step suggestion to update
     * @param stepSuggestionUpdateDTO info to update
     */
    public StepSuggestionDTO updateStepSuggestion(Long projectId,
                                                  Long stepSuggestionId,
                                                  StepSuggestionUpdateDTO stepSuggestionUpdateDTO) {
        StepSuggestion stepSuggestion = getStepSuggestion(projectId, stepSuggestionId);
        checkNotNull(stepSuggestion);
        stepSuggestion.verifyVersion(stepSuggestionUpdateDTO.getVersion());
        stepSuggestionTransformer.updateFromDto(stepSuggestion, stepSuggestionUpdateDTO);
        stepSuggestion.getSteps().forEach(step -> {
            step.setDescription(stepSuggestion.getContent());
            step.setType(stepSuggestion.getType());
        });
        stepDAO.saveAll(stepSuggestion.getSteps());
        return stepSuggestionTransformer.toDto(
            stepSuggestionDAO.save(stepSuggestion));
    }

    /**
     * Deletes step suggestion from database by id
     *
     * @param stepSuggestionId id of step suggestion to delete
     */
    public void removeStepSuggestion(Long projectId, Long stepSuggestionId) {
        Project project = checkNotNull(projectDAO.getOne(projectId));

        StepSuggestion stepSuggestion =
            getStepSuggestion(projectId, stepSuggestionId);
        stepSuggestion.getSteps().forEach(step -> stepService.removeStep(projectId, step.getId()));

        project.removeStepSuggestion(stepSuggestion);

        projectDAO.save(project);
    }

    public void removeSteps(Long projectId, List<Step> steps) {
        steps.forEach(step -> {
            StepSuggestion stepSuggestion = getStepSuggestion(projectId, step);
            stepSuggestion.remove(step);
            stepSuggestionDAO.save(stepSuggestion);
        });
    }

    private boolean compareStepSuggestions(StepSuggestion stepSuggestion1,
                                           StepSuggestion stepSuggestion2) {
        return stepSuggestion1.getContent().equals(stepSuggestion2.getContent()) &&
            stepSuggestion1.getType().equals(stepSuggestion2.getType());
    }

    private boolean compareStepToStepSuggestion(StepSuggestion stepSuggestion, Step step) {
        return stepSuggestion.getContent().equals(step.getDescription()) &&
            stepSuggestion.getType().equals(step.getType());
    }

    private void checkStepSuggestionExistence(Project project, StepSuggestion stepSuggestion) {

        for (StepSuggestion stepSuggestionFromProject : project.getStepSuggestions()) {
            if (compareStepSuggestions(stepSuggestionFromProject, stepSuggestion)) {
                throw new AlreadyExistsException("Step suggestion with type = \""
                    + stepSuggestion.getType() + "\", description = \""
                    + stepSuggestion.getContent() + "\" already exists.");
            }
        }
    }

    private StepSuggestion getStepSuggestion(Long projectId, Long stepSuggestionId) {
        Project project = checkNotNull(projectDAO.getOne(projectId));
        StepSuggestion stepSuggestion = stepSuggestionDAO
            .getOne(stepSuggestionId);
        checkNotNull(stepSuggestion);
        if (!project.hasStepSuggestion(stepSuggestion)) {
            throw new BadRequestException(String.format(
                "Error: project %s does not have step suggestion '%s'",
                project.getName(),
                stepSuggestion.getContent()));
        }
        return stepSuggestion;
    }

    /**
     * Find project steps suggestions by string ignoring case and apply pagination
     *
     * @param searchString - string for search
     * @param pageNumber - number of page
     * @param pageSize - count of results
     * @return list with found steps suggestions
     */
    public List<StepSuggestionDTO> findStepsSuggestions(Long projectId, String searchString,
                                                        int pageNumber, int pageSize) {
        if (pageNumber < 1 || pageSize < 1) {
            throw new BadRequestException("Page number or page size must not be less than one!");
        }

        Pageable numberOfReturnedResults = PageRequest.of(pageNumber - 1, pageSize);
        List<StepSuggestion> foundStepsSuggestions = stepSuggestionDAO
            .findByProjectIdAndContentIgnoreCaseContaining(projectId, searchString,
                numberOfReturnedResults)
            .getContent();

        return stepSuggestionTransformer
            .toDtoList(foundStepsSuggestions);
    }


}
