package com.epam.test_generator.services;

import com.epam.test_generator.dao.interfaces.StepSuggestionDAO;
import com.epam.test_generator.dto.StepSuggestionDTO;
import com.epam.test_generator.entities.StepSuggestion;
import com.epam.test_generator.entities.StepType;
import com.epam.test_generator.transformers.StepSuggestionTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.epam.test_generator.services.utils.UtilsService.checkNotNull;

@Transactional
@Service
public class StepSuggestionService {

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

    List<StepSuggestionDTO> allSuggestionSteps;

    @PostConstruct
    private void initializeDB() {
        allSuggestionSteps = getStepsSuggestions();
        loadDefaultStepSuggestions(given, StepType.GIVEN);
        loadDefaultStepSuggestions(when, StepType.WHEN);
        loadDefaultStepSuggestions(then, StepType.THEN);
        loadDefaultStepSuggestions(and, StepType.AND);
    }


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

    public Long addStepSuggestion(StepSuggestionDTO stepSuggestionDTO) {
        StepSuggestion stepSuggestion = stepSuggestionDAO.save(stepSuggestionTransformer.fromDto(stepSuggestionDTO));

        return stepSuggestion.getId();
    }

    public void updateStepSuggestion(Long stepSuggestionId, StepSuggestionDTO stepSuggestionDTO) {
        StepSuggestion stepSuggestion = stepSuggestionDAO.findOne(stepSuggestionId);
        checkNotNull(stepSuggestion);
        stepSuggestionTransformer.mapDTOToEntity(stepSuggestionDTO, stepSuggestion);

        stepSuggestionDAO.save(stepSuggestion);
    }

    public void removeStepSuggestion(Long stepSuggestionId) {
        StepSuggestion stepSuggestion = stepSuggestionDAO.findOne(stepSuggestionId);
        checkNotNull(stepSuggestion);

        stepSuggestionDAO.delete(stepSuggestionId);
    }
}
