package com.epam.test_generator.api.steps.then;


import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import com.epam.http.response.RestResponse;
import com.epam.test_generator.api.steps.StepBackground;
import cucumber.api.java.en.Then;

public class UpdateStepByStepSugg extends StepBackground {

    @Then("^The step should be updated by StepSuggestion$")
    public void theStepShouldBeUpdated() throws Throwable {
        RestResponse response = testContext.getResponse();

        assertThat(response.raResponse().getStatusCode(), equalTo(200));
    }
}
