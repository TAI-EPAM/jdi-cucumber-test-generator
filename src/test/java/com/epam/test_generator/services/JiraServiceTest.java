package com.epam.test_generator.services;

import com.epam.test_generator.config.security.AuthenticatedUser;
import com.epam.test_generator.dao.impl.JiraProjectDAO;
import com.epam.test_generator.dao.impl.JiraStoryDAO;
import com.epam.test_generator.dao.impl.JiraSubStroryDAO;
import com.epam.test_generator.dao.interfaces.*;
import com.epam.test_generator.entities.*;
import com.epam.test_generator.pojo.JiraProject;
import com.epam.test_generator.pojo.JiraStory;
import com.epam.test_generator.pojo.JiraSubTask;
import net.rcarz.jiraclient.Issue;
import net.rcarz.jiraclient.Issue.SearchResult;
import net.rcarz.jiraclient.JiraClient;
import net.rcarz.jiraclient.JiraException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.Authentication;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.AdditionalMatchers.not;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class JiraServiceTest {

    @Mock
    private Authentication authentication;

    @Mock
    private JiraClient client;

    @Mock
    private JiraSubStroryDAO jiraSubStoryDAO;

    @Mock
    private JiraProjectDAO jiraProjectDAO;

    @Mock
    private JiraStoryDAO jiraStoryDAO;

    @Mock
    private ProjectDAO projectDAO;

    @Mock
    private SuitDAO suitDAO;

    @Mock
    private CaseDAO caseDAO;

    @Mock
    private UserDAO userDAO;

    @Mock
    private RemovedIssueDAO removedIssueDAO;

    @Mock
    private JiraSubTask jiraSubTask;

    @Mock
    private User user;

    @Mock
    private Suit suit;

    @Mock
    private Project project;

    @Mock
    private JiraStory jiraStory;


    @Mock
    private JiraProject jiraProject;

    @Mock
    private Case caze;


    @Mock
    private Issue issue;

    @Mock
    private AuthenticatedUser userDetails;

    @Mock
    private  UserService userService;


    @InjectMocks
    private JiraService jiraService;

    private SearchResult searchResult;
    private Suit testSuit;
    private Suit closedSuit;
    private Case closedCase;



    private static final String NAME = "name";
    private static final String PASSWORD = "pass";
    private static final String JIRA_PROJECT_KEY = "jira_project_key";
    private static final String JIRA_KEY = "key";
    private static final String PRIORITY = "no priority";
    private static final String CLOSE_JIRA_KEY = "key2";
    private static final Long JIRA_SETTINGS_ID = 1L;

    @Before
    public void setUp() throws Exception {
        when(projectDAO.findByJiraKey(anyString())).thenReturn(project);
        when(client.searchIssues(anyString(), anyInt())).thenReturn(searchResult);
        when(jiraStory.getPriority()).thenReturn(PRIORITY);
        when(jiraSubTask.getPriority()).thenReturn(PRIORITY);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getEmail()).thenReturn("la@l.ka");
        when(userService.getUserByEmail(anyString())).thenReturn(user);
        when(jiraStory.getJiraProjectKey()).thenReturn(JIRA_PROJECT_KEY);

        closedSuit = new Suit();
        closedSuit.setJiraKey(CLOSE_JIRA_KEY);
        closedCase = new Case();
        closedCase.setJiraKey(CLOSE_JIRA_KEY);
        closedCase.setJiraParentKey(CLOSE_JIRA_KEY);


        testSuit = new Suit();
        testSuit.setName("suit");
        testSuit.setJiraKey(JIRA_KEY);
        testSuit.setJiraProjectKey(JIRA_PROJECT_KEY);


    }

    @Test
    public void createProjectWithAttachments_Project_Success() throws JiraException {
        when(jiraProjectDAO.getProjectByJiraKey(anyLong(), anyString())).thenReturn(jiraProject);
        when(userDAO.findByEmail(anyString())).thenReturn(user);
        when(jiraSubStoryDAO.getJiraSubtoriesByFilter(anyLong(), anyString())).thenReturn(Collections.singletonList(jiraSubTask));
        when(suitDAO.findByJiraKey(anyString())).thenReturn(suit);
        when(caseDAO.findByJiraKey(anyString())).thenReturn(null);
        when(jiraStory.getJiraKey()).thenReturn(JIRA_KEY);
        when(jiraSubTask.getJiraKey()).thenReturn(JIRA_KEY);


        jiraService.createProjectWithAttachments(JIRA_SETTINGS_ID, Collections.singletonList(jiraStory),authentication);

        verify(projectDAO).save(any(Project.class));
        verify(suitDAO).save(any(Suit.class));
        verify(caseDAO).save(any(Case.class));
    }

    @Test
    public void addStoriesToExistedProject_SuitAndCase_Success() throws Exception {

        when(jiraSubStoryDAO.getJiraSubtoriesByFilter(anyLong(), anyString())).thenReturn(Collections.singletonList(jiraSubTask));
        when(suitDAO.findByJiraKey(anyString())).thenReturn(suit);
        when(caseDAO.findByJiraKey(anyString())).thenReturn(null);
        when(jiraStory.getJiraKey()).thenReturn(JIRA_KEY);
        when(jiraSubTask.getJiraKey()).thenReturn(JIRA_KEY);

        jiraService.addStoriesToExistedProject(JIRA_SETTINGS_ID, Collections.singletonList(jiraStory), JIRA_PROJECT_KEY);

        verify(suitDAO).save(any(Suit.class));
        verify(caseDAO).save(any(Case.class));
    }

    @Test
    public void getStories_JiraStories_Success() throws JiraException {
        when(jiraStoryDAO.getStories(anyLong(), anyString())).thenReturn(Collections.singletonList(jiraStory));

        List<JiraStory> stories = jiraService.getStories(JIRA_SETTINGS_ID, JIRA_KEY);
        Assert.assertTrue(!stories.isEmpty());
    }

    @Test
    public void getJiraStoriesFromJiraProjectByProjectId_JiraStories_Success() throws JiraException {
        when(projectDAO.getOne(anyLong())).thenReturn(project);
        when(jiraStoryDAO.getNonexistentStoriesByProject(anyLong(), anyString())).thenReturn(Collections.singletonList(jiraStory));

        List<JiraStory> stories = jiraService.getJiraStoriesFromJiraProjectByProjectId(JIRA_SETTINGS_ID, JIRA_PROJECT_KEY);
        Assert.assertTrue(!stories.isEmpty());
    }

    @Test
    public void getNonexistedJiraProjects_JiraProjects_Success() throws JiraException {
        when(jiraProjectDAO.getAllProjects(anyLong())).thenReturn(Collections.singletonList(jiraProject));
        when(projectDAO.findByJiraKey(anyString())).thenReturn(null);

        List<JiraProject> projects = jiraService.getNonexistentJiraProjects(JIRA_SETTINGS_ID);
        Assert.assertTrue(!projects.isEmpty());
    }

    @Test
    public void syncFromJiraUpdateSuitAndCase_SuitAndCase_Success() throws JiraException {
        when(projectDAO.findAll()).thenReturn(Collections.singletonList(project));
        when(jiraSubStoryDAO.getJiraSubtoriesByFilter(anyLong(), anyString())).thenReturn(Arrays.asList(jiraSubTask));
        when(suitDAO.findAll()).thenReturn(Arrays.asList(suit));
        when(caseDAO.findAll()).thenReturn(Collections.singletonList(caze));
        when(jiraStoryDAO.getJiraStoriesByFilter(anyLong(), anyString())).thenReturn(Collections.singletonList(jiraStory));
        when(suitDAO.findByJiraKey(anyString())).thenReturn(suit);
        when(caseDAO.findByJiraKey(anyString())).thenReturn(caze);
        when(jiraSubTask.getJiraKey()).thenReturn(JIRA_KEY);

        jiraService.syncFromJira(JIRA_SETTINGS_ID);

        verify(suitDAO).save(any(Suit.class));
        verify(caseDAO).save(any(Case.class));
    }

    @Test
    public void syncFromJiraDeleteClosedSuitAndCase_SuitAndCase_Success() throws JiraException {
        when(projectDAO.findAll()).thenReturn(Collections.singletonList(project));
        when(jiraSubStoryDAO.getJiraSubtoriesByFilter(anyLong(), anyString())).thenReturn(Arrays.asList(jiraSubTask));
        when(suitDAO.findAll()).thenReturn(Arrays.asList(suit, closedSuit));
        when(caseDAO.findAll()).thenReturn(Arrays.asList(caze, closedCase));
        when(jiraStoryDAO.getJiraStoriesByFilter(anyLong(), anyString())).thenReturn(Collections.singletonList(jiraStory));
        when(suitDAO.findByJiraKey(not(eq(CLOSE_JIRA_KEY)))).thenReturn(suit);
        when(suitDAO.findByJiraKey(CLOSE_JIRA_KEY)).thenReturn(closedSuit);
        when(caseDAO.findByJiraKey(not(eq(CLOSE_JIRA_KEY)))).thenReturn(caze);

        jiraService.syncFromJira(JIRA_SETTINGS_ID);

        verify(suitDAO, times(2)).delete(any(Suit.class));
        verify(caseDAO, times(2)).delete(any(Case.class));
    }


    @Test
    public void syncToJiraFromBDD_NewSuitWithCasesWithIssueToDelete_JiraUpdated() throws JiraException {
        Case caze = new Case();
        testSuit.getCases().add(caze);
        RemovedIssue removedIssue = new RemovedIssue();
        removedIssue.setJiraKey(JIRA_KEY);

        when(suitDAO.findAll()).thenReturn(Collections.singletonList(testSuit));
        when(removedIssueDAO.findAll()).thenReturn(Collections.singletonList(removedIssue));

        doAnswer( s -> {
            caze.setLastJiraSyncDate(LocalDateTime.now().minusMinutes(1));
            caze.setLastModifiedDate(LocalDateTime.now());
            return null;
        }).when(jiraSubStoryDAO).createSubStory(JIRA_SETTINGS_ID, caze);

        jiraService.syncToJira(JIRA_SETTINGS_ID);

        verify(jiraStoryDAO).createStory(anyLong(), eq(testSuit));
        verify(jiraSubStoryDAO).createSubStory(anyLong(), eq(caze));
        verify(removedIssueDAO).delete(removedIssue);
    }

    @Test
    public void syncToJiraFromBDD_SuitWithCaseWithIssueToDelete_JiraUpdated() throws JiraException {
        testSuit.getCases().add(new Case());

        testSuit.setLastJiraSyncDate(LocalDateTime.now().minusMinutes(1));
        testSuit.setLastModifiedDate(LocalDateTime.now());

        when(suitDAO.findAll()).thenReturn(Collections.singletonList(testSuit));

        jiraService.syncToJira(JIRA_SETTINGS_ID);

        verify(jiraStoryDAO).updateStoryByJiraKey(anyLong(),  eq(testSuit));
        verify(jiraSubStoryDAO).createSubStory(anyLong(), any(Case.class));
    }
}