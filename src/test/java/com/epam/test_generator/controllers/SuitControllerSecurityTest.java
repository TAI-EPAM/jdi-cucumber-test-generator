package com.epam.test_generator.controllers;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epam.test_generator.DatabaseConfigForTests;
import com.epam.test_generator.config.WebConfig;
import com.epam.test_generator.config.security.JwtAuthenticationFilter;
import com.epam.test_generator.controllers.suit.SuitController;
import com.epam.test_generator.controllers.user.request.LoginUserDTO;
import com.epam.test_generator.entities.Project;
import com.epam.test_generator.entities.Role;
import com.epam.test_generator.entities.User;
import com.epam.test_generator.services.LoginService;
import com.epam.test_generator.services.ProjectService;
import com.epam.test_generator.services.SuitService;
import com.epam.test_generator.services.UserService;
import com.google.common.collect.Lists;
import javax.servlet.Filter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {WebConfig.class, DatabaseConfigForTests.class})
@WebAppConfiguration
@Transactional
public class SuitControllerSecurityTest {

    private final LoginUserDTO loginUserDTO = new LoginUserDTO();


    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Mock
    private User invalidUser;

    @Mock
    private User validUser;

    @Mock
    private Role role;

    @Autowired
    private LoginService loginService;

    @Mock
    private UserService userService;

    @Mock
    private ProjectService projectService;

    @Mock
    private SuitService suitService;

    @Autowired
    private SuitController suitController;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private Filter springSecurityFilterChain;

    private MockMvc mvc;

    private Project project1 = new Project();

    private Project project2 = new Project();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mvc = MockMvcBuilders
            .webAppContextSetup(context)
            .addFilters(springSecurityFilterChain)
            .build();

        loginUserDTO.setEmail("test@email.com");
        loginUserDTO.setPassword("test");

        when(invalidUser.getName()).thenReturn("testInvalidName");
        when(invalidUser.getSurname()).thenReturn("testValidName");
        when(invalidUser.getEmail()).thenReturn("test@email.com");
        when(invalidUser.getPassword()).thenReturn("test");
        when(invalidUser.getRole()).thenReturn(role);
        when(role.getName()).thenReturn("GUEST");
        when(invalidUser.getId()).thenReturn(new Long(0));
        when(invalidUser.getLoginAttempts()).thenReturn(5);
        when(validUser.getEmail()).thenReturn("test@email.com");
        when(validUser.getPassword()).thenReturn("test");
        when(validUser.getId()).thenReturn(new Long(1));
        when(validUser.getRole()).thenReturn(new Role("GUEST"));
        when(validUser.isLocked()).thenReturn(false);

        ReflectionTestUtils.setField(loginService, "userService", userService);
        project1.setId(1L);
        project2.setId(2L);

        when(suitService.getSuitsFromProject(anyLong())).thenReturn(Lists.newArrayList());

        ReflectionTestUtils.setField(suitController, "suitService", suitService);
        ReflectionTestUtils.setField(jwtAuthenticationFilter, "userService", userService);
        ReflectionTestUtils
            .setField(jwtAuthenticationFilter, "projectService", projectService);
    }

    @Test
    public void getSuits_SimpleSuits_StatusOk() throws Exception {
        when(userService.getUserById(anyLong())).thenReturn(validUser);
        when(userService.getUserByEmail(anyString())).thenReturn(validUser);
        when(userService.isSamePasswords(anyString(), anyString())).thenReturn(true);
        when(projectService.getProjectsByUserId(anyLong())).thenReturn(Lists.newArrayList());

        String token = "Bearer " + loginService.getLoginJWTToken(loginUserDTO);

        mvc.perform(get("/suits").header("Authorization", token).contentType("application/json"))
            .andExpect(status().isOk());
    }

    @Test
    public void getSuits_NoSuchUser_StatusForbidden() throws Exception {
        when(userService.getUserById(anyLong())).thenReturn(null);
        when(userService.getUserByEmail(anyString())).thenReturn(invalidUser);
        when(userService.isSamePasswords(anyString(), anyString())).thenReturn(true);
        when(projectService.getProjectsByUserId(anyLong())).thenReturn(Lists.newArrayList());

        String token = "Bearer " + loginService.getLoginJWTToken(loginUserDTO);

        mvc.perform(get("/suits").header("Authorization", token).contentType("application/json"))
            .andExpect(status().isForbidden());
    }

    @Test
    public void getSuits_NoToken_StatusForbidden() throws Exception {
        when(userService.getUserById(anyLong())).thenReturn(validUser);
        when(userService.getUserByEmail(anyString())).thenReturn(validUser);
        when(userService.isSamePasswords(anyString(), anyString())).thenReturn(true);
        when(projectService.getProjectsByUserId(anyLong())).thenReturn(Lists.newArrayList());

        mvc.perform(get("/suits").contentType("application/json"))
            .andExpect(status().isForbidden());
    }

    @Test
    public void getSuits_InvalidToken_StatusForbidden() throws Exception {
        when(userService.getUserById(anyLong())).thenReturn(validUser);
        when(userService.getUserByEmail(anyString())).thenReturn(validUser);
        when(userService.isSamePasswords(anyString(), anyString())).thenReturn(true);
        when(projectService.getProjectsByUserId(anyLong())).thenReturn(Lists.newArrayList());

        String token =
            "Bearer " + loginService.getLoginJWTToken(loginUserDTO) + "something invalid";

        mvc.perform(get("/suits").header("Authorization", token).contentType("application/json"))
            .andExpect(status().isForbidden());
    }

    @Test
    public void getSuits_LockedUserWithToken_StatusForbidden() throws Exception {
        when(userService.getUserById(anyLong())).thenReturn(validUser);
        when(userService.getUserByEmail(anyString())).thenReturn(validUser);
        when(userService.isSamePasswords(anyString(), anyString())).thenReturn(true);
        when(projectService.getProjectsByUserId(anyLong())).thenReturn(Lists.newArrayList());

        String token = "Bearer " + loginService.getLoginJWTToken(loginUserDTO);

        when(validUser.isLocked()).thenReturn(true);

        mvc.perform(get("/projects/" + 1L + "/suits").header("Authorization", token).contentType("application/json"))
            .andExpect(status().isForbidden());
    }

    @Test
    public void getSuits_UserNotBelongsToThisProject_StatusForbidden() throws Exception {
        when(userService.getUserById(anyLong())).thenReturn(validUser);
        when(userService.getUserByEmail(anyString())).thenReturn(validUser);
        when(userService.isSamePasswords(anyString(), anyString())).thenReturn(true);

        when(projectService.getProjectsByUserId(anyLong())).thenReturn(Lists.newArrayList(
            project1, project2));

        String token = "Bearer " + loginService.getLoginJWTToken(loginUserDTO);

        mvc.perform(get("/projects/" + 3L + "/suits").header("Authorization", token)
            .contentType("application/json"))
            .andExpect(status().isForbidden());
    }

    @Test
    public void getSuits_UserBelongsToThisProject_StatusOk() throws Exception {
        when(userService.getUserById(anyLong())).thenReturn(validUser);
        when(userService.getUserByEmail(anyString())).thenReturn(validUser);
        when(userService.isSamePasswords(anyString(), anyString())).thenReturn(true);

        when(projectService.getProjectsByUserId(anyLong())).thenReturn(Lists.newArrayList(
            project1, project2));

        String token = "Bearer " + loginService.getLoginJWTToken(loginUserDTO);

        mvc.perform(get("/projects/" + 2L + "/suits").header("Authorization", token)
            .contentType("application/json"))
            .andExpect(status().isOk());
    }
}