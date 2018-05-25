package com.epam.test_generator.api.steps.when;

import static com.epam.test_generator.api.ApiTokenInserter.requestToken;
import static com.epam.test_generator.api.BddGeneratorApi.getUserProjects;

import com.epam.http.response.RestResponse;
import com.epam.test_generator.api.steps.StepBackground;
import cucumber.api.java.en.When;

public class AssigningProjects extends StepBackground {

    @When("^I try to get projects$")
    public void getUsersProjects() throws Throwable {
        RestResponse response = getUserProjects.call(requestToken(testContext.getToken()));
        testContext.setResponse(response);
    }
}
