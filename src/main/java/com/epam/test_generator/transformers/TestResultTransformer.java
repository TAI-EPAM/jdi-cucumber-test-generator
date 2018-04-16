package com.epam.test_generator.transformers;

import com.epam.test_generator.dto.TestResultDTO;
import com.epam.test_generator.entities.results.TestResult;
import org.springframework.stereotype.Component;

@Component
public class TestResultTransformer extends AbstractDozerTransformer<TestResult, TestResultDTO> {

    @Override
    protected Class<TestResult> getEntityClass() {
        return TestResult.class;
    }

    @Override
    protected Class<TestResultDTO> getDTOClass() {
        return TestResultDTO.class;
    }
}
