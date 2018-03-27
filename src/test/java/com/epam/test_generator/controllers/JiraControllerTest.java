package com.epam.test_generator.controllers;

import com.epam.test_generator.entities.factory.JiraClientFactory;
import com.epam.test_generator.pojo.JiraStory;
import com.epam.test_generator.services.JiraService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class JiraControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper mapper = new ObjectMapper();

    private final String SIMPLE_JIRA_PROJECT_KEY = "key";
    private final Long JIRA_SETTINGS_ID = 1L;

    @Mock
    private Authentication authentication;

    @Mock
    private JiraService jiraService;

    @InjectMocks
    private JiraController jiraController;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(jiraController)
            .setControllerAdvice(new GlobalExceptionController())
            .build();
    }

    @Test
    public void createStoriesForProject() throws Exception {
        List<JiraStory> jiraStories = new ArrayList<>();

        mockMvc.perform(post("/jira/ " +  JIRA_SETTINGS_ID +"/project/" + SIMPLE_JIRA_PROJECT_KEY + "/suits")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(jiraStories)))
            .andDo(print())
            .andExpect(status().isOk());

        verify(jiraService).addStoriesToExistedProject(JIRA_SETTINGS_ID, jiraStories, SIMPLE_JIRA_PROJECT_KEY);
    }

    @Test
    public void createProjectWithAttFromJira() throws Exception {
        List<JiraStory> jiraStories = new ArrayList<>();

        mockMvc.perform(post("/jira/" +  JIRA_SETTINGS_ID + "/project")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(jiraStories)))
            .andDo(print())
            .andExpect(status().isOk());

        verify(jiraService).createProjectWithAttachments(eq(JIRA_SETTINGS_ID), any(List.class),any(Authentication.class));
    }

    @Test
    public void getAllStories() throws Exception {
        mockMvc.perform(get("/jira/" +  JIRA_SETTINGS_ID +"/project/" + SIMPLE_JIRA_PROJECT_KEY + "/stories"))
            .andDo(print())
            .andExpect(status().isOk());

        verify(jiraService).getJiraStoriesFromJiraProjectByProjectId(JIRA_SETTINGS_ID, SIMPLE_JIRA_PROJECT_KEY);
    }


    @Test
    public void getProjects() throws Exception {
        mockMvc.perform(get("/jira/" +  JIRA_SETTINGS_ID +"/projects"))
            .andDo(print())
            .andExpect(status().isOk());

        verify(jiraService).getNonexistentJiraProjects(JIRA_SETTINGS_ID);
    }

    @Test
    public void syncToBddFromJira() throws Exception {
        mockMvc.perform(get("/jira/" +  JIRA_SETTINGS_ID +"/syncFromJira"))
            .andDo(print())
            .andExpect(status().isOk());

        verify(jiraService).syncFromJira(JIRA_SETTINGS_ID);
    }

    @Test
    public void syncToJiraFromBdd() throws Exception {
        mockMvc.perform(get("/jira/" +  JIRA_SETTINGS_ID + "/syncToJira"))
            .andDo(print())
            .andExpect(status().isOk());

        verify(jiraService).syncToJira(JIRA_SETTINGS_ID);
    }
}