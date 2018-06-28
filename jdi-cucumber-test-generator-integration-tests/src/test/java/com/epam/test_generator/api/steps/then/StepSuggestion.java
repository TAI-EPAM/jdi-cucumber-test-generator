package com.epam.test_generator.api.steps.then;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import com.epam.http.response.RestResponse;
import com.epam.test_generator.api.steps.StepBackground;
import com.epam.test_generator.controllers.stepsuggestion.response.StepSuggestionDTO;
import cucumber.api.java.en.Then;

public class StepSuggestion extends StepBackground {

    @Then("^The StepSuggestion should be created$")
    public void theStepShouldntBeCreated() throws Throwable {
        RestResponse response = testContext.getResponse();
        assertThat(response.raResponse().getStatusCode(), equalTo(201));
    }

    @Then("^The StepSuggestion should be updated$")
    public void theStepSuggestionUpdated() throws Throwable {
        RestResponse response = testContext.getResponse();
        assertThat(response.raResponse().getStatusCode(), equalTo(200));
    }

    @Then("^The StepSuggestion should be deleted$")
    public void theStepShouldBeDeleted() throws Throwable {
        RestResponse response = testContext.getResponse();
        assertThat(response.raResponse().getStatusCode(), equalTo(200));
    }

    @Then ("^I found Step Suggestion$")
    public void foundStepSuggestion(){
        StepSuggestionDTO testDTO = testContext
            .getAndDeleteTestDTO(StepSuggestionDTO.class);
        RestResponse response = testContext.getResponse();
        assertThat(response.raResponse().getStatusCode(),equalTo(200));
        assertThat(testDTO.getContent(), equalTo("stepuniqid"));
    }

}
