package com.epam.test_generator.api.steps.then;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import com.epam.http.response.RestResponse;
import com.epam.test_generator.api.steps.StepBackground;
import com.epam.test_generator.controllers.caze.request.CaseCreateDTO;
import com.epam.test_generator.controllers.caze.response.CaseDTO;
import com.epam.test_generator.controllers.user.response.UserDTO;
import cucumber.api.java.en.Then;

public class Administration extends StepBackground{

    @Then("^User must be blocked$")
    public void userMustBeBlocked() throws Throwable {

        RestResponse response = testContext.getResponse();
        UserDTO actualUserDTO = mapper.readValue(response.raResponse().print(), UserDTO.class);
        assertThat(response.raResponse().getStatusCode(), equalTo(200));
        assertThat(actualUserDTO.getBlockedByAdmin(), equalTo(true));
    }

    @Then("^User must be unblocked$")
    public void userMustBeUnblocked() throws Throwable {

        RestResponse response = testContext.getResponse();
        UserDTO actualUserDTO = mapper.readValue(response.raResponse().print(), UserDTO.class);
        assertThat(response.raResponse().getStatusCode(), equalTo(200));
        assertThat(actualUserDTO.getBlockedByAdmin(), equalTo(false));
    }

    @Then("^The user shouldn't be found$")
    public void theUserShouldntBeFounded() {
        RestResponse response = testContext.getResponse();
        assertThat(response.raResponse().getStatusCode(), equalTo(404));
        assertThat(response.raResponse().print(), equalTo(""));
    }
}
