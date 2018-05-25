package com.epam.test_generator.api.steps.then;

import static com.epam.test_generator.api.ApiTokenInserter.requestDataAndToken;
import static com.epam.test_generator.api.BddGeneratorApi.getProjectSuit;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import com.epam.http.response.RestResponse;
import com.epam.test_generator.api.steps.StepBackground;
import com.epam.test_generator.controllers.caze.request.CaseCreateDTO;
import com.epam.test_generator.controllers.caze.response.CaseDTO;
import com.epam.test_generator.controllers.project.request.ProjectCreateDTO;
import com.epam.test_generator.controllers.project.response.ProjectDTO;
import com.epam.test_generator.controllers.suit.request.SuitCreateDTO;
import com.epam.test_generator.controllers.suit.response.SuitDTO;
import cucumber.api.java.en.Then;

public class AssertStepsDef extends StepBackground {


    @Then("^Case should have status '(.+)'$")
    public void theCaseShouldHaveStatus(String status) throws Throwable {
        status = status.toUpperCase();
        RestResponse response = testContext.getResponse();
        CaseDTO actualCaseDTO = mapper.readValue(response.raResponse().print(), CaseDTO.class);

        assertThat(actualCaseDTO.getDisplayedStatusName(), equalTo(status));

    }

    @Then("^Suit should have status '(.+)'$")
    public void theSuitShouldHaveStatus(String status) throws Throwable {
        status = status.toUpperCase();

        Long projectId = testContext.getTestDTO(ProjectDTO.class).getId();
        Long suitId = testContext.getTestDTO(SuitDTO.class).getId();

        RestResponse response = getProjectSuit.call(requestDataAndToken(d -> {
            d.pathParams.add("projectId", projectId.toString());
            d.pathParams.add("suitId", suitId.toString());
        }, testContext.getToken()));

        SuitDTO actualSuitDTO = mapper.readValue(response.raResponse().print(), SuitDTO.class);
        assertThat(actualSuitDTO.getDisplayedStatusName(), equalTo(status));
    }
}
