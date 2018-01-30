package com.epam.test_generator.controllers;

import com.epam.test_generator.dto.LoginUserDTO;
import com.epam.test_generator.services.LoginService;
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

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class LoginControllerTest {

    @Mock
    private LoginService loginService;

    @InjectMocks
    private LoginController loginController;

    @Mock
    private LoginUserDTO loginUserDTO;

    private MockMvc mockMvc;
    private LoginUserDTO user;
    private ObjectMapper mapper;

    @Before
    public void setUp() throws Exception {
        user = new LoginUserDTO();
        mapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(loginController)
                .setControllerAdvice(new GlobalExceptionController())
                .build();
    }

    @Test
    public void loginTest_simpleUser_200() throws Exception {
        user.setPassword("test");
        user.setEmail("test@test.ru");
        when(loginService.getLoginJWTToken(loginUserDTO)).thenReturn("token");
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