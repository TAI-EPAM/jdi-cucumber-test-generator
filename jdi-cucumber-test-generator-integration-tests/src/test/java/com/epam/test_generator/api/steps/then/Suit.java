package com.epam.test_generator.api.steps.then;

import static com.epam.test_generator.api.ApiTokenInserter.requestDataAndToken;
import static com.epam.test_generator.api.BddGeneratorApi.getProjectSuit;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import com.epam.http.response.RestResponse;
import com.epam.test_generator.api.steps.StepBackground;
import com.epam.test_generator.controllers.project.response.ProjectDTO;
import com.epam.test_generator.controllers.suit.request.SuitCreateDTO;
import com.epam.test_generator.controllers.suit.request.SuitUpdateDTO;
import com.epam.test_generator.controllers.suit.response.SuitDTO;
import com.epam.test_generator.dto.ValidationErrorsDTO;
import cucumber.api.java.en.Then;

public class Suit extends StepBackground {

    @Then("^The suit should be created$")
    public void theSuitShouldBeCreated() throws Throwable {
        RestResponse response = testContext.getResponse();
        SuitDTO actualSuitDTO = mapper.readValue(response.raResponse().print(), SuitDTO.class);
        SuitCreateDTO expectedSuitDTO = testContext.getAndDeleteTestDTO(SuitCreateDTO.class);

        assertThat(response.raResponse().getStatusCode(), equalTo(201));
        assertThat(actualSuitDTO.getName(), equalTo(expectedSuitDTO.getName()));
        assertThat(actualSuitDTO.getDescription(), equalTo(expectedSuitDTO.getDescription()));
        assertThat(actualSuitDTO.getPriority(), equalTo(expectedSuitDTO.getPriority()));
        assertThat(actualSuitDTO.getTags(), equalTo(expectedSuitDTO.getTags()));
    }

    @Then("^The suit shouldn't be created$")
    public void theSuitShouldntBeCreated() throws Throwable {
        RestResponse response = testContext.getResponse();
        ValidationErrorsDTO actualProjectDTO = mapper
            .readValue(response.raResponse().print(), ValidationErrorsDTO.class);

        assertThat(response.raResponse().getStatusCode(), equalTo(400));
        assertThat(actualProjectDTO.getError(), equalTo(true));
    }

    @Then("^The suit should be returned$")
    public void theSuitShouldBeReturned() throws Throwable {
        RestResponse response = testContext.getResponse();
        SuitDTO actualSuitDTO = mapper.readValue(response.raResponse().print(), SuitDTO.class);
        SuitDTO expectedSuitDTO = testContext.getAndDeleteTestDTO(SuitDTO.class);

        assertThat(response.raResponse().getStatusCode(), equalTo(200));
        assertThat(actualSuitDTO.getId(), equalTo(expectedSuitDTO.getId()));
        assertThat(actualSuitDTO.getName(), equalTo(expectedSuitDTO.getName()));
        assertThat(actualSuitDTO.getDescription(), equalTo(expectedSuitDTO.getDescription()));
        assertThat(actualSuitDTO.getPriority(), equalTo(expectedSuitDTO.getPriority()));
        assertThat(actualSuitDTO.getCreationDate(), equalTo(expectedSuitDTO.getCreationDate()));
        assertThat(actualSuitDTO.getDisplayedStatusName(),
            equalTo(expectedSuitDTO.getDisplayedStatusName()));
        assertThat(actualSuitDTO.getUpdateDate(), equalTo(expectedSuitDTO.getUpdateDate()));
        assertThat(actualSuitDTO.getRowNumber(), equalTo(expectedSuitDTO.getRowNumber()));
        assertThat(actualSuitDTO.getCases(), equalTo(expectedSuitDTO.getCases()));
        assertThat(actualSuitDTO.getTags(), equalTo(expectedSuitDTO.getTags()));
    }

    @Then("^The suit shouldn't be founded$")
    public void theSuitShouldntBeFounded() {
        RestResponse response = testContext.getResponse();
        assertThat(response.raResponse().getStatusCode(), equalTo(404));
        assertThat(response.raResponse().print(), equalTo(""));
    }

    @Then("^The suit should be updated$")
    public void theSuitShouldBeUpdated() throws Throwable {
        RestResponse response = testContext.getResponse();
        Long projectId = testContext.getAndDeleteTestDTO(ProjectDTO.class).getId();
        SuitDTO oldSuitDTO = testContext.getAndDeleteTestDTO(SuitDTO.class);
        SuitUpdateDTO suitUpdateDTO = testContext.getAndDeleteTestDTO(SuitUpdateDTO.class);
        RestResponse responseNewSuit = getProjectSuit.call(
            requestDataAndToken(
                d -> {
                    d.pathParams.add("projectId", projectId.toString());
                    d.pathParams.add("suitId", oldSuitDTO.getId().toString());
                },
                testContext.getToken()
            )
        );
        SuitDTO actualSuitDTO = mapper
            .readValue(responseNewSuit.raResponse().print(), SuitDTO.class);

        assertThat(response.raResponse().getStatusCode(), equalTo(200));
        assertThat(actualSuitDTO.getId(), equalTo(oldSuitDTO.getId()));
        assertThat(actualSuitDTO.getName(), equalTo(suitUpdateDTO.getName()));
        assertThat(actualSuitDTO.getDescription(), equalTo(suitUpdateDTO.getDescription()));
        assertThat(actualSuitDTO.getPriority(), equalTo(suitUpdateDTO.getPriority()));
        assertThat(actualSuitDTO.getCreationDate(), equalTo(oldSuitDTO.getCreationDate()));
        assertThat(actualSuitDTO.getDisplayedStatusName(),
            equalTo(oldSuitDTO.getDisplayedStatusName()));
        assertThat(actualSuitDTO.getRowNumber(), equalTo(oldSuitDTO.getRowNumber()));
        assertThat(actualSuitDTO.getCases(), equalTo(oldSuitDTO.getCases()));
        assertThat(actualSuitDTO.getTags(), equalTo(suitUpdateDTO.getTags()));
    }

    @Then("^The suit shouldn't be updated$")
    public void theSuitShouldntBeUpdated() throws Throwable {
        RestResponse response = testContext.getResponse();
        ValidationErrorsDTO actualProjectDTO = mapper
            .readValue(response.raResponse().print(), ValidationErrorsDTO.class);

        assertThat(response.raResponse().getStatusCode(), equalTo(400));
        assertThat(actualProjectDTO.getError(), equalTo(true));
    }

    @Then("^The suit should be deleted$")
    public void theSuitShouldBeDeleted() throws Throwable {
        Long projectId = testContext.getTestDTO(ProjectDTO.class).getId();
        Long suitId = testContext.getTestDTO(SuitDTO.class).getId();

        RestResponse oldSuit = getProjectSuit.call(
            requestDataAndToken(
                d -> {
                    d.pathParams.add("projectId", projectId.toString());
                    d.pathParams.add("suitId", suitId.toString());
                },
                testContext.getToken()
            )
        );
        assertThat(oldSuit.raResponse().getStatusCode(), equalTo(404));
        theSuitShouldBeReturned();
    }

}
