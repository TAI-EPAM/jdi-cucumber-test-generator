package com.epam.test_generator.services;

import static com.epam.test_generator.services.utils.UtilsService.checkNotNull;

import com.epam.test_generator.controllers.stepsuggestion.StepSuggestionTransformer;
import com.epam.test_generator.dao.interfaces.ProjectDAO;
import com.epam.test_generator.dao.interfaces.ProjectStepSuggestionDAO;
import com.epam.test_generator.controllers.stepsuggestion.request.StepSuggestionCreateDTO;
import com.epam.test_generator.controllers.stepsuggestion.response.StepSuggestionDTO;
import com.epam.test_generator.controllers.stepsuggestion.request.StepSuggestionUpdateDTO;
import com.epam.test_generator.entities.Project;
import com.epam.test_generator.entities.StepSuggestion;
import com.epam.test_generator.entities.StepType;
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
    private ProjectStepSuggestionDAO projectStepSuggestionDAO;

    @Autowired
    private ProjectDAO projectDAO;

    public List<StepSuggestionDTO> getStepsSuggestions(Long projectId) {
        Project project = checkNotNull(projectDAO.getOne(projectId));
        return stepSuggestionTransformer.toDtoList(project.getStepSuggestions());
    }

    public List<StepSuggestionDTO> getStepsSuggestions(Long projectId,StepType stepType, int pageNumber, int pageSize) {
        Project project = checkNotNull(projectDAO.getOne(projectId));

        Pageable request = PageRequest.of(pageNumber - 1, pageSize, Sort.Direction.ASC, "id");

        if (stepType == StepType.ANY) {
            return projectStepSuggestionDAO.findByProject(project, request).getContent().stream()
                .map(stepSuggestionTransformer::toDto)
                .collect(Collectors.toList());
        }
        return projectStepSuggestionDAO.findByProjectAndType(project, stepType, request).getContent().stream()
            .filter(s -> s.getType() == stepType)
            .map(stepSuggestionTransformer::toDto)
            .collect(Collectors.toList());
    }

    public StepSuggestionDTO getStepSuggestionDTO(Long projectId, Long stepSuggestionId) {
        return stepSuggestionTransformer.toDto(getStepSuggestion(projectId, stepSuggestionId));
    }

    /**
     * Find all step suggestions of particular type in particular project.
     * @param projectId id of a project
     * @param stepType type of step suggestion. If it is StepType.ANY, then step
     *                   suggestions of all types in given project are returned
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
    public Long addStepSuggestion(Long projectId, StepSuggestionCreateDTO stepSuggestionCreateDTO) {
        Project project = projectDAO.getOne(projectId);
        StepSuggestion stepSuggestion = stepSuggestionTransformer
            .fromDto(stepSuggestionCreateDTO);
        stepSuggestion = projectStepSuggestionDAO
            .save(stepSuggestion);
        project.addStepSuggestion(stepSuggestion);

        return stepSuggestion.getId();
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
        return stepSuggestionTransformer.toDto(
            projectStepSuggestionDAO.save(stepSuggestion));
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

        project.removeStepSuggestion(stepSuggestion);

        projectDAO.save(project);
    }

    private StepSuggestion getStepSuggestion(Long projectId, Long stepSuggestionId) {
        Project project = checkNotNull(projectDAO.getOne(projectId));
        StepSuggestion stepSuggestion = projectStepSuggestionDAO
            .getOne(stepSuggestionId);
        checkNotNull(stepSuggestion);
        if(!project.hasStepSuggestion(stepSuggestion)) {
            throw new BadRequestException(String.format(
                "Error: project %s does not have step suggestion '%s'",
                project.getName(),
                stepSuggestion.getContent()));
        }
        return stepSuggestion;
    }
}
