package com.epam.test_generator.transformers;

import com.epam.test_generator.dto.CaseVersionDTO;
import com.epam.test_generator.pojo.CaseVersion;
import org.springframework.stereotype.Component;

@Component
public class CaseVersionTransformer extends AbstractDozerTransformer<CaseVersion, CaseVersionDTO> {

    @Override
    protected Class<CaseVersion> getEntityClass() {
        return CaseVersion.class;
    }

    @Override
    protected Class<CaseVersionDTO> getDTOClass() {
        return CaseVersionDTO.class;
    }
}
