package com.epam.test_generator.controllers;

import com.epam.test_generator.DatabaseConfigForTests;
import com.epam.test_generator.config.WebConfig;
import com.epam.test_generator.config.security.JwtAuthenticationProvider;
import com.epam.test_generator.dao.interfaces.UserDAO;
import com.epam.test_generator.dto.LoginUserDTO;
import com.epam.test_generator.entities.Role;
import com.epam.test_generator.entities.User;
import com.epam.test_generator.services.LoginService;
import com.epam.test_generator.services.UserService;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.Filter;

import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {WebConfig.class, DatabaseConfigForTests.class})
@WebAppConfiguration
@Transactional
public class SuitControllerSecurityTest {

    private final LoginUserDTO loginUserDTO = new LoginUserDTO();

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private PasswordEncoder encoder;

    @InjectMocks
    @Autowired
    private JwtAuthenticationProvider jwtAuthenticationProvider;

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

        when(invalidUser.getName()).thenReturn("testInvalidName");
        when(invalidUser.getSurname()).thenReturn("testValidName");
        when(invalidUser.getEmail()).thenReturn("test@email.com");
        when(invalidUser.getPassword()).thenReturn("test");
        when(invalidUser.getRole()).thenReturn(role);
        when(role.getName()).thenReturn("GUEST");
        when(invalidUser.getId()).thenReturn(new Long(0));
        when(invalidUser.getAttempts()).thenReturn(5);
        when(validUser.getEmail()).thenReturn("test@email.com");
        when(validUser.getPassword()).thenReturn("test");
        when(validUser.getId()).thenReturn(new Long(1));
        when(validUser.getRole()).thenReturn(new Role("GUEST"));
        when(validUser.isLocked()).thenReturn(false);

        ReflectionTestUtils.setField(loginService, "userService", userService);
    }

    @Test
    public void getSuits_SimpleSuits_StatusOk() throws Exception {
        when(userService.getUserById(anyLong())).thenReturn(validUser);
        when(userService.getUserByEmail(anyString())).thenReturn(validUser);
        when(userService.isSamePasswords(anyString(), anyString())).thenReturn(true);

        String token = "Bearer " + loginService.getLoginJWTToken(loginUserDTO);

        mvc.perform(get("/suits").header("Authorization", token).contentType("application/json"))
            .andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    public void getSuits_NoSuchUser_StatusForbidden() throws Exception {
        when(userService.getUserById(anyLong())).thenReturn(null);
        when(userService.getUserByEmail(anyString())).thenReturn(invalidUser);
        when(userService.isSamePasswords(anyString(), anyString())).thenReturn(true);

        String token = "Bearer " + loginService.getLoginJWTToken(loginUserDTO);

        mvc.perform(get("/suits").header("Authorization", token).contentType("application/json"))
            .andDo(print())
            .andExpect(status().isForbidden());
    }

    @Test
    public void getSuits_NoToken_StatusForbidden() throws Exception {
        when(userService.getUserById(anyLong())).thenReturn(validUser);
        when(userService.getUserByEmail(anyString())).thenReturn(validUser);
        when(userService.isSamePasswords(anyString(), anyString())).thenReturn(true);

        mvc.perform(get("/suits").contentType("application/json"))
            .andDo(print())
            .andExpect(status().isForbidden());
    }

    @Test
    public void getSuits_InvalidToken_StatusForbidden() throws Exception {
        when(userService.getUserById(anyLong())).thenReturn(validUser);
        when(userService.getUserByEmail(anyString())).thenReturn(validUser);
        when(userService.isSamePasswords(anyString(), anyString())).thenReturn(true);

        String token = "Bearer " + loginService.getLoginJWTToken(loginUserDTO) + "something invalid";

        mvc.perform(get("/suits").header("Authorization", token).contentType("application/json"))
            .andDo(print())
            .andExpect(status().isForbidden());
    }

    @Test
    public void getSuits_LockedUserWithToken_StatusForbidden() throws Exception {
        when(userService.getUserById(anyLong())).thenReturn(validUser);
        when(userService.getUserByEmail(anyString())).thenReturn(validUser);
        when(userService.isSamePasswords(anyString(), anyString())).thenReturn(true);

        String token = "Bearer " + loginService.getLoginJWTToken(loginUserDTO);

        when(validUser.isLocked()).thenReturn(true);

        mvc.perform(get("/suits").header("Authorization", token).contentType("application/json"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

}