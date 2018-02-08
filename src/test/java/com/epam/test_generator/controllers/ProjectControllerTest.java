package com.epam.test_generator.controllers;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epam.test_generator.dto.ProjectDTO;
import com.epam.test_generator.dto.ProjectFullDTO;
import com.epam.test_generator.services.ProjectService;
import com.epam.test_generator.services.exceptions.NotFoundException;
import com.epam.test_generator.services.exceptions.ProjectClosedException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
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

@RunWith(MockitoJUnitRunner.class)
public class ProjectControllerTest {

    private static final long SIMPLE_PROJECT_ID = 0L;
    private ObjectMapper mapper = new ObjectMapper();
    private MockMvc mockMvc;
    private ProjectDTO projectDTO;

    @Mock
    private ProjectService projectService;
    @InjectMocks
    private ProjectController projectController;

    @Before
    public void setUp(){
        this.mockMvc = MockMvcBuilders.standaloneSetup(projectController)
            .setControllerAdvice(new GlobalExceptionController())
            .build();
        projectDTO = new ProjectDTO();
        projectDTO.setId(SIMPLE_PROJECT_ID);
        projectDTO.setName("Project name");
        projectDTO.setDescription("Project description");
    }

    @Test
    public void getUserProjects_CorrectRequest_StatusOk() throws Exception {
        when(projectService.getAuthenticatedUserProjects(any(Authentication.class))).thenReturn(Arrays.asList(projectDTO));

        mockMvc.perform(get("/projects"))
            .andDo(print())
            .andExpect(status().isOk());

        verify(projectService).getAuthenticatedUserProjects(anyObject());
    }

    @Test
    public void getProject_CorrectProjectId_StatusOk() throws Exception {
        when(projectService.getAuthUserFullProject(anyLong(), any(Authentication.class))).thenReturn(new ProjectFullDTO());

        mockMvc.perform(get("/projects/" + SIMPLE_PROJECT_ID))
            .andDo(print())
            .andExpect(status().isOk());

        verify(projectService).getAuthUserFullProject(eq(SIMPLE_PROJECT_ID), anyObject());
    }

    @Test
    public void getProject_IncorrectProjectId_StatusNotFound() throws Exception {
        when(projectService.getAuthUserFullProject(anyLong(), anyObject())).thenThrow(new NotFoundException());

        mockMvc.perform(get("/projects/" + SIMPLE_PROJECT_ID))
            .andDo(print())
            .andExpect(status().isNotFound());

        verify(projectService).getAuthUserFullProject(eq(SIMPLE_PROJECT_ID), anyObject());
    }

    @Test
    public void createProject_CorrectDTO_StatusCreated() throws Exception {
        projectDTO.setId(null);
        when(projectService.createProject(any(ProjectDTO.class), any(Authentication.class))).thenReturn(SIMPLE_PROJECT_ID);

        mockMvc.perform(post("/projects")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(projectDTO)))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(content().string(String.valueOf(SIMPLE_PROJECT_ID)));

        verify(projectService).createProject(any(ProjectDTO.class), any(Authentication.class));
    }

    @Test
    public void createProject_IncorrectDTO_StatusBadRequest() throws Exception {
        projectDTO.setId(null);
        projectDTO.setName(null);

        mockMvc.perform(post("/projects")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(projectDTO)))
            .andDo(print())
            .andExpect(status().isBadRequest());

        verify(projectService,never()).createProject(any(ProjectDTO.class), any(Authentication.class));
    }

    @Test
    public void updateProject_ValidInput_StatusOk() throws Exception {
        mockMvc.perform(put("/projects/" + SIMPLE_PROJECT_ID)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(projectDTO)))
            .andDo(print())
            .andExpect(status().isOk());

        verify(projectService).updateProject((anyLong()), any(ProjectDTO.class));
    }

    @Test
    public void updateProject_InvalidInput_StatusBadRequest() throws Exception {
        projectDTO.setId(null);
        projectDTO.setName(null);

        mockMvc.perform(put("/projects/" + SIMPLE_PROJECT_ID)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(projectDTO)))
            .andDo(print())
            .andExpect(status().isBadRequest());

        verify(projectService, never()).updateProject(anyLong(), any(ProjectDTO.class));
    }

    @Test
    public void updateProject_ValidUpdateDTO_StatusNotFound() throws Exception {
        doThrow(NotFoundException.class)
            .when(projectService).updateProject(anyLong(), any(ProjectDTO.class));
        mockMvc.perform(put("/projects/" + SIMPLE_PROJECT_ID)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(projectDTO)))
            .andDo(print())
            .andExpect(status().isNotFound());

        verify(projectService).updateProject((anyLong()), any(ProjectDTO.class));
    }

    @Test
    public void updateProject_ValidUpdateDTO_StatusForbidden() throws Exception {
        projectDTO.setActive(false);

        doThrow(ProjectClosedException.class)
            .when(projectService).updateProject(anyLong(), any(ProjectDTO.class));
        mockMvc.perform(put("/projects/" + SIMPLE_PROJECT_ID)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(projectDTO)))
            .andDo(print())
            .andExpect(status().isForbidden());

        verify(projectService).updateProject((anyLong()), any(ProjectDTO.class));
    }

    @Test
    public void closeProject_ProjectId_StatusOk() throws Exception {
        mockMvc.perform(delete("/projects/" + SIMPLE_PROJECT_ID))
            .andDo(print())
            .andExpect(status().isOk());

        verify(projectService).closeProject((anyLong()));
    }

    @Test
    public void closeProject_ProjectId_StatusNotFound() throws Exception {
        doThrow(NotFoundException.class)
            .when(projectService).closeProject(anyLong());
        mockMvc.perform(delete("/projects/" + SIMPLE_PROJECT_ID))
            .andDo(print())
            .andExpect(status().isNotFound());

        verify(projectService).closeProject((anyLong()));
    }

    @Test
    public void closeProject_ProjectId_StatusForbidden() throws Exception {
        doThrow(ProjectClosedException.class)
            .when(projectService).closeProject(anyLong());
        mockMvc.perform(delete("/projects/" + SIMPLE_PROJECT_ID))
            .andDo(print())
            .andExpect(status().isForbidden());

        verify(projectService).closeProject((anyLong()));
    }

    @Test
    public void assignUserToProject_ValidInput_StatusOk() throws Exception {
        mockMvc.perform(put("/projects/" + SIMPLE_PROJECT_ID +"/users")
            .param("userId", "0"))
            .andDo(print())
            .andExpect(status().isOk());

        verify(projectService).addUserToProject(anyLong(), anyLong());
    }

    @Test
    public void assignUserToProject_InvalidInput_StatusNotFound() throws Exception {
        doThrow(NotFoundException.class)
            .when(projectService).addUserToProject(anyLong(), anyLong());
        mockMvc.perform(put("/projects/" + SIMPLE_PROJECT_ID +"/users")
            .param("userId", "0"))
            .andDo(print())
            .andExpect(status().isNotFound());

        verify(projectService).addUserToProject(anyLong(), anyLong());
    }

    @Test
    public void assignUserToProject_ValidInput_StatusForbidden() throws Exception {
        doThrow(ProjectClosedException.class)
            .when(projectService).addUserToProject(anyLong(), anyLong());
        mockMvc.perform(put("/projects/" + SIMPLE_PROJECT_ID +"/users")
            .param("userId", "0"))
            .andDo(print())
            .andExpect(status().isForbidden());

        verify(projectService).addUserToProject(anyLong(), anyLong());
    }

    @Test
    public void removeUserFromProject_ValidInput_StatusOk() throws Exception {
        mockMvc.perform(delete("/projects/" + SIMPLE_PROJECT_ID +"/users")
            .param("userId", "0"))
            .andDo(print())
            .andExpect(status().isOk());

        verify(projectService).removeUserFromProject(anyLong(), anyLong());
    }

    @Test
    public void removeUserFromProject_InvalidInput_StatusNotFound() throws Exception {
        doThrow(NotFoundException.class)
            .when(projectService).removeUserFromProject(anyLong(), anyLong());
        mockMvc.perform(delete("/projects/" + SIMPLE_PROJECT_ID +"/users")
            .param("userId", "0"))
            .andDo(print())
            .andExpect(status().isNotFound());

        verify(projectService).removeUserFromProject(anyLong(), anyLong());
    }

    @Test
    public void removeUserFromProject_ValidInput_StatusForbidden() throws Exception {
        doThrow(ProjectClosedException.class)
            .when(projectService).removeUserFromProject(anyLong(), anyLong());
        mockMvc.perform(delete("/projects/" + SIMPLE_PROJECT_ID +"/users")
            .param("userId", "0"))
            .andDo(print())
            .andExpect(status().isForbidden());

        verify(projectService).removeUserFromProject(anyLong(), anyLong());
    }
}