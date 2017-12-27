package com.epam.test_generator.controllers;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epam.test_generator.dto.LoginUserDTO;
import com.epam.test_generator.services.TokenService;
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

@RunWith(MockitoJUnitRunner.class)

public class LoginControllerTest {


    private MockMvc mockMvc;
    private LoginUserDTO user;
    private ObjectMapper mapper;


    @Mock
    TokenService tokenService;

    @InjectMocks
    LoginController loginController;

    @Mock
    LoginUserDTO loginUserDTO;


    @Before
    public void setUp() throws Exception {
        user = new LoginUserDTO();
        mapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(loginController)
            .setControllerAdvice(new GlobalExceptionController())
            .build();
    }

    @Test
    public void loginTest_200() throws Exception {
        user.setPassword("test");
        user.setEmail("test@test.ru");
        when(tokenService.getToken(loginUserDTO)).thenReturn(anyString());
        String json = mapper.writeValueAsString(user);
        mockMvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON).content(json))
            .andDo(print()).andExpect(status().isOk());
    }

    @Test
    public void loginTest_incorrectEmail_400() throws Exception {
        user.setPassword("test");
        user.setEmail("test");
        String json = mapper.writeValueAsString(user);
        mockMvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON).content(json))
            .andDo(print())
            .andExpect(status().isBadRequest());
    }

    @Test
    public void loginTest_nullPassword400() throws Exception {
        user.setEmail("test");
        String json = mapper.writeValueAsString(user);
        mockMvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON).content(json))
            .andDo(print())
            .andExpect(status().isBadRequest());
    }

    @Test
    public void loginTest_nullEmail400() throws Exception {
        user.setEmail("test");
        String json = mapper.writeValueAsString(user);
        mockMvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON).content(json))
            .andDo(print())
            .andExpect(status().isBadRequest());
    }

    @Test
    public void loginTest_nullUser400() throws Exception {
        String json = mapper.writeValueAsString(user);
        mockMvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON).content(json))
            .andDo(print())
            .andExpect(status().isBadRequest());
    }

    @Test
    public void loginTest_nullJson500() throws Exception {
        String json = mapper.writeValueAsString(null);
        mockMvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON).content(json))
            .andDo(print())
            .andExpect(status().isInternalServerError());
    }

}