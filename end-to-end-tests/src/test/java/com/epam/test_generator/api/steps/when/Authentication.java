package com.epam.test_generator.api.steps.when;

import static com.epam.http.requests.RequestData.requestBody;
import static com.epam.test_generator.api.BddGeneratorApi.loginUsingPOST;

import com.epam.http.response.RestResponse;
import com.epam.test_generator.api.steps.StepBackground;
import com.epam.test_generator.controllers.user.request.LoginUserDTO;
import com.epam.test_generator.controllers.user.request.RegistrationUserDTO;
import cucumber.api.DataTable;
import cucumber.api.java.en.When;
import java.util.List;

public class Authentication extends StepBackground {

    @When("^I sign in with wrong data$")
    public void admLoginRequest(DataTable table) throws Throwable{
        List<LoginUserDTO> loginUserDTOs = table.asList(LoginUserDTO.class);
        testContext.setTestDTO(loginUserDTOs.get(0));
        String loginUserDTOAsString = mapper
            .writeValueAsString(loginUserDTOs.get(0));
        RestResponse response = loginUsingPOST.call(requestBody(loginUserDTOAsString));
        testContext.setResponse(response);
    }
    @When("^I sign in$")
    public void admLoginRequest() throws Throwable {
        LoginUserDTO loginUserDTO = new LoginUserDTO();
        RegistrationUserDTO registrationUserDTO = testContext.getTestDTO(RegistrationUserDTO.class);
        loginUserDTO.setPassword(registrationUserDTO.getPassword());
        loginUserDTO.setEmail(registrationUserDTO.getEmail());
        testContext.setTestDTO(loginUserDTO);
        String loginUserDTOAsString = mapper
            .writeValueAsString(loginUserDTO);
        RestResponse response = loginUsingPOST.call(requestBody(loginUserDTOAsString));
        testContext.setResponse(response);
    }
}
