package com.epam.test_generator;

import static com.epam.http.requests.RequestData.requestBody;
import static com.epam.http.requests.ServiceInit.init;
import static com.epam.test_generator.api.BddGeneratorApi.registerUserAccountUsingPOST;
import static org.junit.Assume.assumeNoException;

import com.epam.test_generator.api.BddGeneratorApi;
import com.epam.test_generator.api.runners.RunnerForAuthentification;
import com.epam.test_generator.api.runners.RunnerForCrud;
import com.epam.test_generator.api.runners.RunnerForOther;
import com.epam.test_generator.api.runners.RunnerForRegistration;
import org.junit.BeforeClass;
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
    @BeforeClass
    public static void checkConnection(){
        init(BddGeneratorApi.class);
        try {
            registerUserAccountUsingPOST.call(requestBody(""));
        } catch (RuntimeException e) {
            assumeNoException("The application isn't running!", e);
        }
    }
}
