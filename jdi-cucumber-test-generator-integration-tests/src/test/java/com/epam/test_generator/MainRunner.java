package com.epam.test_generator;

import com.epam.test_generator.api.runners.RunnerForAuthentification;
import com.epam.test_generator.api.runners.RunnerForCrud;
import com.epam.test_generator.api.runners.RunnerForRegistration;
import com.epam.test_generator.api.runners.RunnerForOther;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
    RunnerForRegistration.class,
    RunnerForAuthentification.class,
    RunnerForCrud.class,
    RunnerForOther.class
})
public class MainRunner {

}
