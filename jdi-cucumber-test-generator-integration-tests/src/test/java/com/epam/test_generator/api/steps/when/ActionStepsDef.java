package com.epam.test_generator.api.steps.when;

import static com.epam.http.requests.ServiceInit.init;
import static com.epam.test_generator.api.ApiTokenInserter.requestBodyAndToken;
import static com.epam.test_generator.api.ApiTokenInserter.requestDataAndToken;
import static com.epam.test_generator.api.BddGeneratorApi.addCaseToSuit;
import static com.epam.test_generator.api.BddGeneratorApi.createProject;
import static com.epam.test_generator.api.BddGeneratorApi.createSuit;
import static com.epam.test_generator.api.BddGeneratorApi.getProjectSuit;
import static com.epam.test_generator.api.BddGeneratorApi.updateCase;

import com.epam.http.response.RestResponse;
import com.epam.test_generator.api.BddGeneratorApi;
import com.epam.test_generator.api.steps.StepBackground;
import com.epam.test_generator.controllers.caze.request.CaseCreateDTO;
import com.epam.test_generator.controllers.caze.request.CaseUpdateDTO;
import com.epam.test_generator.controllers.caze.response.CaseDTO;
import com.epam.test_generator.controllers.project.request.ProjectCreateDTO;
import com.epam.test_generator.controllers.project.response.ProjectDTO;
import com.epam.test_generator.controllers.suit.request.SuitCreateDTO;
import com.epam.test_generator.controllers.suit.response.SuitDTO;
import com.epam.test_generator.entities.Status;
import cucumber.api.java.en.When;
import java.util.List;


public class ActionStepsDef extends StepBackground {




    @When("^I set status '(.+)' for case$")
    public void iSetCaseStatus(String status) throws Throwable {
        status = status.toUpperCase().replace(' ', '_');

        CaseUpdateDTO dto = new CaseUpdateDTO();
        dto.setStatus(Status.valueOf(status));

        String dtoString = mapper.writeValueAsString(dto);
        Long projectId = testContext.getTestDTO(ProjectDTO.class).getId();
        Long suitId = testContext.getTestDTO(SuitDTO.class).getId();
        Long caseId = testContext.getTestDTO(CaseDTO.class).getId();

        RestResponse response = updateCase.call(requestDataAndToken(d -> {
            d.pathParams.add("projectId", projectId.toString());
            d.pathParams.add("suitId", suitId.toString());
            d.pathParams.add("caseId", caseId.toString());
            d.body = dtoString;
        }, testContext.getToken()));

        testContext.setResponse(response);
    }

    @When("^I set status '(.+)' for all cases$")
    public void iSetStatusForAllCases(String status) throws Throwable {
        setStatusForSeveralCases(status, getCasesOfLastSuit());
    }

    @When("^I set status '(.+)' for (\\d+) cases$")
    public void iSetStatusForSeveralCases(String status, int numberOfCases) throws Throwable {
        setStatusForSeveralCases(status, getCasesOfLastSuit().subList(0, numberOfCases));
    }


    private List<CaseDTO> getCasesOfLastSuit() throws Throwable {
        Long projectId = testContext.getTestDTO(ProjectDTO.class).getId();
        Long suitId = testContext.getTestDTO(SuitDTO.class).getId();

        RestResponse response = getProjectSuit.call(requestDataAndToken(d -> {
            d.pathParams.add("projectId", projectId.toString());
            d.pathParams.add("suitId", suitId.toString());
        }, testContext.getToken()));

        SuitDTO actualSuitDTO = mapper.readValue(response.raResponse().print(), SuitDTO.class);

        return actualSuitDTO.getCases();
    }

    private void setStatusForSeveralCases(String status, List<CaseDTO> cases) throws Throwable {
        String mappedStatus = status.toUpperCase().replace(' ', '_');
        Long projectId = testContext.getTestDTO(ProjectDTO.class).getId();
        Long suitId = testContext.getTestDTO(SuitDTO.class).getId();

        for (CaseDTO caseDTO : cases) {
            CaseUpdateDTO dto = new CaseUpdateDTO();
            dto.setStatus(Status.valueOf(mappedStatus));
            String dtoString = mapper.writeValueAsString(dto);

            Long caseId = caseDTO.getId();
            updateCase.call(requestDataAndToken(d -> {
                d.pathParams.add("projectId", projectId.toString());
                d.pathParams.add("suitId", suitId.toString());
                d.pathParams.add("caseId", caseId.toString());
                d.body = dtoString;
            }, testContext.getToken()));
            init(BddGeneratorApi.class);
        }
    }
}
