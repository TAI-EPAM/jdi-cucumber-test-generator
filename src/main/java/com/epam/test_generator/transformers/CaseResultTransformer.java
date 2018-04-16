package com.epam.test_generator.transformers;

import com.epam.test_generator.dto.CaseResultDTO;
import com.epam.test_generator.entities.results.CaseResult;
import org.springframework.stereotype.Component;

@Component
public class CaseResultTransformer extends AbstractDozerTransformer<CaseResult, CaseResultDTO> {

    @Override
    protected Class<CaseResult> getEntityClass() {
        return CaseResult.class;
    }

    @Override
    protected Class<CaseResultDTO> getDTOClass() {
        return CaseResultDTO.class;
    }
}
