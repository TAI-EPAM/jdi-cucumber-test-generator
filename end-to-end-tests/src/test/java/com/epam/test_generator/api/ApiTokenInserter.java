package com.epam.test_generator.api;

import static com.epam.http.requests.RequestData.requestBody;
import static com.epam.http.requests.RequestData.requestData;
import static com.epam.http.requests.RequestData.requestParams;

import com.epam.commons.linqinterfaces.JActionT;
import com.epam.http.requests.RequestData;

public class ApiTokenInserter {

    private static final String AUTHORIZATION_PARAMETER_NAME = "token";

    public static RequestData requestDataAndToken(JActionT<RequestData> valueFunc, String token) {
        RequestData data = requestData(valueFunc);
        addTokenToQuery(data, token);
        return data;
    }

    public static RequestData requestBodyAndToken(String body, String token) {
        RequestData data = requestBody(body);
        addTokenToQuery(data, token);
        return data;
    }

    public static RequestData requestParamsAndToken(Object[][] params, String token) {
        RequestData data = requestParams(params);
        addTokenToQuery(data, token);
        return data;
    }

    public static RequestData requestParamsAndToken(String paramName, String paramValue,
                                                    String token) {
        RequestData data = requestParams(paramName, paramValue);
        addTokenToQuery(data, token);
        return data;
    }

    public static RequestData requestToken(String token) {
        return new RequestData().set(rd -> addTokenToQuery(rd, token));
    }

    private static void addTokenToQuery(RequestData data, String token) {
        if (token != null) {
            data.queryParams.addOrReplace(AUTHORIZATION_PARAMETER_NAME, token);
        }
    }
}
