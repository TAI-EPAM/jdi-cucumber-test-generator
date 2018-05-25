package com.epam.test_generator.api.steps.given;

import com.epam.http.response.RestResponse;
import com.epam.test_generator.api.steps.StepBackground;
import com.epam.test_generator.api.RestApiFacade;
import com.epam.test_generator.controllers.user.request.LoginUserDTO;
import com.epam.test_generator.dto.TokenDTO;
import cucumber.api.DataTable;
import cucumber.api.java.en.Given;

import java.util.List;

import static com.epam.http.requests.RequestData.requestBody;
import static com.epam.test_generator.api.BddGeneratorApi.loginUsingPOST;

public class LoginAs extends StepBackground {

    private static RestApiFacade restApiFacade = new RestApiFacade();

    @Given("^I login as user")
    public void enterAsUser(DataTable table) throws Throwable{
        List<LoginUserDTO> loginUserDTOs = table.asList(LoginUserDTO.class);
        String loginUserDTOAsString = mapper
                .writeValueAsString(loginUserDTOs.get(0));
        RestResponse response = loginUsingPOST.call(requestBody(loginUserDTOAsString));
        TokenDTO userTokenDTO = mapper.readValue(response.raResponse().print(), TokenDTO.class);
        testContext.setToken(userTokenDTO.getToken());
        restApiFacade.loginAdmin();
    }
}
