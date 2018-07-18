package com.epam.test_generator.api.steps.when;


import static com.epam.test_generator.api.ApiTokenInserter.requestDataAndToken;
import static com.epam.test_generator.api.BddGeneratorApi.addStepSuggestion;
import static com.epam.test_generator.api.BddGeneratorApi.removeStepSuggestion;
import static com.epam.test_generator.api.BddGeneratorApi.updateStepSuggestion;

import com.epam.http.response.RestResponse;
import com.epam.test_generator.api.steps.StepBackground;
import com.epam.test_generator.controllers.project.response.ProjectDTO;
import com.epam.test_generator.controllers.stepsuggestion.request.StepSuggestionCreateDTO;
import com.epam.test_generator.controllers.stepsuggestion.request.StepSuggestionUpdateDTO;
import com.epam.test_generator.controllers.stepsuggestion.response.StepSuggestionDTO;
import com.epam.test_generator.entities.StepType;
import cucumber.api.java.en.When;

public class StepSuggestion extends StepBackground {

    @When("^I create StepSuggestion$")
    public void iCreateStepSuggestion() throws Throwable {
        Long projectId = testContext.getTestDTO(ProjectDTO.class).getId();
        StepSuggestionCreateDTO dto = new StepSuggestionCreateDTO("content", StepType.WHEN);
        String dtoAsString = mapper.writeValueAsString(dto);

        RestResponse response = addStepSuggestion.call(requestDataAndToken(
            d -> {
                d.pathParams.add("projectId", projectId.toString());
                d.body = dtoAsString;
            },
            testContext.getToken()));
        testContext.setResponse(response);
        testContext
            .setTestDTO(mapper.readValue(response.raResponse().print(), StepSuggestionDTO.class));
    }

    @When("^I update StepSuggestion$")
    public void iChangeStepSuggestion() throws Throwable {
        Long projectId = testContext.getTestDTO(ProjectDTO.class).getId();
        Long stepId = testContext.getTestDTO(StepSuggestionDTO.class).getId();
        Long version = testContext.getAndDeleteTestDTO(StepSuggestionDTO.class).getVersion();

        StepSuggestionUpdateDTO dto = new StepSuggestionUpdateDTO("newContent", StepType.GIVEN, version);
        String dtoAsString = mapper.writeValueAsString(dto);
        RestResponse response = updateStepSuggestion.call(requestDataAndToken(
            d -> {
                d.pathParams.add("projectId", projectId.toString());
                d.pathParams.add("stepSuggestionId", stepId.toString());
                d.body = dtoAsString;
            },
            testContext.getToken()));
        testContext.setResponse(response);
    }

    @When("^I delete StepSuggestion$")
    public void iDeleteStepSuggestion() throws Throwable {
        Long projectId = testContext.getTestDTO(ProjectDTO.class).getId();
        Long stepId = testContext.getAndDeleteTestDTO(StepSuggestionDTO.class).getId();

        RestResponse response = removeStepSuggestion.call(requestDataAndToken(
            d -> {
                d.pathParams.add("projectId", projectId.toString());
                d.pathParams.add("stepSuggestionId", stepId.toString());
            },
            testContext.getToken()));
        testContext.setResponse(response);
    }
}
