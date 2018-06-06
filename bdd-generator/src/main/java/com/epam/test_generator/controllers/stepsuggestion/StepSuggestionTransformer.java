package com.epam.test_generator.controllers.stepsuggestion;

import com.epam.test_generator.controllers.step.StepTransformer;
import com.epam.test_generator.controllers.step.response.StepDTO;
import com.epam.test_generator.controllers.stepsuggestion.request.StepSuggestionCreateDTO;
import com.epam.test_generator.controllers.stepsuggestion.request.StepSuggestionUpdateDTO;
import com.epam.test_generator.controllers.stepsuggestion.response.StepSuggestionDTO;
import com.epam.test_generator.entities.Step;
import com.epam.test_generator.entities.StepSuggestion;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StepSuggestionTransformer {
    @Autowired
    StepTransformer stepTransformer;

    public StepSuggestionDTO toDto(StepSuggestion stepSuggestion) {
        List<StepDTO> stepDTOs = stepSuggestion.getSteps().stream().map(step -> stepTransformer.toDto(step))
            .collect(Collectors.toList());

        return new StepSuggestionDTO(
            stepSuggestion.getId(),
            stepSuggestion.getContent(),
            stepSuggestion.getType(),
            stepDTOs
        );
    }

    public List<StepSuggestionDTO> toDtoList(Collection<StepSuggestion> stepSuggestions) {
        return stepSuggestions.stream().map(this::toDto).collect(Collectors.toList());
    }

    public StepSuggestion fromDto(StepSuggestionCreateDTO dto) {
        return new StepSuggestion(dto.getContent(), dto.getType());
    }

    public void updateFromDto(StepSuggestion stepSuggestion, StepSuggestionUpdateDTO dto) {
        if (dto.getContent() != null) {
            stepSuggestion.setContent(dto.getContent());
        }

        if (dto.getType() != null) {
            stepSuggestion.setType(dto.getType());
        }
    }

}
