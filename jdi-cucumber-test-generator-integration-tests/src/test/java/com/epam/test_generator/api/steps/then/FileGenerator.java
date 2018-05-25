package com.epam.test_generator.api.steps.then;

import com.epam.http.response.RestResponse;
import com.epam.test_generator.api.steps.StepBackground;
import cucumber.api.java.en.Then;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class FileGenerator extends StepBackground {

    @Then("^The file should be returned$")
    public void theFileShouldBeReturned() {
        RestResponse response = testContext.getResponse();
        assertThat(response.raResponse().getStatusCode(), equalTo(200));
        assertThat(response.raResponse().print().isEmpty(), equalTo(false));
    }
}
