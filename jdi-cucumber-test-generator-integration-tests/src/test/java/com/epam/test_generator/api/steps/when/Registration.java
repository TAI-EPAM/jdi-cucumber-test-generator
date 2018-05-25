package com.epam.test_generator.api.steps.when;

import com.epam.http.response.RestResponse;
import com.epam.test_generator.api.steps.StepBackground;
import com.epam.test_generator.controllers.user.request.RegistrationUserDTO;
import cucumber.api.DataTable;
import cucumber.api.java.en.When;

import java.util.List;

import static com.epam.http.requests.RequestData.requestBody;
import static com.epam.test_generator.api.BddGeneratorApi.registerUserAccountUsingPOST;

public class Registration extends StepBackground {

    @When("^I register a new user$")
    public void sendUserDto(DataTable table) throws Throwable{
        List<RegistrationUserDTO> registrationUserDTOs = table.asList(RegistrationUserDTO.class);
        testContext.setTestDTO(registrationUserDTOs.get(0));
        String userDTOAsString = mapper.writeValueAsString(registrationUserDTOs.get(0));
        RestResponse response = registerUserAccountUsingPOST.call(requestBody(userDTOAsString));
        testContext.setResponse(response);
    }
}
