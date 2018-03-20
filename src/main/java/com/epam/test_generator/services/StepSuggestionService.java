package com.epam.test_generator.services;

import static com.epam.test_generator.services.utils.UtilsService.checkNotNull;

import com.epam.test_generator.dao.interfaces.StepSuggestionDAO;
import com.epam.test_generator.dto.StepSuggestionDTO;
import com.epam.test_generator.entities.StepSuggestion;
import com.epam.test_generator.entities.StepType;
import com.epam.test_generator.transformers.StepSuggestionTransformer;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
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

    public List<StepSuggestionDTO> getStepsSuggestions() {

        return stepSuggestionTransformer.toDtoList(stepSuggestionDAO.findAll());
    }

    public StepSuggestionDTO getStepsSuggestion(long stepSuggestionId) {
        StepSuggestion stepSuggestion = stepSuggestionDAO.findOne(stepSuggestionId);
        checkNotNull(stepSuggestion);

        return stepSuggestionTransformer.toDto(stepSuggestion);
    }

    public List<StepSuggestionDTO> getStepsSuggestionsByType(StepType stepType) {
        return stepSuggestionTransformer.toDtoList(
            stepSuggestionDAO.findAll().stream()
                .filter(s -> s.getType() == stepType)
                .collect(Collectors.toList()));
    }

    /**
     * Adds step suggestion specified in stepSuggestionDTO
     * @param stepSuggestionDTO
     * @return id of step suggestion
     */
    public Long addStepSuggestion(StepSuggestionDTO stepSuggestionDTO) {
        StepSuggestion stepSuggestion = stepSuggestionDAO
            .save(stepSuggestionTransformer.fromDto(stepSuggestionDTO));

        return stepSuggestion.getId();
    }

    /**
     * Updates step suggestion specified in stepSuggestionDTO by id
     * @param stepSuggestionId id of step suggestion to update
     * @param stepSuggestionDTO info to update
     */
    public void updateStepSuggestion(Long stepSuggestionId, StepSuggestionDTO stepSuggestionDTO) {
        StepSuggestion stepSuggestion = stepSuggestionDAO.findOne(stepSuggestionId);
        checkNotNull(stepSuggestion);
        stepSuggestionTransformer.mapDTOToEntity(stepSuggestionDTO, stepSuggestion);

        stepSuggestionDAO.save(stepSuggestion);
    }

    /**
     * Deletes step suggestion from database by id
     * @param stepSuggestionId id of step suggestion to delete
     */
    public void removeStepSuggestion(Long stepSuggestionId) {
        StepSuggestion stepSuggestion = stepSuggestionDAO.findOne(stepSuggestionId);
        checkNotNull(stepSuggestion);

        stepSuggestionDAO.delete(stepSuggestionId);
    }
}
