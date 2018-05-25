package com.epam.test_generator.api.steps.when;

import static com.epam.test_generator.api.ApiTokenInserter.requestDataAndToken;
import static com.epam.test_generator.api.BddGeneratorApi.getCaseVersions;

import com.epam.http.response.RestResponse;
import com.epam.test_generator.api.steps.StepBackground;
import com.epam.test_generator.controllers.caze.response.CaseDTO;
import com.epam.test_generator.controllers.project.response.ProjectDTO;
import com.epam.test_generator.controllers.suit.response.SuitDTO;
import cucumber.api.java.en.When;

public class CaseVersion extends StepBackground {

    @When("^I get case version by case id$")
    public void iGetCaseVersionByCaseId() throws Throwable {

        Long projectId = testContext.getAndDeleteTestDTO(ProjectDTO.class).getId();
        Long suitId = testContext.getAndDeleteTestDTO(SuitDTO.class).getId();
        Long caseId = testContext.getAndDeleteTestDTO(CaseDTO.class).getId();

        RestResponse response = getCaseVersions.call(
            requestDataAndToken(
                d -> {
                    d.pathParams.add("projectId", projectId.toString());
                    d.pathParams.add("suitId", suitId.toString());
                    d.pathParams.add("caseId", caseId.toString());
                },
                testContext.getToken()
            )
        );

        testContext.setResponse(response);
    }

}
