package com.epam.test_generator.controllers.project;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epam.test_generator.controllers.GlobalExceptionController;
import com.epam.test_generator.controllers.project.request.ProjectCreateDTO;
import com.epam.test_generator.controllers.project.request.ProjectUpdateDTO;
import com.epam.test_generator.controllers.project.response.ProjectDTO;
import com.epam.test_generator.services.ProjectService;
import com.epam.test_generator.services.exceptions.NotFoundException;
import com.epam.test_generator.services.exceptions.ProjectClosedException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@RunWith(MockitoJUnitRunner.class)
public class ProjectControllerTest {

    private static final long SIMPLE_PROJECT_ID = 0L;
    private static final long SIMPLE_USER_ID = 0L;
    private static final String USER_ID = "userId";
    private static final String USER_ID_VALUE = "0";
    private ObjectMapper mapper = new ObjectMapper();
    private MockMvc mockMvc;
    private ProjectDTO projectDTO;

    @Mock
    private ProjectService projectService;
    @InjectMocks
    private ProjectController projectController;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(projectController)
            .setControllerAdvice(new GlobalExceptionController())
            .build();
        projectDTO = new ProjectDTO();
        projectDTO.setId(SIMPLE_PROJECT_ID);
        projectDTO.setName("project name");
        projectDTO.setDescription("project description");
    }

    @Test
    public void getUserProjects_CorrectRequest_StatusOk() throws Exception {
        mockMvc.perform(get("/projects"))
            .andExpect(status().isOk());

        verify(projectService).getAuthenticatedUserProjects(any());
    }

    @Test
    public void getProject_CorrectProjectId_StatusOk() throws Exception {
        mockMvc.perform(get("/projects/" + SIMPLE_PROJECT_ID))
            .andExpect(status().isOk());

        verify(projectService).getAuthUserFullProject(eq(SIMPLE_PROJECT_ID), any());
    }

    @Test
    public void getProject_IncorrectProjectId_StatusNotFound() throws Exception {
        when(projectService.getAuthUserFullProject(anyLong(), any()))
            .thenThrow(new NotFoundException());

        mockMvc.perform(get("/projects/" + SIMPLE_PROJECT_ID))
            .andExpect(status().isNotFound());

        verify(projectService).getAuthUserFullProject(eq(SIMPLE_PROJECT_ID), any());
    }

    @Test
    public void createProject_CorrectDTO_StatusCreated() throws Exception {
        projectDTO.setId(null);
        mockMvc.perform(post("/projects")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(projectDTO)))
            .andExpect(status().isCreated());

    }

    @Test
    public void createProject_IncorrectDTO_StatusBadRequest() throws Exception {
        projectDTO.setId(null);
        projectDTO.setName(null);

        mockMvc.perform(post("/projects")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(projectDTO)))
            .andExpect(status().isBadRequest());

        verify(projectService, never())
            .createProject(any(ProjectCreateDTO.class), any(Authentication.class));
    }

    @Test
    public void updateProject_ValidInput_StatusOk() throws Exception {
        mockMvc.perform(put("/projects/" + SIMPLE_PROJECT_ID)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(projectDTO)))
            .andExpect(status().isOk());

        verify(projectService).updateProject((anyLong()), any(ProjectUpdateDTO.class));
    }


    @Test
    public void updateProject_ValidUpdateDTO_StatusNotFound() throws Exception {
        doThrow(NotFoundException.class)
            .when(projectService).updateProject(anyLong(), any(ProjectUpdateDTO.class));
        mockMvc.perform(put("/projects/" + SIMPLE_PROJECT_ID)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(projectDTO)))
            .andExpect(status().isNotFound());

        verify(projectService).updateProject((anyLong()), any(ProjectUpdateDTO.class));
    }

    @Test
    public void updateProject_ValidUpdateDTO_StatusForbidden() throws Exception {
        projectDTO.setActive(false);

        doThrow(ProjectClosedException.class)
            .when(projectService).updateProject(anyLong(), any(ProjectUpdateDTO.class));
        mockMvc.perform(put("/projects/" + SIMPLE_PROJECT_ID)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(projectDTO)))
            .andExpect(status().isForbidden());

        verify(projectService).updateProject((anyLong()), any(ProjectUpdateDTO.class));
    }

    @Test
    public void closeProject_ProjectId_StatusOk() throws Exception {
        mockMvc.perform(delete("/projects/" + SIMPLE_PROJECT_ID))
            .andExpect(status().isOk());

        verify(projectService).closeProject((anyLong()));
    }

    @Test
    public void closeProject_ProjectId_StatusNotFound() throws Exception {
        doThrow(NotFoundException.class)
            .when(projectService).closeProject(anyLong());
        mockMvc.perform(delete("/projects/" + SIMPLE_PROJECT_ID))
            .andExpect(status().isNotFound());

        verify(projectService).closeProject((anyLong()));
    }

    @Test
    public void closeProject_ProjectId_StatusForbidden() throws Exception {
        doThrow(ProjectClosedException.class)
            .when(projectService).closeProject(anyLong());
        mockMvc.perform(delete("/projects/" + SIMPLE_PROJECT_ID))
            .andExpect(status().isForbidden());

        verify(projectService).closeProject((anyLong()));
    }

    @Test
    public void assignUserToProject_ValidInput_StatusOk() throws Exception {
        mockMvc.perform(put("/projects/" + SIMPLE_PROJECT_ID + "/user/" + SIMPLE_USER_ID)
            .param(USER_ID, USER_ID_VALUE))
            .andExpect(status().isOk());

        verify(projectService).addUserToProject(anyLong(), anyLong());
    }

    @Test
    public void assignUserToProject_InvalidInput_StatusNotFound() throws Exception {
        doThrow(NotFoundException.class)
            .when(projectService).addUserToProject(anyLong(), anyLong());
        mockMvc.perform(put("/projects/" + SIMPLE_PROJECT_ID + "/user/" + SIMPLE_USER_ID)
            .param(USER_ID, USER_ID_VALUE))
            .andExpect(status().isNotFound());

        verify(projectService).addUserToProject(anyLong(), anyLong());
    }

    @Test
    public void assignUserToProject_ValidInput_StatusForbidden() throws Exception {
        doThrow(ProjectClosedException.class)
            .when(projectService).addUserToProject(anyLong(), anyLong());
        mockMvc.perform(put("/projects/" + SIMPLE_PROJECT_ID + "/user/" + SIMPLE_USER_ID)
            .param(USER_ID, USER_ID_VALUE))
            .andExpect(status().isForbidden());

        verify(projectService).addUserToProject(anyLong(), anyLong());
    }

    @Test
    public void removeUserFromProject_ValidInput_StatusOk() throws Exception {
        mockMvc.perform(delete("/projects/" + SIMPLE_PROJECT_ID + "/user/" + SIMPLE_USER_ID)
            .param(USER_ID, USER_ID_VALUE))
            .andExpect(status().isOk());

        verify(projectService).removeUserFromProject(anyLong(), anyLong());
    }

    @Test
    public void removeUserFromProject_InvalidInput_StatusNotFound() throws Exception {
        doThrow(NotFoundException.class)
            .when(projectService).removeUserFromProject(anyLong(), anyLong());
        mockMvc.perform(delete("/projects/" + SIMPLE_PROJECT_ID + "/user/" + SIMPLE_USER_ID)
            .param(USER_ID, USER_ID_VALUE))
            .andExpect(status().isNotFound());

        verify(projectService).removeUserFromProject(anyLong(), anyLong());
    }

    @Test
    public void removeUserFromProject_ValidInput_StatusForbidden() throws Exception {
        doThrow(ProjectClosedException.class)
            .when(projectService).removeUserFromProject(anyLong(), anyLong());
        mockMvc.perform(delete("/projects/" + SIMPLE_PROJECT_ID + "/user/" + SIMPLE_USER_ID)
            .param(USER_ID, USER_ID_VALUE))
            .andExpect(status().isForbidden());

        verify(projectService).removeUserFromProject(anyLong(), anyLong());
    }
}