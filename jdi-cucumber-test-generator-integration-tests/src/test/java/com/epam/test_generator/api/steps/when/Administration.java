package com.epam.test_generator.api.steps.when;

import static com.epam.test_generator.api.ApiTokenInserter.requestDataAndToken;
import static com.epam.test_generator.api.BddGeneratorApi.blockUserUsingPUT;
import static com.epam.test_generator.api.BddGeneratorApi.unblockUserUsingPUT;

import com.epam.http.response.RestResponse;
import com.epam.test_generator.api.steps.StepBackground;
import com.epam.test_generator.controllers.user.response.UserDTO;
import cucumber.api.java.en.When;

public class Administration extends StepBackground {

    @When("^I block user$")
    public void iBlockUser() throws Throwable {
        Long userId = testContext.getTestDTO(UserDTO.class).getId();
        RestResponse response = blockUserUsingPUT.call(
            requestDataAndToken(
                d -> {
                    d.pathParams.add("userId", userId.toString());
                }, testContext.getAdminToken()));
        testContext.setResponse(response);
    }

    @When("^I unblock user$")
    public void iUnblockUser() throws Throwable {
        Long userId = testContext.getTestDTO(UserDTO.class).getId();
        RestResponse response = unblockUserUsingPUT.call(
            requestDataAndToken(
                d -> {
                    d.pathParams.add("userId", userId.toString());
                }, testContext.getAdminToken()));
        testContext.setResponse(response);
    }

    @When("^I block user with wrong id$")
    public void iBlockUserWithWrongId() throws Throwable {
        Long userId = Long.MAX_VALUE;
        RestResponse response = blockUserUsingPUT.call(
            requestDataAndToken(
                d -> {
                    d.pathParams.add("userId", userId.toString());
                }, testContext.getAdminToken()));
        testContext.setResponse(response);
    }

}
