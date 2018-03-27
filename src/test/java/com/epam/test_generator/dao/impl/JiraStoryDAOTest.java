package com.epam.test_generator.dao.impl;

import com.epam.test_generator.dao.interfaces.JiraSettingsDAO;
import com.epam.test_generator.entities.JiraSettings;
import com.epam.test_generator.entities.Suit;
import com.epam.test_generator.entities.factory.JiraClientFactory;
import com.epam.test_generator.pojo.JiraStory;
import net.rcarz.jiraclient.Issue;
import net.rcarz.jiraclient.JiraClient;
import net.rcarz.jiraclient.JiraException;
import net.rcarz.jiraclient.RestException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class JiraStoryDAOTest {

    @Mock
    private JiraClient client;

    @Mock
    private JiraClientFactory clientFactory;

    @Mock
    private Suit suit;

    @Mock
    private Issue issue;

    @Mock
    private JiraSettingsDAO jiraSettingsDAO;

    private JiraSettings jiraSettings;

    @InjectMocks
    private
    JiraStoryDAO jiraStroryDAO;

    private static final String JIRA_KEY = "key";
    private static final String JIRA_FILTER = "filter";
    private static final Long JIRA_SETTINGS_ID = 1L;

    @Before
    public void setUp() throws Exception {
        when(clientFactory.getJiraClient(JIRA_SETTINGS_ID)).thenReturn(client);
        jiraSettings = new JiraSettings();
        jiraSettings.setLogin("login");
        jiraSettings.setUri("jira_uri");
        jiraSettings.setPassword("password");
        when(jiraSettingsDAO.findById(anyLong())).thenReturn(jiraSettings);
    }

    @Test
    public void getStoryByJiraKey_JiraStory_Success() throws Exception {
        when(client.getIssue(anyString())).thenReturn(issue);

        JiraStory expectedStory = new JiraStory(issue);
        JiraStory resultStory = jiraStroryDAO.getStoryByJiraKey(JIRA_SETTINGS_ID, JIRA_KEY);
        Assert.assertEquals(expectedStory, resultStory);
    }

    @Test(expected = JiraException.class)
    public void getStoryUnvalidJiraKey_JiraStory_MalformedParametersException() throws Exception {
        when(client.getIssue(anyString())).thenThrow(new JiraException(""));
        jiraStroryDAO.getStoryByJiraKey(JIRA_SETTINGS_ID, JIRA_KEY);
    }

    @Test(expected = JiraException.class)
    public void getUnexistedStoryByJiraKey_JiraStory_Null() throws Exception{
        when(client.getIssue(anyString())).thenThrow(new JiraException("", new RestException("",404,"")));
        JiraStory story = jiraStroryDAO.getStoryByJiraKey(JIRA_SETTINGS_ID, JIRA_KEY);
        Assert.assertNull(story);
    }

    @Test
    public void getJiraStiriesByFilter_JiraStories_Success() throws JiraException {
        Issue.SearchResult searchResult = new Issue.SearchResult();
        searchResult.issues = Collections.singletonList(issue);
        when(client.searchIssues(anyString(), anyInt())).thenReturn(searchResult);
        List<JiraStory> expectedStories = Arrays.asList(new JiraStory(issue));

        List<JiraStory> resultStories = jiraStroryDAO.getJiraStoriesByFilter(JIRA_SETTINGS_ID, JIRA_FILTER);

        Assert.assertEquals(expectedStories, resultStories);
    }

    @Test(expected = JiraException.class)
    public void getJiraStoriesByInvalidFilter_JiraStories_MalformedParametersException() throws Exception {
        when(client.searchIssues(anyString(), anyInt())).thenThrow(new JiraException("a"));
        jiraStroryDAO.getJiraStoriesByFilter(JIRA_SETTINGS_ID, JIRA_FILTER);
    }

    @Test(expected = JiraException.class)
    public void getNonexistentJiraStroiesByInvalidFilter_JiraStories_Success() throws JiraException {
        when(client.searchIssues(anyString(), anyInt())).thenThrow(new JiraException("a", new RestException("a", 404, "bad")));
        List<JiraStory> subTasks = jiraStroryDAO.getJiraStoriesByFilter(JIRA_SETTINGS_ID, JIRA_KEY);
        Assert.assertTrue(subTasks.isEmpty());
    }

    @Test(expected = JiraException.class)
    public void updateStoryByJiraKey() throws JiraException {
        when(client.getIssue(anyString())).thenReturn(issue);
        when(client.getIssue(anyString()).update()).thenCallRealMethod();
        jiraStroryDAO.updateStoryByJiraKey(JIRA_SETTINGS_ID, suit);
    }

    @Test(expected = JiraException.class)
    public void createStory() throws JiraException {
        when(client.createIssue(anyString(),anyString())).thenCallRealMethod();
        jiraStroryDAO.createStory(JIRA_SETTINGS_ID, suit);
    }

    @Test(expected = JiraException.class)
    public void closeStoryByJiraKey() throws JiraException {
        when(client.getIssue(anyString())).thenReturn(issue);
        when(client.getIssue(anyString()).transition()).thenCallRealMethod();
        jiraStroryDAO.closeStoryByJiraKey(JIRA_SETTINGS_ID, JIRA_KEY);
    }

}
