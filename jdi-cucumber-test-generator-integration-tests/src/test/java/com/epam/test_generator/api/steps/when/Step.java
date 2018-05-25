package com.epam.test_generator.api.steps.when;

import static com.epam.test_generator.api.ApiTokenInserter.requestDataAndToken;
import static com.epam.test_generator.api.BddGeneratorApi.addStepToCase;
import static com.epam.test_generator.api.BddGeneratorApi.getStep;
import static com.epam.test_generator.api.BddGeneratorApi.removeCase_1;
import static com.epam.test_generator.api.BddGeneratorApi.updateStep;

import com.epam.http.response.RestResponse;
import com.epam.test_generator.api.steps.StepBackground;
import com.epam.test_generator.api.RestApiFacade;
import com.epam.test_generator.controllers.caze.response.CaseDTO;
import com.epam.test_generator.controllers.project.response.ProjectDTO;
import com.epam.test_generator.controllers.step.request.StepCreateDTO;
import com.epam.test_generator.controllers.step.request.StepUpdateDTO;
import com.epam.test_generator.controllers.step.response.StepDTO;
import com.epam.test_generator.controllers.suit.response.SuitDTO;
import com.epam.test_generator.entities.Status;
import cucumber.api.DataTable;
import cucumber.api.java.en.When;

public class Step extends StepBackground {

    private static final int FIRST_INDEX = 0;
    RestApiFacade restApiFacade = new RestApiFacade();

    @When("^I create step$")
    public void iCreateStep(DataTable dataTable) throws Throwable {
        Long projectId = testContext.getTestDTO(ProjectDTO.class).getId();
        Long suitId = testContext.getTestDTO(SuitDTO.class).getId();
        Long caseId = testContext.getTestDTO(CaseDTO.class).getId();
        StepCreateDTO stepCreateDTO = dataTable.asList(StepCreateDTO.class).get(FIRST_INDEX);
        testContext.setTestDTO(stepCreateDTO);
        String stepCreateDTOAsString = mapper.writeValueAsString(stepCreateDTO);
        RestResponse response = addStepToCase.call(
            requestDataAndToken(
                d -> {
                    d.pathParams.add("projectId", projectId.toString());
                    d.pathParams.add("suitId", suitId.toString());
                    d.pathParams.add("caseId", caseId.toString());
                    d.body = stepCreateDTOAsString;
                }, testContext.getToken()));
        testContext.setResponse(response);
    }

    @When("^I get step by id$")
    public void iGetStep() {
        RestResponse response = restApiFacade.getResponseWithStepFromeContext();
        testContext.setResponse(response);
    }

    @When("^I update step$")
    public void iUpdateStep(DataTable dataTable) throws Throwable {
        Long projectId = testContext.getTestDTO(ProjectDTO.class).getId();
        Long suitId = testContext.getTestDTO(SuitDTO.class).getId();
        Long caseId = testContext.getTestDTO(CaseDTO.class).getId();
        Long stepId = testContext.getTestDTO(StepDTO.class).getId();
        StepUpdateDTO stepUpdateDTO = dataTable.asList(StepUpdateDTO.class).get(FIRST_INDEX);
        testContext.setTestDTO(stepUpdateDTO);
        String stepUpdateDTOAsString = mapper.writeValueAsString(stepUpdateDTO);
        RestResponse response = updateStep.call(
            requestDataAndToken(
                d -> {
                    d.pathParams.add("projectId", projectId.toString());
                    d.pathParams.add("suitId", suitId.toString());
                    d.pathParams.add("caseId", caseId.toString());
                    d.pathParams.add("stepId", stepId.toString());
                    d.body = stepUpdateDTOAsString;
                },
                testContext.getToken()
            )
        );
        testContext.setResponse(response);
    }

    @When("^I delete step$")
    public void iDeleteStep() {
        Long projectId = testContext.getTestDTO(ProjectDTO.class).getId();
        Long suitId = testContext.getTestDTO(SuitDTO.class).getId();
        Long caseId = testContext.getTestDTO(CaseDTO.class).getId();
        Long stepId = testContext.getTestDTO(StepDTO.class).getId();
        RestResponse response = removeCase_1.call(
            requestDataAndToken(
                d -> {
                    d.pathParams.addOrReplace("projectId", projectId.toString());
                    d.pathParams.addOrReplace("suitId", suitId.toString());
                    d.pathParams.addOrReplace("caseId", caseId.toString());
                    d.pathParams.addOrReplace("stepId", stepId.toString());
                },
                testContext.getToken()
            )
        );
        testContext.setResponse(response);
    }

    @When("^I set status '(.+)' for step$")
    public void iSetStatusForStep(String status) throws Throwable {
        String mappedStatus = status.toUpperCase().replace(" ", "_");

        StepUpdateDTO dto = new StepUpdateDTO();
        dto.setStatus(Status.valueOf(mappedStatus));

        String dtoString = mapper.writeValueAsString(dto);

        Long projectId = testContext.getTestDTO(ProjectDTO.class).getId();
        Long suitId = testContext.getTestDTO(SuitDTO.class).getId();
        Long caseId = testContext.getTestDTO(CaseDTO.class).getId();
        RestResponse response = testContext.getResponse();
        StepDTO stepDTO = mapper.readValue(response.raResponse().print(), StepDTO.class);
        Long stepId = stepDTO.getId();
        response = updateStep.call(requestDataAndToken(d -> {
            d.pathParams.add("projectId", projectId.toString());
            d.pathParams.add("suitId", suitId.toString());
            d.pathParams.add("caseId", caseId.toString());
            d.pathParams.add("stepId", stepId.toString());
            d.body = dtoString;
        }, testContext.getToken()));

        testContext.setResponse(response);

        StepDTO actualDto = mapper.readValue(response.raResponse().print(), StepDTO.class);
        testContext.setTestDTO(actualDto);
    }

}
