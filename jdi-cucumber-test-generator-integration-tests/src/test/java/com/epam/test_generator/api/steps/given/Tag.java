package com.epam.test_generator.api.steps.given;

import com.epam.test_generator.api.steps.StepBackground;
import com.epam.test_generator.controllers.tag.response.TagDTO;
import cucumber.api.DataTable;
import cucumber.api.java.en.Given;
import java.util.HashSet;

public class Tag extends StepBackground {

    @Given("^I have a list of tags$")
    public void iHaveAListOfTags(DataTable dataTable) {
        testContext.setTestDTO(new HashSet<>(dataTable.asList(TagDTO.class)));
    }

}
