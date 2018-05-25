package com.epam.test_generator.controllers.test.result;

import com.epam.test_generator.controllers.test.result.response.SuitResultDTO;
import com.epam.test_generator.entities.results.SuitResult;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SuitResultTransformer {

    @Autowired
    private CaseResultTransformer caseResultTransformer;

    public SuitResultDTO toDto(SuitResult result) {
        SuitResultDTO dto = new SuitResultDTO();
        dto.setCaseResults(caseResultTransformer.toListDto(result.getCaseResults()));
        dto.setDuration(result.getDuration());
        dto.setName(result.getName());
        dto.setStatus(result.getStatus().getStatusName());
        return dto;
    }

    public List<SuitResultDTO> toListDto(List<SuitResult> results) {
        return results
            .stream()
            .map(this::toDto)
            .collect(Collectors.toList());
    }
}
