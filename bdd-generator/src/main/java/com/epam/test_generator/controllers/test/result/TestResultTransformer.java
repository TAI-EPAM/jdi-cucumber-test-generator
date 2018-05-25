package com.epam.test_generator.controllers.test.result;

import com.epam.test_generator.controllers.test.result.response.TestResultDTO;
import com.epam.test_generator.entities.results.TestResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TestResultTransformer {

    @Autowired
    private SuitResultTransformer suitResultTransformer;

    public TestResultDTO toDto(TestResult testResult) {
        TestResultDTO dto = new TestResultDTO();
        dto.setDate(testResult.getDate().toInstant().toEpochMilli());
        dto.setDuration(testResult.getDuration());
        dto.setExecutedBy(testResult.getExecutedBy());
        dto.setDisplayedStatusName(testResult.getStatus().getStatusName());
        dto.setAmountOfPassed(testResult.getAmountOfPassed());
        dto.setAmountOfFailed(testResult.getAmountOfFailed());
        dto.setAmountOfSkipped(testResult.getAmountOfSkipped());
        dto.setSuitResults(suitResultTransformer.toListDto(testResult.getSuitResults()));
        return dto;
    }
}
