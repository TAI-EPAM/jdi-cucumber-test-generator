package com.epam.test_generator.api.steps.when;

import static com.epam.test_generator.api.ApiTokenInserter.requestDataAndToken;
import static com.epam.test_generator.api.BddGeneratorApi.createSuit;
import static com.epam.test_generator.api.BddGeneratorApi.getProjectSuit;
import static com.epam.test_generator.api.BddGeneratorApi.removeSuit;
import static com.epam.test_generator.api.BddGeneratorApi.updateSuit;

import com.epam.http.response.RestResponse;
import com.epam.test_generator.api.steps.StepBackground;
import com.epam.test_generator.controllers.project.response.ProjectDTO;
import com.epam.test_generator.controllers.suit.request.SuitCreateDTO;
import com.epam.test_generator.controllers.suit.request.SuitUpdateDTO;
import com.epam.test_generator.controllers.suit.response.SuitDTO;
import com.epam.test_generator.controllers.tag.response.TagDTO;
import cucumber.api.DataTable;
import cucumber.api.java.en.When;
import java.util.HashSet;

public class Suit extends StepBackground {

    private static final int FIRST_INDEX = 0;

    @When("^I create suit$")
    public void iCreateSuit(DataTable dataTable) throws Throwable {
        Long projectId = testContext.getTestDTO(ProjectDTO.class).getId();
        SuitCreateDTO suitCreateDTO = dataTable.asList(SuitCreateDTO.class).get(FIRST_INDEX);
        suitCreateDTO.setTags(testContext.getAndDeleteTestDTO(HashSet.class));
        testContext.setTestDTO(suitCreateDTO);
        String suitCreateDTOAsString = mapper.writeValueAsString(suitCreateDTO);
        RestResponse response = createSuit.call(
            requestDataAndToken(
                d -> {
                    d.pathParams.add("projectId", projectId.toString());
                    d.body = suitCreateDTOAsString;
                }, testContext.getToken()));
        testContext.setResponse(response);
    }

    @When("^I get suit by id$")
    public void iGetSuit() {
        Long projectId = testContext.getTestDTO(ProjectDTO.class).getId();
        Long suitId = testContext.getTestDTO(SuitDTO.class).getId();
        RestResponse response = getProjectSuit.call(
            requestDataAndToken(
                d -> {
                    d.pathParams.add("projectId", projectId.toString());
                    d.pathParams.add("suitId", suitId.toString());
                },
                testContext.getToken()
            )
        );
        testContext.setResponse(response);
    }

    @When("^I update suit$")
    public void iUpdateSuit(DataTable dataTable) throws Throwable {
        Long projectId = testContext.getTestDTO(ProjectDTO.class).getId();
        Long suitId = testContext.getTestDTO(SuitDTO.class).getId();
        SuitUpdateDTO suitUpdateDTO = dataTable.asList(SuitUpdateDTO.class).get(FIRST_INDEX);
        suitUpdateDTO.setTags(testContext.getAndDeleteTestDTO(HashSet.class));

        String suitUpdateDTOAsString = mapper.writeValueAsString(suitUpdateDTO);

        testContext.setTestDTO(suitUpdateDTO);

        RestResponse response = updateSuit.call(
            requestDataAndToken(
                d -> {
                    d.pathParams.add("projectId", projectId.toString());
                    d.pathParams.add("suitId", suitId.toString());
                    d.body = suitUpdateDTOAsString;
                },
                testContext.getToken()
            )
        );
        testContext.setResponse(response);
    }

    @When("^I delete suit$")
    public void iDeleteSuit() {
        Long projectId = testContext.getTestDTO(ProjectDTO.class).getId();
        Long suitId = testContext.getTestDTO(SuitDTO.class).getId();
        RestResponse response = removeSuit.call(
            requestDataAndToken(
                d -> {
                    d.pathParams.addOrReplace("projectId", projectId.toString());
                    d.pathParams.addOrReplace("suitId", suitId.toString());
                },
                testContext.getToken()
            )
        );
        testContext.setResponse(response);
    }

}
