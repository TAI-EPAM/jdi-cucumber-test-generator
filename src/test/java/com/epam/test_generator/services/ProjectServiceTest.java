package com.epam.test_generator.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.epam.test_generator.config.security.AuthenticatedUser;
import com.epam.test_generator.dao.interfaces.ProjectDAO;
import com.epam.test_generator.dto.ProjectDTO;
import com.epam.test_generator.dto.ProjectFullDTO;
import com.epam.test_generator.dto.UserDTO;
import com.epam.test_generator.entities.Project;
import com.epam.test_generator.entities.Role;
import com.epam.test_generator.entities.User;
import com.epam.test_generator.services.exceptions.BadRequestException;
import com.epam.test_generator.services.exceptions.NotFoundException;
import com.epam.test_generator.services.exceptions.ProjectClosedException;
import com.epam.test_generator.transformers.ProjectFullTransformer;
import com.epam.test_generator.transformers.ProjectTransformer;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
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
    private ProjectFullTransformer projectFullTransformer;

    @Mock
    private UserService userService;

    @InjectMocks
    private ProjectService projectService;

    private List<Project> expectedProjects = new ArrayList<>();
    private User simpleUser1;
    private User simpleUser2;
    private Set<User> userSet;
    private Project simpleProject1;
    private Project simpleProject2;
    private ProjectDTO simpleProject1DTO;
    private ProjectDTO simpleProject2DTO;
    private List<ProjectDTO> expectedProjectDTOs;
    private UserDTO simpleUserDTO1;
    private UserDTO simpleUserDTO2;
    private Set<UserDTO> userDTOSet;
    private ProjectFullDTO simpleProjectFullDTO1;

    private static final long SIMPLE_PROJECT_ID = 1L;
    private static final long SIMPLE_SUIT_ID = 2L;
    private static final long SIMPLE_USER_ID = 3L;

    @Before
    public void setUp() throws Exception {
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
        simpleUserDTO1 = new UserDTO(
            "testName3",
            "testSurname3",
            "testPassword1",
            "GUEST",
            "testUser1@mail.com"
        );
        simpleUserDTO1.setId(1L);
        simpleUserDTO2 = new UserDTO(
            "testName4",
            "testSurname4",
            "testPassword2",
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

        simpleProject2 = new Project(
            "project2",
            "project2",
            null,
            userSet,
            true
        );
        simpleProject2.setId(2L);
        simpleProject1DTO = new ProjectDTO(
            "project1",
            "project1",
            true,
            userDTOSet
        );
        simpleProject1DTO.setId(2L);
        simpleProject2DTO = new ProjectDTO(
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
    public void getProjects_expectedProjectsList() {
        when(projectDAO.findAll()).thenReturn(expectedProjects);
        when(projectTransformer.toDtoList(expectedProjects)).thenReturn(expectedProjectDTOs);
        final List<ProjectDTO> projects = projectService.getProjects();
        assertEquals(expectedProjectDTOs, projects);

        verify(projectDAO).findAll();

    }

    @Test
    public void getAuthUserProjects_expectedUserProjectsList() {
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
        final List<ProjectDTO> actualProjectDTOs = projectService
            .getAuthenticatedUserProjects(authentication);
        assertEquals(expectedProjectDTOs, actualProjectDTOs);

        verify(userService, times(2)).getUserById(eq(simpleUser1.getId()));
        verify(projectTransformer).toDtoList(eq(expectedProjects));
    }

    @Test(expected = NotFoundException.class)
    public void getAuthUserProjects_expectedUserNotFoundException() {
        when(authentication.getPrincipal()).thenReturn(new AuthenticatedUser(
            null,
            simpleUser1.getEmail(),
            null,
            null,
            null,
            false
        ));
        when(userService.getUserByEmail(simpleUser1.getEmail())).thenReturn(null);
        projectService.getAuthenticatedUserProjects(authentication);
    }

    @Test
    public void getProjectsByUserId_UserId_expectedUserProjectsList() {
        when(userService.getUserById(simpleUser1.getId())).thenReturn(simpleUser1);
        when(projectDAO.findByUsers(simpleUser1)).thenReturn(expectedProjects);

        final List<Project> actualProjectsList = projectService
            .getProjectsByUserId(simpleUser1.getId());
        assertEquals(expectedProjects, actualProjectsList);

        verify(userService).getUserById(eq(simpleUser1.getId()));
        verify(projectDAO).findByUsers(eq(simpleUser1));
    }

    @Test(expected = NotFoundException.class)
    public void getProjectsByUserId_InvalidUserId_expectedNoUserFoundException() {
        when(userService.getUserById(simpleUser1.getId())).thenReturn(null);
        when(projectDAO.findByUsers(simpleUser1)).thenReturn(expectedProjects);

        projectService.getProjectsByUserId(simpleUser1.getId());

        verify(userService).getUserById(eq(simpleUser1.getId()));
    }

    @Test
    public void getProjectByProjectId_ValidProjectId_expectedProject() {
        when(projectDAO.findOne(simpleProject1.getId())).thenReturn(simpleProject1);

        Project expectedProject = projectService
            .getProjectByProjectId(simpleProject1.getId());
        assertEquals(expectedProject, simpleProject1);

        verify(projectDAO).findOne(eq(simpleProject1.getId()));
    }

    @Test(expected = NotFoundException.class)
    public void getProjectByProjectId_InvalidProjectId_expectedNotFoundException() {
        when(projectDAO.findOne(simpleProject1.getId())).thenReturn(null);

        projectService.getProjectByProjectId(simpleProject1.getId());

        verify(projectDAO).findOne(eq(simpleProject1.getId()));

    }

    @Test
    public void getAuthUserFullProject_ProjectIdAndAuth_FullProject() {
        when(authentication.getPrincipal()).thenReturn(new AuthenticatedUser(
            null,
            simpleUser1.getEmail(),
            null,
            null,
            null,
            false
        ));
        when(projectDAO.findOne(simpleProject1.getId())).thenReturn(simpleProject1);
        when(userService.getUserByEmail(simpleUser1.getEmail())).thenReturn(simpleUser1);
        when(projectService.getProjectByProjectId(simpleProject1.getId())).thenReturn(simpleProject1);
        when(projectFullTransformer.toDto(simpleProject1)).thenReturn(simpleProjectFullDTO1);
        ProjectFullDTO actualFullProject = projectService
            .getAuthUserFullProject(simpleProjectFullDTO1.getId(), authentication);
        assertEquals(simpleProjectFullDTO1, actualFullProject);

        verify(userService).getUserByEmail(eq(simpleUser1.getEmail()));
        verify(projectFullTransformer).toDto(eq(simpleProject1));
    }

    @Test(expected = BadRequestException.class)
    public void getAuthUserFullProject_UserDoesntBelongsToSuit_BadRequestException() {
        simpleProject1.setUsers(Stream.of(simpleUser2).collect(Collectors.toSet()));
        when(authentication.getPrincipal()).thenReturn(new AuthenticatedUser(
            null,
            simpleUser1.getEmail(),
            null,
            null,
            null,
            false
        ));
        when(projectDAO.findOne(simpleProject1.getId())).thenReturn(simpleProject1);
        when(userService.getUserByEmail(simpleUser1.getEmail())).thenReturn(simpleUser1);
        when(projectService.getProjectByProjectId(simpleProject1.getId())).thenReturn(simpleProject1);
        projectService.getAuthUserFullProject(simpleProjectFullDTO1.getId(), authentication);
    }

    @Test
    public void createProject_valid_Success() {
        Project actualProject = new Project(
            "name",
            "description",
            null,
            Sets.newHashSet(simpleUser2),
            false);

        when(projectTransformer.fromDto(any(ProjectDTO.class))).thenReturn(actualProject);
        when(authentication.getPrincipal()).thenReturn(new AuthenticatedUser(
            null, simpleUser1.getEmail(), null, null, null, false));
        when(userService.getUserByEmail(simpleUser1.getEmail())).thenReturn(simpleUser1);
        when(projectDAO.save(any(Project.class))).thenReturn(simpleProject1);

        Long actualId = projectService.createProject(simpleProject1DTO, authentication);

        verify(projectTransformer).fromDto(simpleProject1DTO);
        verify(projectDAO).save(actualProject);
        assertTrue(actualProject.isActive());
        assertTrue(actualProject.getUsers().contains(simpleUser1));
        assertTrue(actualProject.getUsers().contains(simpleUser2));
        assertEquals(simpleProject1.getId(), actualId);
    }

    @Test
    public void updateProject_valid_Success() {
        when(projectDAO.findOne(SIMPLE_PROJECT_ID)).thenReturn(simpleProject1);
        projectService.updateProject(SIMPLE_PROJECT_ID, simpleProject1DTO);
        verify(projectDAO).save(simpleProject1);
    }

    @Test(expected = NotFoundException.class)
    public void updateProject_invalidProjectId_NotFound() {
        when(projectDAO.findOne(SIMPLE_PROJECT_ID)).thenReturn(simpleProject1);
        projectService.updateProject(777L, simpleProject1DTO);
        verify(projectDAO).save(simpleProject1);
    }

    @Test
    public void removeProject_valid_Success() {
        when(projectDAO.findOne(SIMPLE_PROJECT_ID)).thenReturn(simpleProject1);
        projectService.removeProject(SIMPLE_PROJECT_ID);
        verify(projectDAO).delete(SIMPLE_PROJECT_ID);
    }

    @Test(expected = NotFoundException.class)
    public void removeProject_invalidProjectId_NotFound() {

        when(projectDAO.findOne(SIMPLE_PROJECT_ID)).thenReturn(simpleProject1);
        projectService.removeProject(777L);
    }

    @Test
    public void addUserToProject_valid_Success() {
        simpleProject1.setUsers(new HashSet<>());
        when(projectDAO.findOne(SIMPLE_PROJECT_ID)).thenReturn(simpleProject1);
        when(userService.getUserById(SIMPLE_USER_ID)).thenReturn(simpleUser1);

        assertFalse(simpleProject1.getUsers().contains(simpleUser1));
        projectService.addUserToProject(SIMPLE_PROJECT_ID, SIMPLE_USER_ID);
        assertTrue(simpleProject1.getUsers().contains(simpleUser1));
        verify(projectDAO).save(simpleProject1);
    }

    @Test(expected = NotFoundException.class)
    public void addUserToProject_invalidProjectId_NotFound() {
        when(projectDAO.findOne(SIMPLE_PROJECT_ID)).thenReturn(simpleProject1);
        when(userService.getUserById(SIMPLE_USER_ID)).thenReturn(simpleUser1);

        projectService.addUserToProject(777L, SIMPLE_USER_ID);
    }

    @Test(expected = NotFoundException.class)
    public void addUserToProject_invalidUserId_NotFound() {
        when(projectDAO.findOne(SIMPLE_PROJECT_ID)).thenReturn(simpleProject1);
        when(userService.getUserById(SIMPLE_USER_ID)).thenReturn(simpleUser1);

        projectService.addUserToProject(SIMPLE_PROJECT_ID, 666L);
    }

    @Test
    public void removeUserFromProject_valid_Success() {
        simpleProject1.setUsers(Sets.newHashSet(simpleUser1));
        when(projectDAO.findOne(SIMPLE_PROJECT_ID)).thenReturn(simpleProject1);
        when(userService.getUserById(SIMPLE_USER_ID)).thenReturn(simpleUser1);

        assertTrue(simpleProject1.getUsers().contains(simpleUser1));
        projectService.removeUserFromProject(SIMPLE_PROJECT_ID, SIMPLE_USER_ID);
        assertFalse(simpleProject1.getUsers().contains(simpleUser1));
        verify(projectDAO).save(simpleProject1);
    }

    @Test (expected = NotFoundException.class)
    public void removeUserFromProject_invalidProjectId_NotFound() {
        simpleProject1.setUsers(Sets.newHashSet(simpleUser1));
        when(projectDAO.findOne(SIMPLE_PROJECT_ID)).thenReturn(simpleProject1);
        when(userService.getUserById(SIMPLE_USER_ID)).thenReturn(simpleUser1);

        projectService.removeUserFromProject(777L, SIMPLE_USER_ID);
    }

    @Test (expected = NotFoundException.class)
    public void removeUserFromProject_invalidUserId_NotFound() {
        simpleProject1.setUsers(Sets.newHashSet(simpleUser1));
        when(projectDAO.findOne(SIMPLE_PROJECT_ID)).thenReturn(simpleProject1);
        when(userService.getUserById(SIMPLE_USER_ID)).thenReturn(simpleUser1);

        projectService.removeUserFromProject(SIMPLE_PROJECT_ID, 666L);
    }

    @Test
    public void closeProject_validProjectId_Success() {
        when(projectDAO.findOne(SIMPLE_PROJECT_ID)).thenReturn(simpleProject1);
        assertTrue(simpleProject1.isActive());
        projectService.closeProject(SIMPLE_PROJECT_ID);
        assertFalse(simpleProject1.isActive());
        verify(projectDAO).save(simpleProject1);
    }

    @Test(expected = NotFoundException.class)
    public void closeProject_invalidProjectId_NotFound() {
        when(projectDAO.findOne(SIMPLE_PROJECT_ID)).thenReturn(simpleProject1);
        projectService.closeProject(777L);
    }

    @Test(expected = ProjectClosedException.class)
    public void closeProject_ProjectIsClosed_FailReadOnly() {
        simpleProject1.setActive(false);
        when(projectDAO.findOne(SIMPLE_PROJECT_ID)).thenReturn(simpleProject1);
        projectService.closeProject(SIMPLE_PROJECT_ID);
    }


}