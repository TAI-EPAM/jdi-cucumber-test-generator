package com.epam.test_generator.transformers;

import com.epam.test_generator.dto.StepSuggestionDTO;
import com.epam.test_generator.entities.StepSuggestion;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class StepSuggestionTransformer {

    public StepSuggestionDTO toDto(StepSuggestion stepSuggestion) {
        return new StepSuggestionDTO(stepSuggestion.getId(),stepSuggestion.getContent(),
            stepSuggestion.getType(),stepSuggestion.getVersion());
    }

    public List<StepSuggestionDTO> toDtoList (List<StepSuggestion> stepSuggestions) {
        return stepSuggestions.stream().map(this::toDto).collect(Collectors.toList());
    }
}
