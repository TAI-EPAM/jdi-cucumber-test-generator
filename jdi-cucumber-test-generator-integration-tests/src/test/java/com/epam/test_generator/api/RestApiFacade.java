package com.epam.test_generator.api;

import com.epam.http.response.RestResponse;
import com.epam.test_generator.api.container.TestContext;
import com.epam.test_generator.controllers.admin.request.UserRoleUpdateDTO;
import com.epam.test_generator.controllers.caze.request.CaseCreateDTO;
import com.epam.test_generator.controllers.caze.response.CaseDTO;
import com.epam.test_generator.controllers.caze.response.CaseDTO;
import com.epam.test_generator.controllers.project.response.ProjectDTO;
import com.epam.test_generator.controllers.suit.response.SuitDTO;
import com.epam.test_generator.controllers.step.response.StepDTO;
import com.epam.test_generator.controllers.suit.response.SuitDTO;
import com.epam.test_generator.controllers.user.request.LoginUserDTO;
import com.epam.test_generator.controllers.user.request.RegistrationUserDTO;
import com.epam.test_generator.controllers.user.response.UserDTO;
import com.epam.test_generator.dto.TokenDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashSet;
import org.springframework.http.HttpStatus;

import static com.epam.http.requests.RequestData.requestBody;
import static com.epam.test_generator.api.ApiTokenInserter.requestBodyAndToken;
import static com.epam.test_generator.api.ApiTokenInserter.requestDataAndToken;
import static com.epam.test_generator.api.BddGeneratorApi.*;

public class RestApiFacade {

    protected ObjectMapper mapper = new ObjectMapper();
    protected TestContext testContext = TestContext.getTestContext();


    public void registerUser() throws Throwable {
        RegistrationUserDTO registrationUserDTO = createRegistrationUserDTO();
        RestResponse response;
        do {
            String email = "test" + 0 + (int) (Math.random() * 10000) + "@mail.com";
            registrationUserDTO.setEmail(email);
            String userDTOAsString = mapper.writeValueAsString(registrationUserDTO);
            response = registerUserAccountUsingPOST.call(requestBody(userDTOAsString));
        } while (response.raResponse().getStatusCode() != HttpStatus.OK.value());
        UserDTO userDTO = mapper
                .readValue(response.raResponse().print(), UserDTO.class);
        testContext.setTestDTO(registrationUserDTO);
        testContext.setTestDTO(userDTO);
        testContext.setResponse(response);
    }


    public void loginUser(String email, String password) throws Throwable {
        TokenDTO tokenDTO = login(email, password);
        testContext.setToken(tokenDTO.getToken());
    }

    public void loginAdmin() throws Throwable {
        TokenDTO tokenDTO = login(TestContext.ADMIN_EMAIL, TestContext.ADMIN_PASSWORD);
        testContext.setAdminToken(tokenDTO.getToken());
    }

    public void changeRole(String email, String role) throws Throwable {
        UserRoleUpdateDTO userRoleUpdateDTO = new UserRoleUpdateDTO();
        userRoleUpdateDTO.setEmail(email);
        userRoleUpdateDTO.setRole(role);

        String userRoleUpdateDTOAsString = mapper.writeValueAsString(userRoleUpdateDTO);

        RestResponse response = changeUserRoleUsingPUT
                .call(requestBodyAndToken(userRoleUpdateDTOAsString, testContext.getAdminToken()));
        testContext.setResponse(response);
    }

    public void assignUserToProject() {

        RestResponse response = addUserToProject.call(requestDataAndToken(d -> {
                    d.pathParams.add("projectId", testContext.getTestDTO(ProjectDTO.class).getId().toString());
                    d.pathParams.add("userId", testContext.getTestDTO(UserDTO.class).getId().toString());
                }, testContext.getAdminToken()
        ));
        testContext.setResponse(response);
    }

    public RestResponse getResponseWithStepFromeContext(){
        Long projectId = testContext.getTestDTO(ProjectDTO.class).getId();
        Long suitId = testContext.getTestDTO(SuitDTO.class).getId();
        Long caseId = testContext.getTestDTO(CaseDTO.class).getId();
        Long stepId = testContext.getTestDTO(StepDTO.class).getId();
        RestResponse response = getStep.call(
            requestDataAndToken(
                d -> {
                    d.pathParams.add("projectId", projectId.toString());
                    d.pathParams.add("suitId", suitId.toString());
                    d.pathParams.add("caseId", caseId.toString());
                    d.pathParams.add("stepId", stepId.toString());
                },
                testContext.getToken()
            )
        );
        return response;

    }

    public void addCaseToSuit(CaseCreateDTO caseCreateDTO) throws Throwable {
        Long projectId = testContext.getTestDTO(ProjectDTO.class).getId();
        Long suitId = testContext.getTestDTO(SuitDTO.class).getId();
        caseCreateDTO.setTags(testContext.getAndDeleteTestDTO(HashSet.class));
        String caseCreateDTOAsString = mapper.writeValueAsString(caseCreateDTO);
        RestResponse response = addCaseToSuit.call(
            requestDataAndToken(d -> {
                d.body = caseCreateDTOAsString;
                d.pathParams.add("projectId", projectId.toString());
                d.pathParams.add("suitId", suitId.toString());
            }, testContext.getToken())
        );
        CaseDTO caseDTO = mapper.readValue(response.raResponse().print(), CaseDTO.class);
        testContext.setTestDTO(caseDTO);
    }

    private TokenDTO login(String email, String password) throws Throwable {
        LoginUserDTO loginUserDTO = new LoginUserDTO();
        loginUserDTO.setEmail(email);
        loginUserDTO.setPassword(password);

        String loginUserDTOAsString = mapper
                .writeValueAsString(loginUserDTO);
        RestResponse response = loginUsingPOST.call(requestBody(loginUserDTOAsString));

        return mapper.readValue(response.raResponse().print(), TokenDTO.class);
    }

    private RegistrationUserDTO createRegistrationUserDTO() {
        RegistrationUserDTO registrationUserDTO = new RegistrationUserDTO();
        registrationUserDTO.setPassword(TestContext.USER_PASSWORD);
        registrationUserDTO.setName(TestContext.USER_NAME);
        registrationUserDTO.setSurname(TestContext.USER_SURNAME);
        return registrationUserDTO;
    }
}
