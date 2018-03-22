package com.epam.test_generator.services;

import static com.epam.test_generator.services.utils.UtilsService.checkNotNull;
import static com.epam.test_generator.services.utils.UtilsService.verifyVersion;

import com.epam.test_generator.dao.interfaces.StepSuggestionDAO;
import com.epam.test_generator.dto.StepSuggestionCreateDTO;
import com.epam.test_generator.dto.StepSuggestionDTO;
import com.epam.test_generator.dto.StepSuggestionUpdateDTO;
import com.epam.test_generator.entities.Step;
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
     * @param stepSuggestionCreateDTO
     * @return id of step suggestion
     */
    public Long addStepSuggestion(StepSuggestionCreateDTO stepSuggestionCreateDTO) {

        StepSuggestion stepSuggestion = new StepSuggestion(stepSuggestionCreateDTO.getContent(),
            stepSuggestionCreateDTO.getType(), 0L);
        stepSuggestion.setId(0L);
        stepSuggestion = stepSuggestionDAO
            .save(stepSuggestion);

        return stepSuggestion.getId();
    }

    /**
     * Updates step suggestion specified in stepSuggestionDTO by id
     * @param stepSuggestionId id of step suggestion to update
     * @param stepSuggestionUpdateDTO info to update
     */
    public void updateStepSuggestion(Long stepSuggestionId,
                                     StepSuggestionUpdateDTO stepSuggestionUpdateDTO) {
        StepSuggestion stepSuggestion = stepSuggestionDAO.findOne(stepSuggestionId);
        checkNotNull(stepSuggestion);
        verifyVersion(stepSuggestionUpdateDTO.getVersion(), stepSuggestion);
        if (stepSuggestionUpdateDTO.getContent() != null) {
            stepSuggestion.setContent(stepSuggestionUpdateDTO.getContent());
        }

        if (stepSuggestionUpdateDTO.getType() != null) {
            stepSuggestion.setType(stepSuggestionUpdateDTO.getType());
        }

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
