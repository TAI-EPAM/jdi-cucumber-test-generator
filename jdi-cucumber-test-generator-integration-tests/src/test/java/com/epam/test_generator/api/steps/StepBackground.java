package com.epam.test_generator.api.steps;

import com.epam.test_generator.api.container.TestContext;
import com.fasterxml.jackson.databind.ObjectMapper;

public class StepBackground {

    protected ObjectMapper mapper = new ObjectMapper();
    protected TestContext testContext = TestContext.getTestContext();
}
