package com.epam.test_generator.api.steps.when;

import static com.epam.test_generator.api.ApiTokenInserter.requestDataAndToken;
import static com.epam.test_generator.api.BddGeneratorApi.findStepsSuggestions;

import com.epam.http.response.RestResponse;
import com.epam.test_generator.api.steps.StepBackground;
import com.epam.test_generator.controllers.project.response.ProjectDTO;
import com.epam.test_generator.controllers.stepsuggestion.response.StepSuggestionDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import cucumber.api.java.en.And;
import java.util.List;

public class StepSuggestionSerch extends StepBackground{

    @And("^I search StepSuggestion$")
    public void iSearchStepSuggestion()  throws Throwable {
        Long projectId = testContext.getTestDTO(ProjectDTO.class).getId();
            RestResponse response = findStepsSuggestions.call(requestDataAndToken(
                d -> {
                    d.pathParams.add("projectId", projectId.toString());
                    d.queryParams.addFirst("pageSize", "1");
                    d.queryParams.addFirst("pageNumber", "1");
                    d.queryParams.addFirst("text","stepuniqid");
                },
                testContext.getToken()));
        List<StepSuggestionDTO> stepSuggestions = mapper
            .readValue(response.raResponse().print(), new TypeReference<List<StepSuggestionDTO>>() {
            });
        testContext.setResponse(response);
        testContext.setTestDTO(stepSuggestions);
        testContext.setTestDTO(stepSuggestions.get(0));
        }

}
