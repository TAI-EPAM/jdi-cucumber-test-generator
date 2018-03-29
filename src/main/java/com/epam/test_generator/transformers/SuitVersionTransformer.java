package com.epam.test_generator.transformers;

import com.epam.test_generator.dto.SuitVersionDTO;
import com.epam.test_generator.pojo.SuitVersion;
import org.springframework.stereotype.Component;

@Component
public class SuitVersionTransformer extends AbstractDozerTransformer<SuitVersion, SuitVersionDTO> {

    @Override
    protected Class<SuitVersion> getEntityClass() {
        return SuitVersion.class;
    }

    @Override
    protected Class<SuitVersionDTO> getDTOClass() {
        return SuitVersionDTO.class;
    }
}


