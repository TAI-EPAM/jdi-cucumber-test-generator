package com.epam.test_generator.controllers;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epam.test_generator.controllers.admin.AdminController;
import com.epam.test_generator.controllers.admin.request.JiraSettingsCreateDTO;
import com.epam.test_generator.controllers.admin.request.UserRoleUpdateDTO;
import com.epam.test_generator.services.AdminService;
import com.epam.test_generator.services.JiraSettingsService;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


@RunWith(MockitoJUnitRunner.class)
public class AdminControllerTest {

    private MockMvc mockMvc;
    private UserRoleUpdateDTO userChangeRole;
    private ObjectMapper mapper;
    @Mock
    private AdminService adminService;
    @Mock
    private JiraSettingsService jiraSettingsService;

    @InjectMocks
    private AdminController adminController;

    @Before

    public void setUp() {
        mapper = new ObjectMapper();
        userChangeRole = new UserRoleUpdateDTO();
        mockMvc = MockMvcBuilders.standaloneSetup(adminController)
            .setControllerAdvice(new GlobalExceptionController())
            .build();
    }

    @Test
    public void changeUserRole_SimpleRole_StatusOk() throws Exception {
        userChangeRole.setEmail("email@mail.com");
        userChangeRole.setRole("Role");
        String json = mapper.writeValueAsString(userChangeRole);

        mockMvc.perform(
            put("/admin/role").contentType(MediaType.APPLICATION_JSON).content(json))
            .andExpect(status().isOk());
    }

    @Test
    public void createJiraSettings_JiraSetting_StatusOk() throws Exception {
        JiraSettingsCreateDTO jiraSettingsCreateDTO = new JiraSettingsCreateDTO();
        jiraSettingsCreateDTO.setLogin("login");
        jiraSettingsCreateDTO.setPassword("password");
        jiraSettingsCreateDTO.setUri("uri");

        mockMvc.perform(post("/admin/jira-settings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(jiraSettingsCreateDTO)))
            .andExpect(status().isOk());

        verify(jiraSettingsService).createJiraSettings(eq(jiraSettingsCreateDTO));
    }

    @Test
    public void getJiraSettings_JiraSettings_StatusOk() throws Exception {
        mockMvc.perform(get("/admin/jira-settings"))
            .andExpect(status().isOk());

        verify(jiraSettingsService).getJiraSettings();
    }
}