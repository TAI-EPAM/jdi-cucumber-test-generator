package com.epam.test_generator.api.steps.given;

import static com.epam.http.requests.ServiceInit.init;
import static com.epam.test_generator.api.ApiTokenInserter.requestDataAndToken;
import static com.epam.test_generator.api.BddGeneratorApi.addStepToCase;

import com.epam.http.response.RestResponse;
import com.epam.test_generator.api.BddGeneratorApi;
import com.epam.test_generator.api.steps.StepBackground;
import com.epam.test_generator.controllers.caze.response.CaseDTO;
import com.epam.test_generator.controllers.project.response.ProjectDTO;
import com.epam.test_generator.controllers.step.request.StepCreateDTO;
import com.epam.test_generator.controllers.step.response.StepDTO;
import com.epam.test_generator.controllers.suit.response.SuitDTO;
import cucumber.api.DataTable;
import cucumber.api.java.en.Given;

public class Step extends StepBackground {

    private static final int FIRST_INDEX = 0;

    @Given("^I have a step$")
    public void iHaveAStep(DataTable dataTable) throws Throwable {
        Long projectId = testContext.getTestDTO(ProjectDTO.class).getId();
        Long suitId = testContext.getTestDTO(SuitDTO.class).getId();
        Long caseId = testContext.getTestDTO(CaseDTO.class).getId();
        String stepCreateDTOAsString = mapper
            .writeValueAsString(dataTable.asList(StepCreateDTO.class).get(FIRST_INDEX));
        RestResponse response = addStepToCase.call(
            requestDataAndToken(d -> {
                d.body = stepCreateDTOAsString;
                d.pathParams.add("projectId", projectId.toString());
                d.pathParams.add("suitId", suitId.toString());
                d.pathParams.add("caseId", caseId.toString());
            }, testContext.getToken())
        );
        StepDTO stepDTO = mapper.readValue(response.raResponse().print(), StepDTO.class);
        testContext.setTestDTO(stepDTO);
        init(BddGeneratorApi.class);
    }

    @Given("^I have a step that doesn't exist in data base$")
    public void iHaveAStepThatDoesntExist(DataTable dataTable) {
        testContext.setTestDTO(dataTable.asList(StepDTO.class).get(FIRST_INDEX));
    }

}
