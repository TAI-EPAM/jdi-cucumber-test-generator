package com.epam.test_generator.api.steps.then;

import com.epam.http.response.RestResponse;
import com.epam.test_generator.api.steps.StepBackground;
import com.epam.test_generator.dto.TokenDTO;
import cucumber.api.java.en.Then;
import org.springframework.http.HttpStatus;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class Authentication extends StepBackground {

    @Then("^I am signed in$")
    public void requestAssert() throws Throwable {
        RestResponse response = testContext.getResponse();
        TokenDTO userTokenDTO = mapper.readValue(response.raResponse().print(), TokenDTO.class);

        assertThat(response.raResponse().getStatusCode(), equalTo(HttpStatus.OK.value()));
        testContext.setToken(userTokenDTO.getToken());
    }

}
