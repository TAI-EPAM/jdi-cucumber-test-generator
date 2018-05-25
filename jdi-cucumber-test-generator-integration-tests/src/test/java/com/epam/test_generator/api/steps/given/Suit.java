package com.epam.test_generator.api.steps.given;

import static com.epam.test_generator.api.ApiTokenInserter.requestDataAndToken;
import static com.epam.test_generator.api.BddGeneratorApi.createSuit;

import com.epam.http.response.RestResponse;
import com.epam.test_generator.api.steps.StepBackground;
import com.epam.test_generator.controllers.project.response.ProjectDTO;
import com.epam.test_generator.controllers.suit.request.SuitCreateDTO;
import com.epam.test_generator.controllers.suit.response.SuitDTO;
import cucumber.api.DataTable;
import cucumber.api.java.en.Given;
import java.util.HashSet;

public class Suit extends StepBackground {

    private static final int FIRST_INDEX = 0;

    @Given("^I have a suit that doesn't exist in data base$")
    public void iHaveAProjectThatDoesntExist(DataTable dataTable) {
        testContext.setTestDTO(dataTable.asList(SuitDTO.class).get(FIRST_INDEX));
    }

    @Given("^I have a suit$")
    public void iHaveASuit(DataTable dataTable) throws Throwable {
        Long projectId = testContext.getTestDTO(ProjectDTO.class).getId();
        SuitCreateDTO suitCreateDTO = dataTable.asList(SuitCreateDTO.class).get(FIRST_INDEX);
        suitCreateDTO.setTags(testContext.getAndDeleteTestDTO(HashSet.class));
        String suitCreateDTOAsString = mapper.writeValueAsString(suitCreateDTO);
        RestResponse response = createSuit.call(
            requestDataAndToken(d -> {
                d.body = suitCreateDTOAsString;
                d.pathParams.add("projectId", projectId.toString());
            }, testContext.getToken())
        );
        SuitDTO suitDTO = mapper.readValue(response.raResponse().print(), SuitDTO.class);
        testContext.setTestDTO(suitDTO);
    }

}
