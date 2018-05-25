package com.epam.test_generator.api.steps.then;

import static com.epam.test_generator.api.ApiTokenInserter.requestDataAndToken;
import static com.epam.test_generator.api.BddGeneratorApi.getCase;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import com.epam.http.response.RestResponse;
import com.epam.test_generator.api.steps.StepBackground;
import com.epam.test_generator.controllers.caze.request.CaseCreateDTO;
import com.epam.test_generator.controllers.caze.request.CaseUpdateDTO;
import com.epam.test_generator.controllers.caze.response.CaseDTO;
import com.epam.test_generator.controllers.project.response.ProjectDTO;
import com.epam.test_generator.controllers.suit.response.SuitDTO;
import com.epam.test_generator.dto.ValidationErrorsDTO;
import cucumber.api.java.en.Then;

public class Case extends StepBackground {

    @Then("^The case should be created$")
    public void theCaseShouldBeCreated() throws Throwable {
        RestResponse response = testContext.getResponse();
        CaseDTO actualCaseDTO = mapper.readValue(response.raResponse().print(), CaseDTO.class);
        CaseCreateDTO expectedCaseDTO = testContext.getAndDeleteTestDTO(CaseCreateDTO.class);

        assertThat(response.raResponse().getStatusCode(), equalTo(201));
        assertThat(actualCaseDTO.getName(), equalTo(expectedCaseDTO.getName()));
        assertThat(actualCaseDTO.getDescription(), equalTo(expectedCaseDTO.getDescription()));
        assertThat(actualCaseDTO.getPriority(), equalTo(expectedCaseDTO.getPriority()));
        assertThat(actualCaseDTO.getTags(), equalTo(expectedCaseDTO.getTags()));
        assertThat(actualCaseDTO.getComment(), equalTo(expectedCaseDTO.getComment()));
    }

    @Then("^The case shouldn't be created$")
    public void theCaseShouldntBeCreated() throws Throwable {
        RestResponse response = testContext.getResponse();
        ValidationErrorsDTO actualProjectDTO = mapper
            .readValue(response.raResponse().print(), ValidationErrorsDTO.class);

        assertThat(response.raResponse().getStatusCode(), equalTo(400));
        assertThat(actualProjectDTO.getError(), equalTo(true));
    }

    @Then("^The case should be returned$")
    public void theCaseShouldBeReturned() throws Throwable {
        RestResponse response = testContext.getResponse();
        CaseDTO actualCaseDTO = mapper.readValue(response.raResponse().print(), CaseDTO.class);
        CaseDTO expectedCaseDTO = testContext.getAndDeleteTestDTO(CaseDTO.class);

        assertThat(response.raResponse().getStatusCode(), equalTo(200));
        assertThat(actualCaseDTO.getId(), equalTo(expectedCaseDTO.getId()));
        assertThat(actualCaseDTO.getName(), equalTo(expectedCaseDTO.getName()));
        assertThat(actualCaseDTO.getDescription(), equalTo(expectedCaseDTO.getDescription()));
        assertThat(actualCaseDTO.getPriority(), equalTo(expectedCaseDTO.getPriority()));
        assertThat(actualCaseDTO.getCreationDate(), equalTo(expectedCaseDTO.getCreationDate()));
        assertThat(actualCaseDTO.getDisplayedStatusName(),
            equalTo(expectedCaseDTO.getDisplayedStatusName()));
        assertThat(actualCaseDTO.getUpdateDate(), equalTo(expectedCaseDTO.getUpdateDate()));
        assertThat(actualCaseDTO.getRowNumber(), equalTo(expectedCaseDTO.getRowNumber()));
        assertThat(actualCaseDTO.getSteps(), equalTo(expectedCaseDTO.getSteps()));
        assertThat(actualCaseDTO.getComment(), equalTo(expectedCaseDTO.getComment()));
        assertThat(actualCaseDTO.getTags(), equalTo(expectedCaseDTO.getTags()));
    }

    @Then("^The case shouldn't be founded$")
    public void theCaseShouldntBeFounded() {
        RestResponse response = testContext.getResponse();
        assertThat(response.raResponse().getStatusCode(), equalTo(404));
        assertThat(response.raResponse().print(), equalTo(""));
    }

    @Then("^The case should be updated$")
    public void theCaseShouldBeUpdated() throws Throwable {
        RestResponse response = testContext.getResponse();
        Long projectId = testContext.getAndDeleteTestDTO(ProjectDTO.class).getId();
        Long suitId = testContext.getAndDeleteTestDTO(SuitDTO.class).getId();
        CaseUpdateDTO caseUpdateDTO = testContext.getAndDeleteTestDTO(CaseUpdateDTO.class);
        CaseDTO oldCaseDTO = testContext.getAndDeleteTestDTO(CaseDTO.class);
        RestResponse responseNewCase = getCase.call(
            requestDataAndToken(
                d -> {
                    d.pathParams.add("projectId", projectId.toString());
                    d.pathParams.add("suitId", suitId.toString());
                    d.pathParams.add("caseId", oldCaseDTO.getId().toString());
                },
                testContext.getToken()
            )
        );
        CaseDTO actualCaseDTO = mapper
            .readValue(responseNewCase.raResponse().print(), CaseDTO.class);

        assertThat(response.raResponse().getStatusCode(), equalTo(200));
        assertThat(actualCaseDTO.getId(), equalTo(oldCaseDTO.getId()));
        assertThat(actualCaseDTO.getName(), equalTo(caseUpdateDTO.getName()));
        assertThat(actualCaseDTO.getDescription(), equalTo(caseUpdateDTO.getDescription()));
        assertThat(actualCaseDTO.getPriority(), equalTo(caseUpdateDTO.getPriority()));
        assertThat(actualCaseDTO.getCreationDate(), equalTo(oldCaseDTO.getCreationDate()));
        assertThat(actualCaseDTO.getDisplayedStatusName(),
            equalTo(caseUpdateDTO.getStatus().toString()));
        assertThat(actualCaseDTO.getRowNumber(), equalTo(oldCaseDTO.getRowNumber()));
        assertThat(actualCaseDTO.getSteps(), equalTo(oldCaseDTO.getSteps()));
        assertThat(actualCaseDTO.getComment(), equalTo(caseUpdateDTO.getComment()));
        assertThat(actualCaseDTO.getTags(), equalTo(caseUpdateDTO.getTags()));
    }

    @Then("^The case shouldn't be updated$")
    public void theCaseShouldntBeUpdated() throws Throwable {
        RestResponse response = testContext.getResponse();
        ValidationErrorsDTO actualProjectDTO = mapper
            .readValue(response.raResponse().print(), ValidationErrorsDTO.class);

        assertThat(response.raResponse().getStatusCode(), equalTo(400));
        assertThat(actualProjectDTO.getError(), equalTo(true));
    }

    @Then("^The case should be deleted$")
    public void theCaseShouldBeDeleted() throws Throwable {
        Long projectId = testContext.getTestDTO(ProjectDTO.class).getId();
        Long suitId = testContext.getTestDTO(SuitDTO.class).getId();
        Long caseId = testContext.getTestDTO(CaseDTO.class).getId();

        RestResponse oldCaseResponse = getCase.call(
            requestDataAndToken(
                d -> {
                    d.pathParams.add("projectId", projectId.toString());
                    d.pathParams.add("suitId", suitId.toString());
                    d.pathParams.add("caseId", caseId.toString());
                },
                testContext.getToken()
            )
        );
        assertThat(oldCaseResponse.raResponse().getStatusCode(), equalTo(404));
        theCaseShouldBeReturned();
    }

}
