package com.epam.test_generator.dao.impl;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.epam.test_generator.entities.factory.JiraClientFactory;
import com.epam.test_generator.pojo.JiraFilter;
import com.epam.test_generator.services.exceptions.JiraRuntimeException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import net.rcarz.jiraclient.JiraClient;
import net.rcarz.jiraclient.RestClient;
import net.rcarz.jiraclient.RestException;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.collections.map.ListOrderedMap;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class JiraFilterDAOTest {


    private static final String SELF = "self";
    private static final String FILTER_ID = "75259";
    private static final String NAME = "name";
    private static final String PHOTO = "photo";
    private static final String FILTER_NAME = "MyOwnTestFilter";
    private static final String USER_NAME = "User_Userov";
    private static final String JQL = "project = PROJECTKEY AND resolution = Closed ORDER BY "
            + "priority DESC, updated DESC";
    private static final Long JIRA_SETTINGS_ID = 1L;

    @Mock
    private RestClient restClient;

    @Mock
    private JiraClientFactory jiraClientFactory;

    @Mock
    private JiraClient jiraClient;

    @InjectMocks
    private JiraFilterDAO jiraFilterDAO;

    private JSONArray response;


    @Before
    public void setUp() {
        response = new JSONArray();
        response.add(createJSONFilter());
        when(jiraClientFactory.getJiraClient(JIRA_SETTINGS_ID)).thenReturn(jiraClient);
        when(jiraClient.getRestClient()).thenReturn(restClient);
    }

    @Test
    public void getFilters_Success() throws URISyntaxException, IOException, RestException {

        URI str = new URI("str");


        when(restClient.buildURI(anyString())).thenReturn(str);
        when(restClient.get(str)).thenReturn(response);
        List<JiraFilter> filters = jiraFilterDAO.getFilters(JIRA_SETTINGS_ID);

        assertThat(filters.size(), is(equalTo(1)));

        JiraFilter jiraFilter = filters.get(0);

        assertThat(jiraFilter.getId(), is(equalTo(FILTER_ID)));
        assertThat(jiraFilter.getName(), is(equalTo(FILTER_NAME)));
        assertThat(jiraFilter.getOwner(), is(equalTo(USER_NAME)));
    }

    @Test
    public void getFilterById_Success() throws Exception {
        URI str = new URI("str");

        when(restClient.buildURI(anyString())).thenReturn(str);
        when(restClient.get(str)).thenReturn((JSONObject) response.get(0));

        String jql = jiraFilterDAO.getFilterByFilterId(JIRA_SETTINGS_ID, "42");

        assertThat(jql, is(equalTo(JQL)));
    }

    @Test(expected = JiraRuntimeException.class)
    public void getFilters_RuntimeException_unableToConnect() {
        jiraFilterDAO.getFilters(JIRA_SETTINGS_ID);

    }

    private ListOrderedMap createJSONFilter() {
        ListOrderedMap listOrderedMap = new ListOrderedMap();
        listOrderedMap.put(SELF, SELF);
        listOrderedMap.put("id", FILTER_ID);
        listOrderedMap.put(NAME, "MyOwnTestFilter");
        JSONObject owner = new JSONObject();
        owner.put(SELF, SELF);
        owner.put("key", "user_userov");
        owner.put(NAME, USER_NAME);
        JSONObject avatarUrls = new JSONObject();
        avatarUrls.put("16x16",
                "photo");
        avatarUrls.put("24x24", PHOTO);
        avatarUrls.put("32x32", PHOTO);
        avatarUrls.put("48x48", PHOTO);
        listOrderedMap.put(NAME, FILTER_NAME);
        owner.put("avatarUrls", avatarUrls);
        owner.put("displayName", "User Userov");
        owner.put("active", true);
        listOrderedMap.put("owner", owner);
        listOrderedMap.put("jql", JQL);
        return listOrderedMap;
    }
}