package com.epam.test_generator.controllers;

import com.epam.test_generator.dto.EmailDTO;
import com.epam.test_generator.entities.User;
import com.epam.test_generator.services.EmailService;
import com.epam.test_generator.services.UserService;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class PasswordForgotControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private EmailService emailService;

    @Mock
    private User user;

    @Mock
    private EmailDTO email;
    private ObjectMapper mapper;
    private MockMvc mockMvc;

    @InjectMocks
    private PasswordForgotController passwordForgotController;

    @Before
    public void setUp() throws Exception {
        email = new EmailDTO();
        mapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(passwordForgotController)
                .setControllerAdvice(new GlobalExceptionController())
                .build();
    }

    @Test
    public void passwordForgot_200() throws Exception {
        when(userService.getUserByEmail(anyString())).thenReturn(user);
        String json = mapper.writeValueAsString(email);
        mockMvc.perform(post("/passwordForgot").contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print()).andExpect(status().isOk());
    }

    @Test
    public void passwordForgot_incorrectMail_400() throws Exception {
        when(userService.getUserByEmail(anyString())).thenReturn(null);
        doCallRealMethod().when(userService).checkUserExist(any(User.class));
        String json = mapper.writeValueAsString(email);
        mockMvc.perform(post("/passwordForgot").contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print()).andExpect(status().isUnauthorized());
    }

    @Test
    public void passwordForgot_nullJson_500() throws Exception {
        String json = mapper.writeValueAsString(null);
        mockMvc.perform(post("/passwordForgot").contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print()).andExpect(status().isInternalServerError());
    }

}