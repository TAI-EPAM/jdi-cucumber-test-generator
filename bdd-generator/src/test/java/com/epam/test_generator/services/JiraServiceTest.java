package com.epam.test_generator.services;

import static org.mockito.AdditionalMatchers.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.epam.test_generator.config.security.AuthenticatedUser;
import com.epam.test_generator.dao.impl.JiraProjectDAO;
import com.epam.test_generator.dao.impl.JiraStoryDAO;
import com.epam.test_generator.dao.impl.JiraSubStoryDAO;
import com.epam.test_generator.dao.interfaces.CaseDAO;
import com.epam.test_generator.dao.interfaces.ProjectDAO;
import com.epam.test_generator.dao.interfaces.RemovedIssueDAO;
import com.epam.test_generator.dao.interfaces.SuitDAO;
import com.epam.test_generator.dao.interfaces.UserDAO;
import com.epam.test_generator.entities.Case;
import com.epam.test_generator.entities.Project;
import com.epam.test_generator.entities.RemovedIssue;
import com.epam.test_generator.entities.Status;
import com.epam.test_generator.entities.Suit;
import com.epam.test_generator.entities.User;
import com.epam.test_generator.pojo.JiraProject;
import com.epam.test_generator.pojo.JiraStatus;
import com.epam.test_generator.pojo.JiraStory;
import com.epam.test_generator.pojo.JiraSubTask;
import com.google.common.collect.Lists;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import net.rcarz.jiraclient.JiraClient;
import net.rcarz.jiraclient.JiraException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.Authentication;

@RunWith(MockitoJUnitRunner.class)
public class JiraServiceTest {

    private static final String JIRA_PROJECT_KEY = "jira_project_key";
    private static final String JIRA_KEY = "key";
    private static final String PRIORITY = "no priority";
    private static final String CLOSE_JIRA_KEY = "key2";
    private static final Long JIRA_SETTINGS_ID = 1L;
    private static final String SUIT_NAME = "suit_name";
    private static final String USER_EMAIL = "la@l.ka";

    @Mock
    private Authentication authentication;

    @Mock
    private JiraClient client;

    @Mock
    private JiraSubStoryDAO jiraSubStoryDAO;

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
    private AuthenticatedUser userDetails;

    @Mock
    private  UserService userService;


    @InjectMocks
    private JiraService jiraService;

    private Suit testSuit;
    private Suit closedSuit;
    private Case closedCase;




    @Before
    public void setUp() {
        closedSuit = new Suit();
        closedSuit.setJiraKey(CLOSE_JIRA_KEY);
        closedCase = new Case();
        closedCase.setJiraKey(CLOSE_JIRA_KEY);
        closedCase.setJiraParentKey(CLOSE_JIRA_KEY);


        testSuit = new Suit();
        testSuit.setName(SUIT_NAME);
        testSuit.setJiraKey(JIRA_KEY);
        testSuit.setJiraProjectKey(JIRA_PROJECT_KEY);

        when(userService.getUserByEmail(anyString())).thenReturn(user);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getEmail()).thenReturn(USER_EMAIL);

        when(jiraStory.getJiraProjectKey()).thenReturn(JIRA_PROJECT_KEY);
        when(jiraStory.getJiraKey()).thenReturn(JIRA_KEY);
        when(jiraSubTask.getJiraParentKey()).thenReturn(JIRA_KEY);
        when(jiraSubTask.getPriority()).thenReturn(PRIORITY);

        when(projectDAO.findByJiraKey(closedSuit.getJiraProjectKey())).thenReturn(project);
        when(project.getSuits()).thenReturn(Lists.newArrayList(closedSuit));


    }

    @Test
    public void createProjectWithAttachments_Project_Success() {
        when(jiraProjectDAO.getProjectByJiraKey(anyLong(), anyString())).thenReturn(jiraProject);
        when(jiraStory.getJiraKey()).thenReturn(JIRA_KEY);
        when(jiraStory.getStatus()).thenReturn(JiraStatus.RESOLVED);
        when(jiraStory.getPriority()).thenReturn(PRIORITY);


        jiraService.createProjectWithJiraStories(JIRA_SETTINGS_ID, JIRA_PROJECT_KEY,
            Collections.singletonList(jiraStory), authentication);

        verify(projectDAO).save(any(Project.class));
    }

    @Test
    public void addStoriesToExistedProject_SuitAndCase_Success() {

        when(jiraStory.getJiraKey()).thenReturn(JIRA_KEY);
        when(jiraStory.getStatus()).thenReturn(JiraStatus.RESOLVED);
        when(projectDAO.findByJiraKey(JIRA_PROJECT_KEY)).thenReturn(project);
        when(jiraStory.getPriority()).thenReturn(PRIORITY);
        jiraService.addStoriesToExistedProject(Collections.singletonList(jiraStory), JIRA_PROJECT_KEY);

        verify(projectDAO).save(any(Project.class));
    }

    @Test
    public void getStories_JiraStories_Success() throws JiraException {
        when(jiraStoryDAO.getStories(anyLong(), anyString())).thenReturn(Collections.singletonList(jiraStory));

        List<JiraStory> stories = jiraService.getStories(JIRA_SETTINGS_ID, JIRA_KEY);
        Assert.assertTrue(!stories.isEmpty());
    }

    @Test
    public void getJiraStoriesFromJiraProjectByProjectId_JiraStories_Success() {
        when(jiraStoryDAO.getNonexistentStoriesByProject(anyLong(), anyString())).thenReturn(Collections.singletonList(jiraStory));

        List<JiraStory> stories = jiraService.getJiraStoriesFromJiraProjectByProjectId(JIRA_SETTINGS_ID, JIRA_PROJECT_KEY);
        Assert.assertTrue(!stories.isEmpty());
    }

    @Test
    public void getNonexistedJiraProjects_JiraProjects_Success() {
        when(jiraProjectDAO.getAllProjects(anyLong())).thenReturn(Collections.singletonList(jiraProject));
        when(jiraProject.getJiraKey()).thenReturn(JIRA_PROJECT_KEY);
        when(projectDAO.findByJiraKey(jiraProject.getJiraKey())).thenReturn(null);

        List<JiraProject> projects = jiraService.getNonexistentJiraProjects(JIRA_SETTINGS_ID);
        Assert.assertTrue(!projects.isEmpty());
    }

    @Test
    public void syncFromJiraUpdateSuitAndCase_SuitAndCase_Success() {
        when(projectDAO.findAll()).thenReturn(Collections.singletonList(project));
        when(jiraStoryDAO.getJiraStoriesByFilter(anyLong(), anyString()))
            .thenReturn(Collections.singletonList(jiraStory));
        when(jiraSubStoryDAO.getJiraSubtoriesByFilter(anyLong(), anyString()))
            .thenReturn(Collections.singletonList(jiraSubTask));
        when(suitDAO.findByJiraKey(jiraStory.getJiraKey())).thenReturn(suit);
        when(suitDAO.findByJiraKey(jiraSubTask.getJiraParentKey())).thenReturn(suit);


        when(suitDAO.findAll()).thenReturn(Collections.singletonList(closedSuit));
        when(caseDAO.findAll()).thenReturn(Collections.singletonList(closedCase));
        when(suitDAO.findByJiraKey(closedCase.getJiraParentKey())).thenReturn(suit);
        when(suit.getCases()).thenReturn(Lists.newArrayList(closedCase));
        when(caseDAO.findByJiraKey(anyString())).thenReturn(caze);
        when(jiraSubTask.getJiraKey()).thenReturn(JIRA_KEY);

        jiraService.syncFromJira(JIRA_SETTINGS_ID);

        verify(suitDAO).save(any(Suit.class));
        verify(caseDAO).save(any(Case.class));
    }

    @Test
    public void syncFromJiraDeleteClosedSuitAndCase_SuitAndCase_Success() {
        when(projectDAO.findAll()).thenReturn(Collections.singletonList(project));
        when(jiraStoryDAO.getJiraStoriesByFilter(anyLong(), anyString()))
            .thenReturn(Collections.singletonList(jiraStory));
        when(jiraSubStoryDAO.getJiraSubtoriesByFilter(anyLong(), anyString()))
            .thenReturn(Collections.singletonList(jiraSubTask));






        when(suitDAO.findByJiraKey(CLOSE_JIRA_KEY)).thenReturn(closedSuit);
        when(suitDAO.findAll()).thenReturn(Arrays.asList(suit, closedSuit));
        when(caseDAO.findAll()).thenReturn(Arrays.asList(caze, closedCase));
        when(suitDAO.findByJiraKey(not(eq(CLOSE_JIRA_KEY)))).thenReturn(suit);
        when(caseDAO.findByJiraKey(not(eq(CLOSE_JIRA_KEY)))).thenReturn(caze);

        jiraService.syncFromJira(JIRA_SETTINGS_ID);

        verify(suitDAO, times(2)).delete(any(Suit.class));
        verify(caseDAO, times(2)).delete(any(Case.class));
    }


    @Test
    public void syncToJiraFromBDD_NewSuitWithCasesWithIssueToDelete_JiraUpdated() {
        Case caze = new Case();
        testSuit.getCases().add(caze);
        RemovedIssue removedIssue = new RemovedIssue();
        removedIssue.setJiraKey(JIRA_KEY);

        when(suitDAO.findAll()).thenReturn(Collections.singletonList(testSuit));
        when(removedIssueDAO.findAll()).thenReturn(Collections.singletonList(removedIssue));

        doAnswer( s -> {
            caze.setLastJiraSyncDate(ZonedDateTime.now().minusMinutes(1));
            caze.setLastModifiedDate(ZonedDateTime.now());
            return null;
        }).when(jiraSubStoryDAO).createSubStory(JIRA_SETTINGS_ID, caze);

        jiraService.syncToJira(JIRA_SETTINGS_ID);

        verify(jiraStoryDAO).createStory(anyLong(), eq(testSuit));
        verify(jiraSubStoryDAO).createSubStory(anyLong(), eq(caze));
        verify(removedIssueDAO).delete(removedIssue);
    }

    @Test
    public void syncToJiraFromBDD_SuitWithCaseWithIssueToDelete_JiraUpdated() {
        testSuit.getCases().add(new Case());

        testSuit.setLastJiraSyncDate(ZonedDateTime.now().minusMinutes(1));
        testSuit.setLastModifiedDate(ZonedDateTime.now());
        testSuit.setStatus(Status.PASSED);

        when(suitDAO.findAll()).thenReturn(Collections.singletonList(testSuit));

        jiraService.syncToJira(JIRA_SETTINGS_ID);

        verify(jiraSubStoryDAO).createSubStory(anyLong(), any(Case.class));
    }
}