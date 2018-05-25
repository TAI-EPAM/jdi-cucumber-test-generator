package com.epam.test_generator.api.container;

import com.epam.http.response.RestResponse;

import java.util.HashMap;
import java.util.Map;

public class TestContext {
    public static final String ADMIN_EMAIL = "admin@mail.com";
    public static final String ADMIN_PASSWORD = "admin";
    public static final String USER_PASSWORD = "qwerty";
    public static final String USER_NAME = "name";
    public static final String USER_SURNAME = "surname";
    private static final String RESPONSE = "response";
    private static TestContext testContext;
    private Map<String, Object> map = new HashMap<>();
    private String token;
    private String adminToken;

    public static TestContext getTestContext() {
        if (testContext == null) {
            testContext = new TestContext();
        }
        return testContext;
    }

    public void setResponse(RestResponse response) {
        map.put(RESPONSE, response);
    }

    public RestResponse getResponse() {
        return (RestResponse) map.get(RESPONSE);
    }

    public <T> void setTestDTO(T object) {
        map.put(object.getClass().getCanonicalName(), object);
    }

    public <T> T getTestDTO(Class<T> objectClass) {
        return (T) map.get(objectClass.getCanonicalName());
    }

    public <T> T getAndDeleteTestDTO(Class<T> objectClass) {
        return (T) map.remove(objectClass.getCanonicalName());
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAdminToken() {
        return adminToken;
    }

    public void setAdminToken(String adminToken) {
        this.adminToken = adminToken;
    }

    public void clear() {
        map.clear();
        token = null;
    }
}