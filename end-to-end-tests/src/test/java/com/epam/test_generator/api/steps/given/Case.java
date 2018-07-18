package com.epam.test_generator.api.steps.given;

import com.epam.test_generator.api.steps.StepBackground;
import com.epam.test_generator.api.RestApiFacade;
import com.epam.test_generator.controllers.caze.request.CaseCreateDTO;
import com.epam.test_generator.controllers.caze.response.CaseDTO;
import cucumber.api.DataTable;
import cucumber.api.java.en.Given;

public class Case extends StepBackground {

    private static final int FIRST_INDEX = 0;
    private RestApiFacade restApiFacade = new RestApiFacade();

    @Given("^I have a case$")
    public void iHaveACase(DataTable dataTable) throws Throwable {
        restApiFacade.addCaseToSuit(dataTable.asList(CaseCreateDTO.class).get(FIRST_INDEX));
    }

    @Given("^I have some cases$")
    public void iHaveSeveralCases(DataTable dataTable) throws Throwable {
        for (CaseCreateDTO caseCreateDTO : dataTable.asList(CaseCreateDTO.class)) {
            restApiFacade.addCaseToSuit(caseCreateDTO);
        }
    }

    @Given("^I have a case that doesn't exist in data base$")
    public void iHaveACaseThatDoesntExist(DataTable dataTable) {
        testContext.setTestDTO(dataTable.asList(CaseDTO.class).get(FIRST_INDEX));
    }
}
