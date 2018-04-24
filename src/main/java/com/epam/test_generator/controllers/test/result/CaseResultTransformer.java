package com.epam.test_generator.controllers.test.result;

import com.epam.test_generator.controllers.test.result.response.CaseResultDTO;
import com.epam.test_generator.entities.results.CaseResult;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CaseResultTransformer {

    @Autowired
    private StepResultTransformer stepResultTransformer;

    public CaseResultDTO toDto(CaseResult result) {
        CaseResultDTO dto = new CaseResultDTO();
        dto.setComment(result.getComment());
        dto.setDuration(result.getDuration());
        dto.setName(result.getName());
        dto.setDisplayedStatusName(result.getStatus().getStatusName());
        dto.setStepResults(stepResultTransformer.toListDto(result.getStepResults()));
        return dto;
    }

    public List<CaseResultDTO> toListDto(List<CaseResult> results) {
        return results
            .stream()
            .map(this::toDto)
            .collect(Collectors.toList());
    }
}
