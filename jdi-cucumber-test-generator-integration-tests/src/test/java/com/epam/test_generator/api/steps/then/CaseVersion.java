package com.epam.test_generator.api.steps.then;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import com.epam.http.response.RestResponse;
import com.epam.test_generator.api.steps.StepBackground;
import cucumber.api.java.en.Then;

public class CaseVersion extends StepBackground {

    @Then("^The case version should be returned$")
    public void theCaseVersionShouldBeReturned() throws Throwable{
        RestResponse response = testContext.getResponse();
        assertThat(response.status.code, equalTo(200));
        assertThat(response.raResponse().print(), not("[]"));
    }

    @Then("^The case version shouldn't be returned$")
    public void theCaseVersionShouldnTBeReturned() throws Throwable {
        RestResponse response = testContext.getResponse();
        assertThat(response.raResponse().getStatusCode(), equalTo(404));
        assertThat(response.raResponse().print(), equalTo(""));
    }
}
