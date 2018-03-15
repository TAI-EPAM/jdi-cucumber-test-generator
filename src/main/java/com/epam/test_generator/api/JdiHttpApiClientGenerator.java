package com.epam.test_generator.api;

import com.epam.http.annotations.*;
import com.epam.http.requests.RestMethod;
import com.squareup.javapoet.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import v2.io.swagger.models.HttpMethod;
import v2.io.swagger.models.Swagger;
import v2.io.swagger.parser.SwaggerParser;
import javax.lang.model.element.Modifier;
import java.io.IOException;
import java.io.PrintStream;

import static io.restassured.http.ContentType.JSON;

/**
 * The prototype generator of BDD Api to JDI HTTP client java class
 */
public class JdiHttpApiClientGenerator {

    public static final String API_CLASS_NAME = "BddGeneratorApi";
    private static final String API_PACKAGE_NAME = "com.epam.test_generator.api";

    private static final String AUTHORIZATION_PARAMETER_NAME = "Authorization";
    private static final String AUTHORIZATION_TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiQURNSU4iLCJpc3MiOiJjdWN1bWJlciIsImlkIjo1LCJnaXZlbl9uYW1lIjoiYWRtaW5OYW1lIiwiZmFtaWx5X25hbWUiOiJhZG1pblN1cm5hbWUiLCJlbWFpbCI6ImFkbWluQG1haWwuY29tIn0.0pF_BkGW_1NdihCY4J-7wk7iRkLJs9vMzF-T8zHXFcA";

    private static final Logger logger = LoggerFactory.getLogger(JdiHttpApiClientGenerator.class);


    /**
     * Generate JDI HTTP API model based on external Swagger API model
     *
     * @param urlApi location swagger model (http or local source)
     * @param file full name of the created file
     * @throws IOException parse file exception
     */
    protected void generateApiClass(String urlApi, String file) throws IOException {

        logger.trace("Swagger read api from url: " + urlApi);
        Swagger swagger = new SwaggerParser().read(urlApi);

        if (swagger == null) {
            throw new IOException("IncorrectURL Exception");
        }

        final String serviceDomain = "http://" + swagger.getHost() + swagger.getBasePath();

        TypeSpec.Builder apiClassBuilder = getBasicClassBuilder(serviceDomain);

        FieldSpec authParameterName = createConstantFieldSpec("AUTHORIZATION_PARAMETER_NAME", AUTHORIZATION_PARAMETER_NAME);
        apiClassBuilder.addField(authParameterName);

        FieldSpec authToken = createConstantFieldSpec("AUTHORIZATION_TOKEN", AUTHORIZATION_TOKEN);
        apiClassBuilder.addField(authToken);

        logger.trace("Swagger get all endpoints from BDD api");
        addApiMethodFields(swagger, apiClassBuilder, authParameterName, authToken);

        JavaFile javaFile = JavaFile.builder(API_PACKAGE_NAME, apiClassBuilder.build())
            .addStaticImport(JSON)
            .indent("\t")
            .build();

        logger.trace("Create java api class file");
        PrintStream stream = new PrintStream(file, "UTF-8");
        javaFile.writeTo(stream);
    }


    private void addApiMethodFields(Swagger swagger, TypeSpec.Builder apiClassBuilder,
                                    FieldSpec authParameterName, FieldSpec authToken) {

        swagger.getPaths()
            // get all endpoint's operations
            .forEach((endPointName, value) -> value.getOperationMap()
                // create method for each http operation
                .forEach((httpMethod, operation) -> {

                    FieldSpec getMethodSpec = createStaticRestMethodSpec(operation.getOperationId(),
                                            endPointName, httpMethod, authParameterName, authToken);

                    apiClassBuilder.addField(getMethodSpec);

                }));
    }


    private TypeSpec.Builder getBasicClassBuilder(String serviceDomain) {

        return TypeSpec.classBuilder(API_CLASS_NAME)
            .addAnnotation(AnnotationSpec.builder(ServiceDomain.class)
                .addMember("value", "$S", serviceDomain)
                .build())
            .addJavadoc("Represents the EPAM JDI Http Client for BBD Generator REST API.\n" +
                "Automatically generated from BDD Generator's Swagger OpenAPI Specification.\n")
            .addModifiers(Modifier.PUBLIC);
    }


    private FieldSpec createConstantFieldSpec(String fieldName, String fieldValue) {

        FieldSpec field = FieldSpec
            .builder(String.class, fieldName)
            .addModifiers(Modifier.PRIVATE, Modifier.FINAL, Modifier.STATIC)
            .initializer("$S", fieldValue)
            .build();
        return field;
    }


    private FieldSpec createStaticRestMethodSpec(String methodName, String endpoint, HttpMethod httpMethod,
                                                 FieldSpec authParameterName, FieldSpec authToken) {

        FieldSpec methodSpec = FieldSpec.builder(RestMethod.class, methodName)
            .addModifiers(Modifier.STATIC)
            .addAnnotation(AnnotationSpec.builder(Header.class)
                .addMember("name", "$N", authParameterName)
                .addMember("value", "$N", authToken)
                .build())
            .addAnnotation(AnnotationSpec.builder(ContentType.class)
                .addMember("value", "$L", JSON.name())
                .build())
            .addAnnotation(AnnotationSpec.builder(getClassFromHttpMethod(httpMethod))
                .addMember("value", "$S", endpoint)
                .build())
            .build();

        return methodSpec;
    }

    private Class getClassFromHttpMethod(HttpMethod httpMethod){

        Class httpMethodClass;

        switch (httpMethod) {
            case GET:
                httpMethodClass = GET.class;
                break;
            case POST:
                httpMethodClass = POST.class;
                break;
            case PUT:
                httpMethodClass = PUT.class;
                break;
            case DELETE:
                httpMethodClass = DELETE.class;
                break;
            default:
                throw new IllegalArgumentException(
                    "Swagger API has undefined HTTP method");
        }

        return httpMethodClass;
    }
}
