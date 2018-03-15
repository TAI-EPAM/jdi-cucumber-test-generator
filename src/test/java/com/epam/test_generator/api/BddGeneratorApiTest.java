package com.epam.test_generator.api;

import com.epam.http.response.ResponseStatusType;
import com.epam.http.response.RestResponse;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import static com.epam.http.requests.RequestData.requestData;
import static com.epam.http.requests.ServiceInit.init;
import static com.epam.test_generator.api.BddGeneratorApi.*;
import static java.lang.String.format;
import static org.hamcrest.core.IsCollectionContaining.hasItem;

@Ignore
public class BddGeneratorApiTest {

    private final String projectName = "TestProject";

    @BeforeClass
    public static void initService() {
        init(BddGeneratorApi.class);
    }

    @Test
    public void createProject_StatusOk() {
        RestResponse response = createProject
                .call(requestData(d -> {
                    d.body = format("{\"name\": \"%s\"}", projectName);}
                ));
        response.isStatus(ResponseStatusType.OK);
    }

    @Test
    public void getUserProjects_StatusOkAndContainsProjectWithGivenName() {
        RestResponse response = getUserProjects.call();
        response.isOk().body("name", hasItem(projectName));
    }


}
