package com.epam.test_generator.api.steps.then;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import com.epam.http.response.RestResponse;
import com.epam.test_generator.api.steps.StepBackground;
import cucumber.api.java.en.Then;
import org.springframework.http.HttpStatus;

public class RoleChanger extends StepBackground {

    @Then("^User role must be changed$")
    public void userRoleMustBeChanged() throws Throwable {
        RestResponse response = testContext.getResponse();

        assertThat(response.raResponse().getStatusCode(), equalTo(HttpStatus.OK.value()));
    }

    @Then("^User role should not be changed$")
    public void userRoleShouldNotBeChanged() throws Throwable {
        RestResponse response = testContext.getResponse();

        assertThat(response.raResponse().getStatusCode(), equalTo(401));
    }
}
