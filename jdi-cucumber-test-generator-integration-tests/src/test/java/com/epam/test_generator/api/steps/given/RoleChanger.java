package com.epam.test_generator.api.steps.given;

import com.epam.test_generator.api.container.TestContext;
import com.epam.test_generator.api.steps.StepBackground;
import com.epam.test_generator.api.RestApiFacade;
import com.epam.test_generator.controllers.user.response.UserDTO;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;

public class RoleChanger extends StepBackground {
    private static RestApiFacade restApiFacade = new RestApiFacade();

    @Given("^I have new user")
    public void iHaveNewUser() throws Throwable {
        restApiFacade.registerUser();
    }

    @Given("^I logged in as admin$")
    public void iLoggedInAsAdmin() throws Throwable {
        restApiFacade.loginAdmin();
    }

    @And("I have user with role (.*)$")
    public void iHaveNewUserWithRole(String role) throws Throwable{
        restApiFacade.registerUser();
        restApiFacade.loginAdmin();
        restApiFacade.changeRole(testContext.getTestDTO(UserDTO.class).getEmail(), role);
        UserDTO userDTO = testContext.getTestDTO(UserDTO.class);
        restApiFacade.loginUser(userDTO.getEmail(), TestContext.USER_PASSWORD);
    }
}
