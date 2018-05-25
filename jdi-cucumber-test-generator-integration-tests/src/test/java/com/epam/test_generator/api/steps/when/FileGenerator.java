package com.epam.test_generator.api.steps.when;

import com.epam.http.response.RestResponse;
import com.epam.test_generator.api.steps.StepBackground;
import com.epam.test_generator.controllers.caze.response.CaseDTO;
import com.epam.test_generator.controllers.project.response.ProjectDTO;
import com.epam.test_generator.controllers.suit.response.SuitDTO;
import cucumber.api.java.en.When;

import static com.epam.test_generator.api.ApiTokenInserter.requestDataAndToken;
import static com.epam.test_generator.api.BddGeneratorApi.downloadFileUsingPOST;

public class FileGenerator extends StepBackground {

    @When("^I download file$")
    public void iDownloadFile() {
        Long projectId = testContext.getAndDeleteTestDTO(ProjectDTO.class).getId();
        Long suitId = testContext.getAndDeleteTestDTO(SuitDTO.class).getId();
        Long caseId = testContext.getAndDeleteTestDTO(CaseDTO.class).getId();
        RestResponse response = downloadFileUsingPOST.call(requestDataAndToken(d -> {
            d.pathParams.add("projectId", projectId.toString());
            d.pathParams.add("suitId", suitId.toString());
            d.body = "[" + caseId + "]";
        }, testContext.getToken()));
        testContext.setResponse(response);
    }
}
