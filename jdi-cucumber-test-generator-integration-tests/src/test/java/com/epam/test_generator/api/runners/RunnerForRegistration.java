package com.epam.test_generator.api.runners;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
    strict = true,
    features = "src/test/resources/com/epam/test_generator/api/login/Registration.feature",
    plugin = "com.github.kirlionik.cucumberallure.AllureReporter",
    glue = "com.epam.test_generator.api.steps"
)
public class RunnerForRegistration {

}