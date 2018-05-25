package com.epam.test_generator.api.steps.when;

import static com.epam.test_generator.api.ApiTokenInserter.requestBodyAndToken;
import static com.epam.test_generator.api.ApiTokenInserter.requestDataAndToken;
import static com.epam.test_generator.api.BddGeneratorApi.closeProject;
import static com.epam.test_generator.api.BddGeneratorApi.createProject;
import static com.epam.test_generator.api.BddGeneratorApi.getProject;
import static com.epam.test_generator.api.BddGeneratorApi.updateProject;

import com.epam.http.response.RestResponse;
import com.epam.test_generator.api.steps.StepBackground;
import com.epam.test_generator.controllers.project.request.ProjectCreateDTO;
import com.epam.test_generator.controllers.project.request.ProjectUpdateDTO;
import com.epam.test_generator.controllers.project.response.ProjectDTO;
import cucumber.api.DataTable;
import cucumber.api.java.en.When;

public class Project extends StepBackground {

    private final static int FIRST_INDEX = 0;

    @When("^I create project$")
    public void iCreateProject(DataTable dataTable) throws Throwable {
        ProjectCreateDTO projectCreateDTO =
            dataTable.asList(ProjectCreateDTO.class).get(FIRST_INDEX);
        testContext.setTestDTO(projectCreateDTO);
        String projectCreateDTOAsString = mapper.writeValueAsString(projectCreateDTO);
        RestResponse response = createProject
            .call(requestBodyAndToken(projectCreateDTOAsString, testContext.getToken()));
        testContext.setResponse(response);
    }

    @When("^I get project by id$")
    public void iGetProject() {
        Long projectId = testContext.getTestDTO(ProjectDTO.class).getId();
        RestResponse response = getProject.call(
            requestDataAndToken(
                d -> d.pathParams.add("projectId", projectId.toString()),
                testContext.getToken()
            )
        );
        testContext.setResponse(response);
    }

    @When("^I update project$")
    public void iUpdateProject(DataTable dataTable) throws Throwable {
        Long projectId = testContext.getTestDTO(ProjectDTO.class).getId();
        ProjectUpdateDTO projectUpdateDTO =
            dataTable.asList(ProjectUpdateDTO.class).get(FIRST_INDEX);
        testContext.setTestDTO(projectUpdateDTO);
        String projectUpdateDTOAsString = mapper.writeValueAsString(projectUpdateDTO);
        RestResponse response = updateProject.call(
            requestDataAndToken(
                d -> {
                    d.pathParams.add("projectId", projectId.toString());
                    d.body = projectUpdateDTOAsString;
                },
                testContext.getToken()
            )
        );
        testContext.setResponse(response);
    }

    @When("^I close project$")
    public void iCloseProject() {
        Long projectId = testContext.getTestDTO(ProjectDTO.class).getId();
        RestResponse response = closeProject.call(
            requestDataAndToken(
                d -> d.pathParams.add("projectId", projectId.toString()),
                testContext.getToken()
            )
        );
        testContext.setResponse(response);
    }

}
