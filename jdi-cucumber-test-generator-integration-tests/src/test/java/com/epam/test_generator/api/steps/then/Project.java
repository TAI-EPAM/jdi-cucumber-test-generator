package com.epam.test_generator.api.steps.then;

import static com.epam.test_generator.api.ApiTokenInserter.requestDataAndToken;
import static com.epam.test_generator.api.BddGeneratorApi.getProject;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import com.epam.http.response.RestResponse;
import com.epam.test_generator.api.steps.StepBackground;
import com.epam.test_generator.controllers.project.request.ProjectCreateDTO;
import com.epam.test_generator.controllers.project.request.ProjectUpdateDTO;
import com.epam.test_generator.controllers.project.response.ProjectDTO;
import com.epam.test_generator.controllers.project.response.ProjectFullDTO;
import com.epam.test_generator.dto.ValidationErrorsDTO;
import cucumber.api.java.en.Then;

public class Project extends StepBackground {

    @Then("^The project should be created$")
    public void theProjectShouldBeCreated() throws Throwable {
        RestResponse response = testContext.getResponse();
        ProjectDTO actualProjectDTO = mapper
            .readValue(response.raResponse().print(), ProjectDTO.class);
        ProjectCreateDTO expectedProjectDTO = testContext
            .getAndDeleteTestDTO(ProjectCreateDTO.class);

        assertThat(response.raResponse().getStatusCode(), equalTo(201));
        assertThat(actualProjectDTO.getName(), equalTo(expectedProjectDTO.getName()));
        assertThat(actualProjectDTO.getDescription(), equalTo(expectedProjectDTO.getDescription()));
    }

    @Then("^The project shouldn't be created$")
    public void theProjectShouldntBeCreated() throws Throwable {
        RestResponse response = testContext.getResponse();
        ValidationErrorsDTO actualProjectDTO = mapper
            .readValue(response.raResponse().print(), ValidationErrorsDTO.class);

        assertThat(response.raResponse().getStatusCode(), equalTo(400));
        assertThat(actualProjectDTO.getError(), equalTo(true));
    }

    @Then("^The project should be returned$")
    public void theProjectShouldBeReturned() throws Throwable {
        RestResponse response = testContext.getResponse();
        ProjectFullDTO actualProjectDTO = mapper
            .readValue(response.raResponse().print(), ProjectFullDTO.class);
        ProjectDTO expectedProjectDTO = testContext.getAndDeleteTestDTO(ProjectDTO.class);

        assertThat(response.raResponse().getStatusCode(), equalTo(200));
        assertThat(actualProjectDTO.getId(), equalTo(expectedProjectDTO.getId()));
        assertThat(actualProjectDTO.getName(), equalTo(expectedProjectDTO.getName()));
        assertThat(actualProjectDTO.getDescription(), equalTo(expectedProjectDTO.getDescription()));
    }

    @Then("^The project shouldn't be founded$")
    public void theProjectShouldntBeFounded() {
        RestResponse response = testContext.getResponse();
        assertThat(response.raResponse().getStatusCode(), equalTo(403));
    }

    @Then("^The project should be updated$")
    public void theProjectShouldBeUpdated() throws Throwable {
        RestResponse response = testContext.getResponse();
        Long projectId = testContext.getAndDeleteTestDTO(ProjectDTO.class).getId();
        ProjectUpdateDTO expectedProjectDTO = testContext
            .getAndDeleteTestDTO(ProjectUpdateDTO.class);
        RestResponse responseNewProject = getProject.call(
            requestDataAndToken(
                d -> d.pathParams.add("projectId", projectId.toString()),
                testContext.getToken()
            )
        );
        ProjectFullDTO actualProjectDTO = mapper.readValue(responseNewProject.raResponse().print(),
            ProjectFullDTO.class);

        assertThat(response.raResponse().getStatusCode(), equalTo(200));
        assertThat(actualProjectDTO.getId(), equalTo(projectId));
        assertThat(actualProjectDTO.getName(), equalTo(expectedProjectDTO.getName()));
        assertThat(actualProjectDTO.getDescription(), equalTo(expectedProjectDTO.getDescription()));
    }

    @Then("^The project shouldn't be updated$")
    public void theProjectShouldntBeUpdated() throws Throwable {
        RestResponse response = testContext.getResponse();
        ValidationErrorsDTO actualProjectDTO = mapper
            .readValue(response.raResponse().print(), ValidationErrorsDTO.class);

        assertThat(response.raResponse().getStatusCode(), equalTo(400));
        assertThat(actualProjectDTO.getError(), equalTo(true));
    }

    @Then("^The project should be closed$")
    public void theProjectShouldBeClosed() throws Throwable {
        Long projectId = testContext.getAndDeleteTestDTO(ProjectDTO.class).getId();
        RestResponse response = testContext.getResponse();
        assertThat(response.raResponse().getStatusCode(), equalTo(200));
        assertThat(response.raResponse().print(), equalTo(""));

        RestResponse responseNewProject = getProject.call(
            requestDataAndToken(
                d -> d.pathParams.add("projectId", projectId.toString()),
                testContext.getToken()
            )
        );
        ProjectFullDTO projectFullDTO = mapper
            .readValue(responseNewProject.raResponse().print(), ProjectFullDTO.class);
        assertThat(responseNewProject.raResponse().getStatusCode(), equalTo(200));
        assertThat(projectFullDTO.isActive(), equalTo(false));
    }

}
