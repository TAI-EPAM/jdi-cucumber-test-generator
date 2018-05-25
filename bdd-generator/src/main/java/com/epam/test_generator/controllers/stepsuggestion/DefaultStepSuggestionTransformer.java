package com.epam.test_generator.controllers.stepsuggestion;

import com.epam.test_generator.controllers.stepsuggestion.request.StepSuggestionCreateDTO;
import com.epam.test_generator.controllers.stepsuggestion.response.StepSuggestionDTO;
import com.epam.test_generator.entities.DefaultStepSuggestion;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class DefaultStepSuggestionTransformer {

    public StepSuggestionDTO toDto(DefaultStepSuggestion defaultstepSuggestion) {
        return new StepSuggestionDTO(
                defaultstepSuggestion.getId(),
                defaultstepSuggestion.getContent(),
                defaultstepSuggestion.getType()
        );
    }

    public List<StepSuggestionDTO> toDtoList(List<DefaultStepSuggestion> defaultStepSuggestions) {
        return defaultStepSuggestions.stream().map(this::toDto).collect(Collectors.toList());
    }

}
