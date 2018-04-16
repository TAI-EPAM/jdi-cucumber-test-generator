package com.epam.test_generator.transformers;

import com.epam.test_generator.dto.SuitResultDTO;
import com.epam.test_generator.entities.results.SuitResult;
import org.springframework.stereotype.Component;

@Component
public class SuitResultTransformer extends AbstractDozerTransformer<SuitResult, SuitResultDTO> {

    @Override
    protected Class<SuitResult> getEntityClass() {
        return SuitResult.class;
    }

    @Override
    protected Class<SuitResultDTO> getDTOClass() {
        return SuitResultDTO.class;
    }
}
