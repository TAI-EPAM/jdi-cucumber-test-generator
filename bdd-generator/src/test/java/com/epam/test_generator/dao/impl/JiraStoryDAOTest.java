package com.epam.test_generator.dao.impl;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.epam.test_generator.dao.interfaces.JiraSettingsDAO;
import com.epam.test_generator.entities.JiraSettings;
import com.epam.test_generator.entities.Suit;
import com.epam.test_generator.entities.factory.JiraClientFactory;
import com.epam.test_generator.pojo.JiraStory;
import com.epam.test_generator.services.exceptions.JiraRuntimeException;
import java.util.Collections;
import java.util.List;
import net.rcarz.jiraclient.Issue;
import net.rcarz.jiraclient.IssueType;
import net.rcarz.jiraclient.JiraClient;
import net.rcarz.jiraclient.JiraException;
import net.rcarz.jiraclient.RestException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

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
    private IssueType issueType;

    @Mock
    private JiraSettingsDAO jiraSettingsDAO;

    @InjectMocks
    private JiraStoryDAO jiraStroryDAO;

    private static final String JIRA_KEY = "key";
    private static final String JIRA_FILTER = "filter";
    private static final Integer CLOSE_ACTION_ID = 31;
    private static final Long JIRA_SETTINGS_ID = 1L;

    @Before
    public void setUp()  {
        when(clientFactory.getJiraClient(JIRA_SETTINGS_ID)).thenReturn(client);
        JiraSettings jiraSettings = new JiraSettings();
        jiraSettings.setLogin("login");
        jiraSettings.setUri("jira_uri");
        jiraSettings.setPassword("password");
    }

    @Test
    public void getStoryByJiraKey_JiraStory_Success() throws Exception {
        when(client.getIssue(anyString())).thenReturn(issue);
        when(issue.getIssueType()).thenReturn(issueType);
        when(issueType.isSubtask()).thenReturn(false);

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
    public void getUnexistedStoryByJiraKey_JiraStory_Null() throws Exception {
        when(client.getIssue(anyString())).thenThrow(new JiraException("", new RestException("", 404, "")));
        JiraStory story = jiraStroryDAO.getStoryByJiraKey(JIRA_SETTINGS_ID, JIRA_KEY);
        Assert.assertNull(story);
    }

    @Test
    public void getJiraStiriesByFilter_JiraStories_Success() throws JiraException {
        Issue.SearchResult searchResult = new Issue.SearchResult();
        searchResult.issues = Collections.singletonList(issue);
        when(client.searchIssues(anyString(), anyInt())).thenReturn(searchResult);
        when(issue.getIssueType()).thenReturn(issueType);
        when(issueType.isSubtask()).thenReturn(false);
        List<JiraStory> expectedStories = Collections.singletonList(new JiraStory(issue));

        List<JiraStory> resultStories = jiraStroryDAO
            .getJiraStoriesByFilter(JIRA_SETTINGS_ID, JIRA_FILTER);

        Assert.assertEquals(expectedStories, resultStories);
    }

    @Test(expected = JiraRuntimeException.class)
    public void getJiraStoriesByInvalidFilter_JiraStories_MalformedParametersException() throws Exception {
        when(client.searchIssues(anyString(), anyInt())).thenThrow(new JiraException("a"));
        jiraStroryDAO.getJiraStoriesByFilter(JIRA_SETTINGS_ID, JIRA_FILTER);
    }

    @Test(expected = JiraRuntimeException.class)
    public void getNonexistentJiraStroiesByInvalidFilter_JiraStories_Success() throws Exception {
        when(client.searchIssues(anyString(), anyInt())).thenThrow(new JiraException("a", new RestException("a", 404, "bad")));
        List<JiraStory> subTasks = jiraStroryDAO.getJiraStoriesByFilter(JIRA_SETTINGS_ID, JIRA_KEY);
        Assert.assertTrue(subTasks.isEmpty());
    }

    @Test(expected = JiraRuntimeException.class)
    public void updateStoryByJiraKey() throws JiraException {
        when(clientFactory.getJiraClient(JIRA_SETTINGS_ID)).thenReturn(client);
        when(client.getIssue(suit.getJiraKey())).thenReturn(issue);
        when(issue.update()).thenCallRealMethod();
        jiraStroryDAO.updateStoryByJiraKey(JIRA_SETTINGS_ID, suit);
    }

    @Test(expected = JiraRuntimeException.class)
    public void closeStoryByJiraKey() throws JiraException {
        when(client.getIssue(anyString())).thenReturn(issue);
        when(client.getIssue(anyString()).transition()).thenCallRealMethod();
        jiraStroryDAO.changeStatusByJiraKey(JIRA_SETTINGS_ID, JIRA_KEY, CLOSE_ACTION_ID);
    }

}
