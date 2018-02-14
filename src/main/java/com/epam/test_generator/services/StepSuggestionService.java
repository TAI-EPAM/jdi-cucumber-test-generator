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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class StepSuggestionService {

    List<StepSuggestionDTO> allSuggestionSteps;
    @Autowired
    private StepSuggestionTransformer stepSuggestionTransformer;
    @Autowired
    private StepSuggestionDAO stepSuggestionDAO;
    @Value("#{'${suggestions.given}'.split(',')}")
    private List<String> given;
    @Value("#{'${suggestions.when}'.split(',')}")
    private List<String> when;
    @Value("#{'${suggestions.then}'.split(',')}")
    private List<String> then;
    @Value("#{'${suggestions.and}'.split(',')}")
    private List<String> and;

    @PostConstruct
    private void initializeDB() {
        allSuggestionSteps = getStepsSuggestions();
        loadDefaultStepSuggestions(given, StepType.GIVEN);
        loadDefaultStepSuggestions(when, StepType.WHEN);
        loadDefaultStepSuggestions(then, StepType.THEN);
        loadDefaultStepSuggestions(and, StepType.AND);
    }


    /**
     * Sets step types to stepSuggestions in database. Input List of steps filters by type and type sets
     * to chosen StepType only. Method uses to initialize database.
     * @param steps List of steps
     * @param type type of steps to filter and appoint
     */
    private void loadDefaultStepSuggestions(List<String> steps, StepType type) {
        List<StepSuggestionDTO> givenSuggestions = allSuggestionSteps.stream()
            .filter(c -> new Integer(type.ordinal()).equals(c.getType()))
            .collect(Collectors.toList());
        for (String s : steps) {
            if (givenSuggestions.stream().map(StepSuggestionDTO::getContent).noneMatch(s::equals)) {
                stepSuggestionDAO.save(new StepSuggestion(s, type));
            }
        }
    }

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
