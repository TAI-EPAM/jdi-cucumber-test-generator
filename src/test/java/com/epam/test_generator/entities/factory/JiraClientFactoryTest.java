package com.epam.test_generator.entities.factory;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import com.epam.test_generator.dao.interfaces.JiraSettingsDAO;
import com.epam.test_generator.entities.JiraSettings;
import com.epam.test_generator.services.exceptions.NotFoundException;
import java.util.Optional;
import net.rcarz.jiraclient.JiraClient;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class JiraClientFactoryTest {

    @Mock
    private JiraSettingsDAO jiraSettingsDAO;

    @InjectMocks
    private JiraClientFactory jiraClientFactory;

    private static final String JIRA_URI = "uri";
    private static final String JIRA_LOGIN = "login";
    private static final String JIRA_PASSWORD = "pass";
    private static final Long JIRA_SETTINGS_ID = 1L;

    @Test
    public void getJiraClient_NormalData_Success() {
        JiraSettings jiraSettings = new JiraSettings(JIRA_URI, JIRA_LOGIN, JIRA_PASSWORD);
        when(jiraSettingsDAO.findById(anyLong())).thenReturn(Optional.of(jiraSettings));

        JiraClient jiraClient = jiraClientFactory.getJiraClient(JIRA_SETTINGS_ID);
        Assert.assertNotNull(jiraClient);
    }

    @Test(expected = NotFoundException.class)
    public void getJiraClient_NonexistentId_Exception() {
        when(jiraSettingsDAO.findById(anyLong())).thenReturn(Optional.empty());

        jiraClientFactory.getJiraClient(JIRA_SETTINGS_ID);
    }

}
