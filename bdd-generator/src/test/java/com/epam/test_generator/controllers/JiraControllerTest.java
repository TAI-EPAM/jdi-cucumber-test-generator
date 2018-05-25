package com.epam.test_generator.controllers;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epam.test_generator.pojo.JiraFilter;
import com.epam.test_generator.pojo.JiraStory;
import com.epam.test_generator.services.JiraService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@RunWith(MockitoJUnitRunner.class)
public class JiraControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper mapper = new ObjectMapper();

    private final String SIMPLE_JIRA_PROJECT_KEY = "key";
    private final Long JIRA_SETTINGS_ID = 1L;

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

        mockMvc.perform(
            post("/jira/project/" + SIMPLE_JIRA_PROJECT_KEY + "/suits")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(jiraStories)))
            .andDo(print())
            .andExpect(status().isOk());

        verify(jiraService)
            .addStoriesToExistedProject(jiraStories, SIMPLE_JIRA_PROJECT_KEY);
    }

    @Test
    public void createProjectbyFilters() throws Exception {
        List<JiraFilter> jiraFilters = new ArrayList<>();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        mockMvc.perform(post(
            "/jira/jira-settings/" + JIRA_SETTINGS_ID + "/project-by-filters/"
                + SIMPLE_JIRA_PROJECT_KEY)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(jiraFilters)))
            .andDo(print())
            .andExpect(status().isOk());

        verify(jiraService)
            .createProjectWithAttachedFilters(JIRA_SETTINGS_ID, SIMPLE_JIRA_PROJECT_KEY,
                jiraFilters, auth);
    }


    @Test
    public void createProjectWithAttFromJira() throws Exception {
        List<JiraStory> jiraStories = new ArrayList<>();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        mockMvc.perform(
            post("/jira/jira-settings/" + JIRA_SETTINGS_ID + "/project/" + SIMPLE_JIRA_PROJECT_KEY)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(jiraStories)))
            .andDo(print())
            .andExpect(status().isOk());

        verify(jiraService)
            .createProjectWithJiraStories(JIRA_SETTINGS_ID, SIMPLE_JIRA_PROJECT_KEY, jiraStories,
                auth);
    }

    @Test
    public void getAllStories() throws Exception {
        mockMvc.perform(
            get("/jira/jira-settings/" + JIRA_SETTINGS_ID + "/project/" + SIMPLE_JIRA_PROJECT_KEY + "/stories"))
            .andDo(print())
            .andExpect(status().isOk());

        verify(jiraService)
            .getJiraStoriesFromJiraProjectByProjectId(JIRA_SETTINGS_ID, SIMPLE_JIRA_PROJECT_KEY);
    }


    @Test
    public void getProjects() throws Exception {
        mockMvc.perform(get("/jira/jira-settings/" + JIRA_SETTINGS_ID + "/projects"))
            .andDo(print())
            .andExpect(status().isOk());

        verify(jiraService).getNonexistentJiraProjects(JIRA_SETTINGS_ID);
    }

    @Test
    public void syncToBddFromJira() throws Exception {
        mockMvc.perform(put("/jira/jira-settings/" + JIRA_SETTINGS_ID + "/import"))
            .andDo(print())
            .andExpect(status().isOk());

        verify(jiraService).syncFromJira(JIRA_SETTINGS_ID);
    }

    @Test
    public void syncToJiraFromBdd() throws Exception {
        mockMvc.perform(put("/jira/jira-settings/" + JIRA_SETTINGS_ID + "/export"))
            .andDo(print())
            .andExpect(status().isOk());

        verify(jiraService).syncToJira(JIRA_SETTINGS_ID);
    }
}