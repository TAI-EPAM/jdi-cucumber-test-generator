package com.epam.test_generator.api.steps.when;

import static com.epam.test_generator.api.ApiTokenInserter.requestDataAndToken;
import static com.epam.test_generator.api.BddGeneratorApi.addCaseToSuit;
import static com.epam.test_generator.api.BddGeneratorApi.getCase;
import static com.epam.test_generator.api.BddGeneratorApi.removeCases;
import static com.epam.test_generator.api.BddGeneratorApi.updateCase;

import com.epam.http.response.RestResponse;
import com.epam.test_generator.api.steps.StepBackground;
import com.epam.test_generator.controllers.caze.request.CaseCreateDTO;
import com.epam.test_generator.controllers.caze.request.CaseUpdateDTO;
import com.epam.test_generator.controllers.caze.response.CaseDTO;
import com.epam.test_generator.controllers.project.response.ProjectDTO;
import com.epam.test_generator.controllers.suit.response.SuitDTO;
import com.epam.test_generator.controllers.tag.response.TagDTO;
import cucumber.api.DataTable;
import cucumber.api.java.en.When;
import java.util.Set;

public class Case extends StepBackground {

    private static final int FIRST_INDEX = 0;

    @When("^I create case$")
    public void iCreateCase(DataTable dataTable) throws Throwable {
        Long projectId = testContext.getTestDTO(ProjectDTO.class).getId();
        Long suitId = testContext.getTestDTO(SuitDTO.class).getId();
        CaseCreateDTO caseCreateDTO = dataTable.asList(CaseCreateDTO.class).get(FIRST_INDEX);
        caseCreateDTO.setTags((Set<TagDTO>)testContext.getAndDeleteCollectionTestDTO(TagDTO.class));
        testContext.setTestDTO(caseCreateDTO);
        String caseCreateDTOAsString = mapper.writeValueAsString(caseCreateDTO);
        RestResponse response = addCaseToSuit.call(
            requestDataAndToken(
                d -> {
                    d.pathParams.add("projectId", projectId.toString());
                    d.pathParams.add("suitId", suitId.toString());
                    d.body = caseCreateDTOAsString;
                }, testContext.getToken()));
        testContext.setResponse(response);
    }

    @When("^I get case by id$")
    public void iGetCase() {
        Long projectId = testContext.getTestDTO(ProjectDTO.class).getId();
        Long suitId = testContext.getTestDTO(SuitDTO.class).getId();
        Long caseId = testContext.getTestDTO(CaseDTO.class).getId();
        RestResponse response = getCase.call(
            requestDataAndToken(
                d -> {
                    d.pathParams.add("projectId", projectId.toString());
                    d.pathParams.add("suitId", suitId.toString());
                    d.pathParams.add("caseId", caseId.toString());
                },
                testContext.getToken()
            )
        );
        testContext.setResponse(response);
    }

    @When("^I update case$")
    public void iUpdateCase(DataTable dataTable) throws Throwable {
        Long projectId = testContext.getTestDTO(ProjectDTO.class).getId();
        Long suitId = testContext.getTestDTO(SuitDTO.class).getId();
        Long caseId = testContext.getTestDTO(CaseDTO.class).getId();
        CaseUpdateDTO caseUpdateDTO = dataTable.asList(CaseUpdateDTO.class).get(FIRST_INDEX);
        caseUpdateDTO.setTags((Set<TagDTO>)testContext.getAndDeleteCollectionTestDTO(TagDTO.class));
        testContext.setTestDTO(caseUpdateDTO);
        String caseUpdateDTOAsString = mapper.writeValueAsString(caseUpdateDTO);
        RestResponse response = updateCase.call(
            requestDataAndToken(
                d -> {
                    d.pathParams.add("projectId", projectId.toString());
                    d.pathParams.add("suitId", suitId.toString());
                    d.pathParams.add("caseId", caseId.toString());
                    d.body = caseUpdateDTOAsString;
                },
                testContext.getToken()
            )
        );
        testContext.setResponse(response);
    }

    @When("^I delete case$")
    public void iDeleteCase() throws Throwable {
        Long projectId = testContext.getTestDTO(ProjectDTO.class).getId();
        Long suitId = testContext.getTestDTO(SuitDTO.class).getId();
        Long[] caseIds = {testContext.getTestDTO(CaseDTO.class).getId()};
        String caseIdsAsString = mapper.writeValueAsString(caseIds);
        RestResponse response = removeCases.call(
            requestDataAndToken(
                d -> {
                    d.pathParams.addOrReplace("projectId", projectId.toString());
                    d.pathParams.addOrReplace("suitId", suitId.toString());
                    d.body = caseIdsAsString;
                },
                testContext.getToken()
            )
        );
        testContext.setResponse(response);
    }

}
