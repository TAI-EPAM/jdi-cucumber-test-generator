package com.epam.test_generator.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.epam.test_generator.config.security.AuthenticatedUser;
import com.epam.test_generator.controllers.project.ProjectTransformer;
import com.epam.test_generator.controllers.project.request.ProjectCreateDTO;
import com.epam.test_generator.controllers.project.request.ProjectUpdateDTO;
import com.epam.test_generator.controllers.user.response.UserDTO;
import com.epam.test_generator.dao.interfaces.ProjectDAO;
import com.epam.test_generator.controllers.project.response.ProjectDTO;
import com.epam.test_generator.controllers.project.response.ProjectFullDTO;
import com.epam.test_generator.entities.Project;
import com.epam.test_generator.entities.Role;
import com.epam.test_generator.entities.User;
import com.epam.test_generator.services.exceptions.BadRequestException;
import com.epam.test_generator.services.exceptions.NotFoundException;
import com.epam.test_generator.services.exceptions.ProjectClosedException;
import com.epam.test_generator.services.exceptions.UnauthorizedException;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.Authentication;

@RunWith(MockitoJUnitRunner.class)
public class ProjectServiceTest {

    @Mock
    private Authentication authentication;

    @Mock
    private ProjectDAO projectDAO;

    @Mock
    private ProjectTransformer projectTransformer;

    @Mock
    private UserService userService;

    @InjectMocks
    private ProjectService projectService;

    private List<Project> expectedProjects = new ArrayList<>();
    private User simpleUser1;
    private User simpleUser2;
    private Set<User> userSet;
    private Project simpleProject1;
    private List<ProjectDTO> expectedProjectDTOs;
    private Set<UserDTO> userDTOSet;
    private ProjectFullDTO simpleProjectFullDTO1;
    private ProjectCreateDTO projectCreateDTO;
    private ProjectUpdateDTO projectUpdateDTO;

    private static final long PROJECT_ID = 777L;
    private static final long USER_ID = 666L;
    private static final long SIMPLE_PROJECT_ID = 1L;
    private static final long SIMPLE_USER_ID = 3L;

    private static final String CREATE_NAME = "NAME";
    private static final String CREATE_DESCRIPTION = "DESCRIPTION";

    private static final String UPDATE_NAME = "NAME1";
    private static final String UPDATE_DESCRIPTION = "DESCRIPTION1";

    @Before
    public void setUp() {
        simpleUser1 = new User(
            "testName1",
            "testSurname1",
            "testUser1@mail.com",
            "testPassword1",
            new Role("GUEST")
        );
        simpleUser1.setId(1L);
        simpleUser2 = new User(
            "testName2",
            "testSurname2",
            "testUser2@mail.com",
            "testPassword2",
            new Role("GUEST")
        );
        simpleUser2.setId(2L);
        UserDTO simpleUserDTO1 = new UserDTO(
            "testName3",
            "testSurname3",
            "GUEST",
            "testUser1@mail.com"
        );
        simpleUserDTO1.setId(1L);
        UserDTO simpleUserDTO2 = new UserDTO(
            "testName4",
            "testSurname4",
            "GUEST",
            "testUser2@mail.com"
        );
        simpleUserDTO2.setId(2L);
        simpleProject1 = new Project(
            "project1",
            "project1",
            null,
            userSet,
            true
        );
        simpleProject1.setId(SIMPLE_PROJECT_ID);

        Project simpleProject2 = new Project(
            "project2",
            "project2",
            null,
            userSet,
            true
        );
        simpleProject2.setId(2L);
        ProjectDTO simpleProject1DTO = new ProjectDTO(
            "project1",
            "project1",
            true,
            userDTOSet
        );
        simpleProject1DTO.setId(2L);
        ProjectDTO simpleProject2DTO = new ProjectDTO(
            "project2",
            "project2",
            true,
            userDTOSet
        );
        simpleProject2DTO.setId(2L);
        simpleProjectFullDTO1 = new ProjectFullDTO(
            "project1",
            "project1",
            null,
            userDTOSet,
            true
        );
        simpleProjectFullDTO1.setId(1L);

        projectCreateDTO = new ProjectCreateDTO();
        projectCreateDTO.setName(CREATE_NAME);
        projectCreateDTO.setDescription(CREATE_DESCRIPTION);

        projectUpdateDTO = new ProjectUpdateDTO();
        projectUpdateDTO.setName(UPDATE_NAME);
        projectUpdateDTO.setDescription(UPDATE_DESCRIPTION);

        userSet = Stream.of(simpleUser1, simpleUser2)
            .collect(Collectors.toSet());
        simpleProject1.setUsers(userSet);
        simpleProject2.setUsers(userSet);
        userDTOSet = Stream.of(simpleUserDTO1, simpleUserDTO2)
            .collect(Collectors.toSet());
        simpleProject1DTO.setUsers(userDTOSet);
        simpleProject2DTO.setUsers(userDTOSet);
        simpleProjectFullDTO1.setUsers(userDTOSet);
        expectedProjectDTOs = Stream.of(simpleProject1DTO, simpleProject2DTO)
            .collect(Collectors.toList());
        expectedProjects = Stream.of(simpleProject1, simpleProject2)
            .collect(Collectors.toList());
    }

    @Test
    public void get_AllProjects_Success() {
        when(projectDAO.findAll()).thenReturn(expectedProjects);
        when(projectTransformer.toDtoList(expectedProjects)).thenReturn(expectedProjectDTOs);
        List<ProjectDTO> projects = projectService.getProjects();
        assertEquals(expectedProjectDTOs, projects);

        verify(projectDAO).findAll();

    }

    @Test
    public void get_AuthUserProjects_Success() {
        when(authentication.getPrincipal()).thenReturn(new AuthenticatedUser(
            simpleUser1.getId(),
            simpleUser1.getEmail(),
            null,
            null,
            null,
            false
        ));
        when(userService.getUserById(simpleUser1.getId())).thenReturn(simpleUser1);
        when(projectService.getProjectsByUserId(simpleUser1.getId())).thenReturn(expectedProjects);
        when(projectTransformer.toDtoList(expectedProjects)).thenReturn(expectedProjectDTOs);
        List<ProjectDTO> actualProjectDTOs = projectService
            .getAuthenticatedUserProjects(authentication);
        assertEquals(expectedProjectDTOs, actualProjectDTOs);

        verify(userService, times(2)).getUserById(eq(simpleUser1.getId()));
        verify(projectTransformer).toDtoList(eq(expectedProjects));
    }

    @Test(expected = UnauthorizedException.class)
    public void get_AuthUserProjects_NotFoundException() {
        when(authentication.getPrincipal()).thenReturn(new AuthenticatedUser(
            simpleUser1.getId(),
            simpleUser1.getEmail(),
            null,
            null,
            null,
            false
        ));

        when(userService.getUserById(anyLong())).thenThrow(UnauthorizedException.class);
        projectService.getAuthenticatedUserProjects(authentication);
    }

    @Test
    public void getProjects_ByUserId_Success() {
        when(userService.getUserById(simpleUser1.getId())).thenReturn(simpleUser1);
        when(projectDAO.findByUsers(simpleUser1)).thenReturn(expectedProjects);

        List<Project> actualProjectsList = projectService
            .getProjectsByUserId(simpleUser1.getId());
        assertEquals(expectedProjects, actualProjectsList);

        verify(userService).getUserById(eq(simpleUser1.getId()));
        verify(projectDAO).findByUsers(eq(simpleUser1));
    }

    @Test(expected = UnauthorizedException.class)
    public void getProjectsByUserId_InvalidUserId_NotFoundException() {
        when(userService.getUserById(simpleUser1.getId())).thenThrow(UnauthorizedException.class);

        projectService.getProjectsByUserId(simpleUser1.getId());

    }

    @Test
    public void getProjectByProjectId_ValidProjectId_Success() {
        when(projectDAO.findById(simpleProject1.getId())).thenReturn(Optional.of(simpleProject1));

        Project expectedProject = projectService
            .getProjectByProjectId(simpleProject1.getId());
        assertEquals(expectedProject, simpleProject1);

        verify(projectDAO).findById(eq(simpleProject1.getId()));
    }

    @Test(expected = NotFoundException.class)
    public void getProjectByProjectId_InvalidProjectId_NotFoundException() {
        when(projectDAO.findById(simpleProject1.getId())).thenReturn(Optional.empty());

        projectService.getProjectByProjectId(simpleProject1.getId());

        verify(projectDAO).findById(eq(simpleProject1.getId()));

    }

    @Test
    public void getAuthUserFullProject_ProjectIdAndAuth_Success() {
        when(authentication.getPrincipal()).thenReturn(new AuthenticatedUser(
            null,
            simpleUser1.getEmail(),
            null,
            null,
            null,
            false
        ));
        when(userService.getUserByEmail(simpleUser1.getEmail())).thenReturn(simpleUser1);
        when(projectDAO.findById(simpleProject1.getId())).thenReturn(Optional.of(simpleProject1));
        when(projectTransformer.toFullDto(simpleProject1)).thenReturn(simpleProjectFullDTO1);
        ProjectFullDTO actualFullProject = projectService
            .getAuthUserFullProject(simpleProjectFullDTO1.getId(), authentication);
        assertEquals(simpleProjectFullDTO1, actualFullProject);
        verify(userService).getUserByEmail(eq(simpleUser1.getEmail()));
        verify(projectTransformer).toFullDto(eq(simpleProject1));
    }

    @Test(expected = BadRequestException.class)
    public void getAuthUserFullProject_UserDoesNotBelongsToSuit_BadRequestException() {
        simpleProject1.setUsers(Stream.of(simpleUser2).collect(Collectors.toSet()));
        when(authentication.getPrincipal()).thenReturn(new AuthenticatedUser(
            null,
            simpleUser1.getEmail(),
            null,
            null,
            null,
            false
        ));
        when(projectDAO.findById(simpleProject1.getId())).thenReturn(Optional.of(simpleProject1));
        when(userService.getUserByEmail(simpleUser1.getEmail())).thenReturn(simpleUser1);
        projectService.getAuthUserFullProject(simpleProjectFullDTO1.getId(), authentication);
    }

    @Test
    public void create_Project_Success() {
        Project actualProject = new Project(
            CREATE_NAME,
            CREATE_DESCRIPTION,
            null,
             null,
            false);

        when(projectTransformer.fromDto(any(ProjectCreateDTO.class))).thenReturn(actualProject);
        when(authentication.getPrincipal()).thenReturn(new AuthenticatedUser(
            null, simpleUser1.getEmail(), null, null, null, false));
        when(userService.getUserByEmail(simpleUser1.getEmail())).thenReturn(simpleUser1);
        when(projectDAO.save(any(Project.class))).thenReturn(simpleProject1);

        projectService.createProject(projectCreateDTO, authentication);

        assertTrue(actualProject.isActive());
        assertTrue(actualProject.getUsers().contains(simpleUser1));
        assertEquals(actualProject, projectTransformer.fromDto(projectCreateDTO));
    }

    @Test
    public void update_Project_Success() {
        when(projectDAO.findById(SIMPLE_PROJECT_ID)).thenReturn(Optional.of(simpleProject1));
        projectService.updateProject(SIMPLE_PROJECT_ID, projectUpdateDTO);
        verify(projectDAO).save(simpleProject1);
    }

    @Test(expected = NotFoundException.class)
    public void updateProject_InvalidProjectId_NotFound() {
        projectService.updateProject(PROJECT_ID, projectUpdateDTO);
        verify(projectDAO).save(simpleProject1);
    }

    @Test
    public void remove_Project_Success() {
        when(projectDAO.findById(SIMPLE_PROJECT_ID)).thenReturn(Optional.of(simpleProject1));
        projectService.removeProject(SIMPLE_PROJECT_ID);
        verify(projectDAO).delete(simpleProject1);
    }

    @Test(expected = NotFoundException.class)
    public void removeProject_InvalidProjectId_NotFound() {

        projectService.removeProject(PROJECT_ID);
    }

    @Test
    public void addUserToProject_ValidUser_Success() {
        simpleProject1.setUsers(new HashSet<>());
        when(projectDAO.findById(SIMPLE_PROJECT_ID)).thenReturn(Optional.of(simpleProject1));
        when(userService.getUserById(SIMPLE_USER_ID)).thenReturn(simpleUser1);

        assertFalse(simpleProject1.getUsers().contains(simpleUser1));
        projectService.addUserToProject(SIMPLE_PROJECT_ID, SIMPLE_USER_ID);
        assertTrue(simpleProject1.getUsers().contains(simpleUser1));
        verify(projectDAO).save(simpleProject1);
    }

    @Test(expected = NotFoundException.class)
    public void addUserToProject_InvalidProjectId_NotFound() {
        projectService.addUserToProject(PROJECT_ID, SIMPLE_USER_ID);
    }

    @Test(expected = NotFoundException.class)
    public void addUserToProject_InvalidUserId_NotFound() {
        when(projectDAO.findById(SIMPLE_PROJECT_ID)).thenReturn(Optional.empty());

        projectService.addUserToProject(SIMPLE_PROJECT_ID, USER_ID);
    }

    @Test
    public void removeUserFromProject_ValidUser_Success() {
        simpleProject1.setUsers(Sets.newHashSet(simpleUser1));
        when(projectDAO.findById(SIMPLE_PROJECT_ID)).thenReturn(Optional.of(simpleProject1));
        when(userService.getUserById(SIMPLE_USER_ID)).thenReturn(simpleUser1);

        assertTrue(simpleProject1.getUsers().contains(simpleUser1));
        projectService.removeUserFromProject(SIMPLE_PROJECT_ID, SIMPLE_USER_ID);
        assertFalse(simpleProject1.getUsers().contains(simpleUser1));
        verify(projectDAO).save(simpleProject1);
    }

    @Test (expected = NotFoundException.class)
    public void removeUserFromProject_InvalidProjectId_NotFound() {
        simpleProject1.setUsers(Sets.newHashSet(simpleUser1));

        projectService.removeUserFromProject(PROJECT_ID, SIMPLE_USER_ID);
    }

    @Test (expected = BadRequestException.class)
    public void removeUserFromProject_InvalidUserId_NotFound() {
        simpleProject1.setUsers(Sets.newHashSet(simpleUser1));
        when(projectDAO.findById(SIMPLE_PROJECT_ID)).thenReturn(Optional.of(simpleProject1));

        projectService.removeUserFromProject(SIMPLE_PROJECT_ID, USER_ID);
    }

    @Test
    public void closeProject_ValidProjectId_Success() {
        when(projectDAO.findById(SIMPLE_PROJECT_ID)).thenReturn(Optional.of(simpleProject1));
        assertTrue(simpleProject1.isActive());
        projectService.closeProject(SIMPLE_PROJECT_ID);
        assertFalse(simpleProject1.isActive());
        verify(projectDAO).save(simpleProject1);
    }

    @Test(expected = NotFoundException.class)
    public void closeProject_InvalidProjectId_NotFound() {
        projectService.closeProject(PROJECT_ID);
    }

    @Test(expected = ProjectClosedException.class)
    public void closeProject_ProjectIsClosed_FailReadOnly() {
        simpleProject1.setActive(false);
        when(projectDAO.findById(SIMPLE_PROJECT_ID)).thenReturn(Optional.of(simpleProject1));
        projectService.closeProject(SIMPLE_PROJECT_ID);
    }


}