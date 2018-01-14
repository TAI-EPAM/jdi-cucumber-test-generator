package com.epam.test_generator.controllers;

import com.epam.test_generator.config.WebConfig;
import com.epam.test_generator.config.security.JwtAuthenticationProvider;
import com.epam.test_generator.dao.interfaces.RoleDAO;
import com.epam.test_generator.dao.interfaces.UserDAO;
import com.epam.test_generator.dto.ChangeUserRoleDTO;
import com.epam.test_generator.dto.LoginUserDTO;
import com.epam.test_generator.entities.Role;
import com.epam.test_generator.entities.User;
import com.epam.test_generator.services.RoleService;
import com.epam.test_generator.services.TokenService;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.Filter;

import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)

@ContextConfiguration(classes = {WebConfig.class})
@WebAppConfiguration
public class AdminControllerTest {

    private final LoginUserDTO loginUserDTO = new LoginUserDTO();
    private final ChangeUserRoleDTO changeUserRoleDTO = new ChangeUserRoleDTO();

    @Autowired
    UserDAO userDAO;
    @Autowired
    PasswordEncoder encoder;
    @InjectMocks
    @Autowired
    JwtAuthenticationProvider jwtAuthenticationProvider;
    @Mock
    private User user;
    @Mock
    private User passiveUser;
    @Mock
    private UserService userService;
    @InjectMocks
    @Autowired
    private TokenService tokenService;

    @Autowired
    RoleDAO roleDAO;

    @InjectMocks
    @Autowired
    RoleService roleService;

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

        changeUserRoleDTO.setEmail("test@email.com");
        changeUserRoleDTO.setRole("TEST_LEAD");

        when(passiveUser.getEmail()).thenReturn("test@email.com");

        when(user.getEmail()).thenReturn("test@email.com");
        when(user.getPassword()).thenReturn("test");
        when(user.getId()).thenReturn(new Long(1));

    }

    @Test
    public void getUsers_code200_statusOk() throws Exception {
        when(user.getRole()).thenReturn(new Role("ADMIN"));
        when(userService.getUserById(anyLong())).thenReturn(user);
        when(userService.getUserByEmail(anyString())).thenReturn(user);
        when(userService.isSamePasswords(anyString(), anyString())).thenReturn(true);

        final String token = "Bearer " + tokenService.getToken(loginUserDTO);

        mvc.perform(get("/admin/users").header("Authorization", token).contentType("application/json"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void getUsers_code403_statusForbidden() throws Exception {

        when(user.getRole()).thenReturn(new Role("GUEST"));
        when(userService.getUserById(anyLong())).thenReturn(user);
        when(userService.getUserByEmail(anyString())).thenReturn(user);
        when(userService.isSamePasswords(anyString(), anyString())).thenReturn(true);

        final String token = "Bearer " + tokenService.getToken(loginUserDTO);


        mvc.perform(get("/admin/users").header("Authorization", token).contentType("application/json"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }


    @Test
    public void changeUserRole_code200_ok() throws Exception {
        when(user.getRole()).thenReturn(new Role("ADMIN"));
        when(userService.getUserById(anyLong())).thenReturn(user);
        when(userService.getUserByEmail(anyString())).thenReturn(user);
        when(userService.isSamePasswords(anyString(), anyString())).thenReturn(true);

        final String token = "Bearer " + tokenService.getToken(loginUserDTO);

        final String json = new ObjectMapper().writeValueAsString(changeUserRoleDTO);


        when(userService.getUserByEmail("admin@admin.com")).thenReturn(passiveUser);
        when(roleService.getRoleByName(anyString())).thenReturn(new Role("TEST_LEAD"));

        mvc.perform(put("/admin/changeroles").header("Authorization", token).content(json).contentType("application/json"))
                .andDo(print())
                .andExpect(status().isOk());
    }


    @Test
    public void changeUserRole_code403_forbidden() throws Exception {
        when(user.getRole()).thenReturn(new Role("GUEST"));
        when(userService.getUserById(anyLong())).thenReturn(user);
        when(userService.getUserByEmail(anyString())).thenReturn(user);
        when(userService.isSamePasswords(anyString(), anyString())).thenReturn(true);

        final String token = "Bearer " + tokenService.getToken(loginUserDTO);

        final String json = new ObjectMapper().writeValueAsString(changeUserRoleDTO);


        when(userService.getUserByEmail("admin@admin.com")).thenReturn(passiveUser);

        mvc.perform(put("/admin/changeroles").header("Authorization", token).content(json).contentType("application/json"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

}