package com.epam.test_generator.controllers;

import com.epam.test_generator.DatabaseConfigForTests;
import com.epam.test_generator.config.WebConfig;
import com.epam.test_generator.config.security.JwtAuthenticationProvider;
import com.epam.test_generator.dao.interfaces.UserDAO;
import com.epam.test_generator.dto.ChangeUserRoleDTO;
import com.epam.test_generator.dto.LoginUserDTO;
import com.epam.test_generator.entities.Role;
import com.epam.test_generator.entities.User;
import com.epam.test_generator.services.AdminService;
import com.epam.test_generator.services.ProjectService;
import com.epam.test_generator.services.RoleService;
import com.epam.test_generator.services.LoginService;
import com.epam.test_generator.services.UserService;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.Filter;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {WebConfig.class, DatabaseConfigForTests.class})
@WebAppConfiguration
public class AdminControllerSecurityTest {

    private final LoginUserDTO loginUserDTO = new LoginUserDTO();
    private final ChangeUserRoleDTO changeUserRoleDTO = new ChangeUserRoleDTO();

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private PasswordEncoder encoder;

    @Mock
    private ProjectService projectService;

    @InjectMocks
    @Autowired
    private JwtAuthenticationProvider jwtAuthenticationProvider;

    @Mock
    private User user;

    @Mock
    private User passiveUser;

    @Mock
    private UserService userService;

    @InjectMocks
    @Autowired
    private LoginService loginService;

    @Mock
    private RoleService roleService;

    @InjectMocks
    @Autowired
    private AdminService adminService;


    @Autowired
    private WebApplicationContext context;

    @Autowired
    private Filter springSecurityFilterChain;

    private MockMvc mvc;


    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mvc = MockMvcBuilders
            .webAppContextSetup(context)
            .addFilters(springSecurityFilterChain)
            .build();

        loginUserDTO.setEmail("test@email.com");
        loginUserDTO.setPassword("test");

        changeUserRoleDTO.setEmail("admin@email.com");
        changeUserRoleDTO.setRole("TEST_LEAD");

        when(passiveUser.getEmail()).thenReturn("admin@email.com");
        when(passiveUser.getRole()).thenReturn(new Role("GUEST"));

        when(user.getEmail()).thenReturn("test@email.com");
        when(user.getPassword()).thenReturn("test");
        when(user.getId()).thenReturn(new Long(1));
        when(user.isLocked()).thenReturn(false);

        when(projectService.getProjectByProjectId(anyLong())).thenReturn(null);

        ReflectionTestUtils.setField(adminService, "userService", userService);
        ReflectionTestUtils.setField(adminService, "roleService", roleService);
        ReflectionTestUtils.setField(loginService, "userService", userService);
    }

    @Test
    public void getUsers_SimpleUser_StatusOk() throws Exception {
        when(user.getRole()).thenReturn(new Role("ADMIN"));
        when(userService.getUserById(anyLong())).thenReturn(user);
        when(userService.getUserByEmail(anyString())).thenReturn(user);
        when(userService.isSamePasswords(anyString(), anyString())).thenReturn(true);

        final String token = "Bearer " + loginService.getLoginJWTToken(loginUserDTO);

        mvc.perform(
            get("/admin/users").header("Authorization", token).contentType("application/json"))
            .andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    public void getUsers_UserWithWrongRole_StatusForbidden() throws Exception {

        when(user.getRole()).thenReturn(new Role("GUEST"));
        when(userService.getUserById(anyLong())).thenReturn(user);
        when(userService.getUserByEmail(anyString())).thenReturn(user);
        when(userService.isSamePasswords(anyString(), anyString())).thenReturn(true);

        final String token = "Bearer " + loginService.getLoginJWTToken(loginUserDTO);

        mvc.perform(
            get("/admin/users").header("Authorization", token).contentType("application/json"))
            .andDo(print())
            .andExpect(status().isForbidden());
    }

    @Test
    public void changeUserRole_UserWithRightRole_StatusOk() throws Exception {
        when(user.getRole()).thenReturn(new Role("ADMIN"));
        when(userService.getUserById(anyLong())).thenReturn(user);
        when(userService.getUserByEmail(anyString())).thenReturn(user);
        when(userService.isSamePasswords(anyString(), anyString())).thenReturn(true);

        final String token = "Bearer " + loginService.getLoginJWTToken(loginUserDTO);

        when(userService.getUserByEmail(eq("admin@email.com"))).thenReturn(passiveUser);
        when(roleService.getRoleByName(changeUserRoleDTO.getRole()))
            .thenReturn(new Role("TEST_LEAD"));

        final String json = new ObjectMapper().writeValueAsString(changeUserRoleDTO);

        mvc.perform(put("/admin/changeroles").header("Authorization", token).content(json)
            .contentType("application/json"))
            .andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    public void changeUserRole_UserWithWrongRole_StatusBadRequest() throws Exception {
        when(user.getRole()).thenReturn(new Role("ADMIN"));
        when(userService.getUserById(anyLong())).thenReturn(user);
        when(userService.getUserByEmail(anyString())).thenReturn(user);
        when(userService.isSamePasswords(anyString(), anyString())).thenReturn(true);

        final String token = "Bearer " + loginService.getLoginJWTToken(loginUserDTO);

        when(userService.getUserByEmail(eq("admin@email.com"))).thenReturn(passiveUser);
        when(roleService.getRoleByName(changeUserRoleDTO.getRole())).thenReturn(null);

        final String json = new ObjectMapper().writeValueAsString(changeUserRoleDTO);

        mvc.perform(put("/admin/changeroles").header("Authorization", token).content(json)
            .contentType("application/json"))
            .andDo(print())
            .andExpect(status().isBadRequest());
    }

    @Test
    public void changeUserRole_UserWithWrongRole_StatusForbidden() throws Exception {
        when(user.getRole()).thenReturn(new Role("GUEST"));
        when(userService.getUserById(anyLong())).thenReturn(user);
        when(userService.getUserByEmail(anyString())).thenReturn(user).thenReturn(passiveUser);
        when(userService.isSamePasswords(anyString(), anyString())).thenReturn(true);

        final String token = "Bearer " + loginService.getLoginJWTToken(loginUserDTO);

        final String json = new ObjectMapper().writeValueAsString(changeUserRoleDTO);

        mvc.perform(put("/admin/changeroles").header("Authorization", token).content(json)
            .contentType("application/json"))
            .andDo(print())
            .andExpect(status().isForbidden());
    }

}