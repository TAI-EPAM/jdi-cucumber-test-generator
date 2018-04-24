package com.epam.test_generator.controllers.step;

import com.epam.test_generator.controllers.step.request.StepCreateDTO;
import com.epam.test_generator.controllers.step.response.StepDTO;
import com.epam.test_generator.controllers.step.request.StepUpdateDTO;
import com.epam.test_generator.entities.Step;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class StepTransformer {

    public StepDTO toDto(Step step) {
        return new StepDTO(step.getId(), step.getRowNumber(), step.getDescription(), step.getType(),
            step.getComment(), step.getStatus().getStatusName());
    }

    public List<StepDTO> toDtoList(List<Step> steps) {
        return steps.stream().map(this::toDto).collect(Collectors.toList());
    }

    public Step fromDto(StepCreateDTO stepCreateDTO) {
        return new Step(null, null, stepCreateDTO.getDescription(),
            stepCreateDTO.getType(), stepCreateDTO.getComment(), stepCreateDTO.getStatus());
    }

    public Step updateFromDto(StepUpdateDTO stepUpdateDTO, Step step) {
        if (stepUpdateDTO.getDescription() != null) {
            step.setDescription(stepUpdateDTO.getDescription());
        }

        if (stepUpdateDTO.getComment() != null) {
            step.setComment(stepUpdateDTO.getComment());
        }

        if (stepUpdateDTO.getRowNumber() != null) {
            step.setRowNumber(stepUpdateDTO.getRowNumber());
        }

        if (stepUpdateDTO.getType() != null) {
            step.setType(stepUpdateDTO.getType());
        }

        if (stepUpdateDTO.getStatus() != null) {
            step.setStatus(stepUpdateDTO.getStatus());
        }

        return step;
    }
}
