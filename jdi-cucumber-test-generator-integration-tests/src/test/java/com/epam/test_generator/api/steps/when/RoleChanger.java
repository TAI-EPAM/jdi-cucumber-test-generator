package com.epam.test_generator.api.steps.when;

import com.epam.test_generator.api.steps.StepBackground;
import com.epam.test_generator.api.RestApiFacade;
import com.epam.test_generator.controllers.user.response.UserDTO;
import cucumber.api.java.en.When;

public class RoleChanger extends StepBackground {

    private static RestApiFacade restApiFacade = new RestApiFacade();

    @When("^I change user role on (.*)")
    public void iUpdateUserRole(String newRole) throws Throwable {

        UserDTO userDTO = testContext.getTestDTO(UserDTO.class);
        restApiFacade.changeRole(userDTO.getEmail(), newRole);
    }
}
