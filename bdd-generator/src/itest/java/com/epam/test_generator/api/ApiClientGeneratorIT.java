package com.epam.test_generator.api;

import static com.epam.test_generator.api.JdiHttpApiClientGenerator.API_CLASS_NAME;

import java.io.File;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;
import java.io.IOException;

public class ApiClientGeneratorIT {

    //private static final String SWAGGER_API_URL = "http://localhost:8080/v2/api-docs";
    private static final String SWAGGER_API_URL = "/swagger-api.json";
    private static final String API_FILE = "src/itest/java/com/epam/test_generator/api/"
        + API_CLASS_NAME
        +".java";

    @AfterClass
    public static void deleteApiFile() {
        File file = new File(API_FILE);
        file.delete();
    }

    @Test
    public void generateApiClass_CorrectURL_FileGenerated() throws IOException {
        File file = new File(API_FILE);

        Assert.assertFalse(file.exists());

        new JdiHttpApiClientGenerator().generateApiClass(SWAGGER_API_URL, API_FILE);

        Assert.assertTrue(file.exists());
    }

    @Test(expected = IOException.class)
    public void generateApiClass_IncorrectURL_Exception() throws IOException {
        new JdiHttpApiClientGenerator().generateApiClass("http://localhost:8080/bad-url", API_FILE);
    }
}
