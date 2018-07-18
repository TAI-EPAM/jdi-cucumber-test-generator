package com.epam.test_generator.api.steps.when;

import static com.epam.test_generator.api.ApiTokenInserter.requestDataAndToken;
import static com.epam.test_generator.api.BddGeneratorApi.downloadFile;

import com.epam.http.response.RestResponse;
import com.epam.test_generator.api.steps.StepBackground;
import com.epam.test_generator.controllers.project.response.ProjectDTO;
import com.epam.test_generator.dto.wrapper.ListWrapper;
import cucumber.api.java.en.When;

public class FileGenerator extends StepBackground {

    @When("^I download file$")
    public void iDownloadFile() throws Throwable {
        Long projectId = testContext.getAndDeleteTestDTO(ProjectDTO.class).getId();
        ListWrapper featureFileDTOListWrapper = testContext.getAndDeleteTestDTO(ListWrapper.class);
        String featureFileDTOAsString = mapper.writeValueAsString(featureFileDTOListWrapper);
        RestResponse response = downloadFile.call(requestDataAndToken(d -> {
            d.pathParams.add("projectId", projectId.toString());
            d.body = featureFileDTOAsString;
        }, testContext.getToken()));
        testContext.setResponse(response);
    }
}
