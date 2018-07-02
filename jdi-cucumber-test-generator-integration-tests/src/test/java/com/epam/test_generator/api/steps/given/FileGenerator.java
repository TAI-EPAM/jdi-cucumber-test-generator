package com.epam.test_generator.api.steps.given;

import com.epam.test_generator.api.steps.StepBackground;
import com.epam.test_generator.controllers.caze.response.CaseDTO;
import com.epam.test_generator.entities.request.FeatureFileDTO;
import com.epam.test_generator.controllers.suit.response.SuitDTO;
import com.epam.test_generator.dto.wrapper.ListWrapper;
import cucumber.api.java.en.Given;
import java.util.Collections;

public class FileGenerator extends StepBackground {

    @Given("^I have information for generating feature file$")
    public void iHaveAFeatureFile() {
        FeatureFileDTO featureFileDTO = new FeatureFileDTO();
        featureFileDTO.setSuitId(testContext.getTestDTO(SuitDTO.class).getId());
        featureFileDTO.setCaseIds(
            Collections.singletonList(testContext.getTestDTO(CaseDTO.class).getId()));
        ListWrapper<FeatureFileDTO> featureFileDTOListWrapper = new ListWrapper<>();
        featureFileDTOListWrapper.setList(Collections.singletonList(featureFileDTO));
        testContext.setTestDTO(featureFileDTOListWrapper);
    }
}
