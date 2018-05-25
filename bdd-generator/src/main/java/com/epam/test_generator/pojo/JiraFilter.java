package com.epam.test_generator.pojo;

import java.util.Map;
import java.util.Objects;
import net.rcarz.jiraclient.Field;
import net.rcarz.jiraclient.RestClient;
import net.rcarz.jiraclient.User;
import net.sf.json.JSONObject;

public class JiraFilter {

    private static final String SELF = "self";
    private static final String FILTER_ID = "id";
    private static final String FILTER_NAME = "name";
    private static final String OWNER = "owner";


    private String self;
    private String id;
    private String name;
    private String owner;



    public JiraFilter(RestClient restClient, JSONObject json) {
        JSONObject jsonObject = Objects.requireNonNull(json);
        deserialize(jsonObject, restClient);
    }


    public JiraFilter() {
    }

    private void deserialize(JSONObject jsonObject, RestClient restClient) {

        self = Field.getString(((Map<?, ?>) jsonObject).get(SELF));
        id = Field.getString(((Map<?, ?>) jsonObject).get(FILTER_ID));
        name = Field.getString(((Map<?, ?>) jsonObject).get(FILTER_NAME));
        owner = Field.getResource(User.class, ((Map<?, ?>) jsonObject).get(OWNER), restClient)
            .getName();

    }

    public String getSelf() {
        return self;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getOwner() {
        return owner;
    }
}
