package com.epam.test_generator.controllers.test.result;

import com.epam.test_generator.controllers.test.result.response.StepResultDTO;
import com.epam.test_generator.entities.results.StepResult;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class StepResultTransformer {

    public StepResultDTO toDto(StepResult result) {
        StepResultDTO dto = new StepResultDTO();
        dto.setDescription(result.getDescription());
        dto.setStatus(result.getStatus().getStatusName());
        return dto;
    }

    public List<StepResultDTO> toListDto(List<StepResult> results) {
        return results
            .stream()
            .map(this::toDto)
            .collect(Collectors.toList());
    }
}
