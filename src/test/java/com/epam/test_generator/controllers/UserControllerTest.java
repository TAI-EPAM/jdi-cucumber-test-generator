package com.epam.test_generator.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epam.test_generator.dto.LoginUserDTO;
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

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {


    @Mock
    UserService userService;
    @InjectMocks
    UserController userController;
    private MockMvc mockMvc;
    private LoginUserDTO userDTO;
    private LoginUserDTO user;
    private ObjectMapper mapper;

    @Before
    public void setUp() throws Exception {
        user = new LoginUserDTO();
        mapper = new ObjectMapper();
        userDTO = new LoginUserDTO();
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
            .setControllerAdvice(new GlobalExceptionController())
            .build();
    }

    @Test
    public void registrationTest_200() throws Exception {
        userDTO.setPassword("test");
        userDTO.setEmail("test@test.ru");
        String json = mapper.writeValueAsString(userDTO);
        mockMvc.perform(post("/registration").contentType(MediaType.APPLICATION_JSON).content(json))
            .andDo(print()).andExpect(status().isOk());
    }


    @Test
    public void registrationTest_incorrectEmail_400() throws Exception {
        user.setPassword("test");
        user.setEmail("test");
        String json = mapper.writeValueAsString(user);
        mockMvc.perform(post("/registration").contentType(MediaType.APPLICATION_JSON).content(json))
            .andDo(print())
            .andExpect(status().isBadRequest());
    }

    @Test
    public void registrationTest_nullPassword400() throws Exception {
        user.setEmail("test");
        String json = mapper.writeValueAsString(user);
        mockMvc.perform(post("/registration").contentType(MediaType.APPLICATION_JSON).content(json))
            .andDo(print())
            .andExpect(status().isBadRequest());
    }

    @Test
    public void registrationTest_nullEmail400() throws Exception {
        user.setEmail("test");
        String json = mapper.writeValueAsString(user);
        mockMvc.perform(post("/registration").contentType(MediaType.APPLICATION_JSON).content(json))
            .andDo(print())
            .andExpect(status().isBadRequest());
    }

    @Test
    public void registrationTest_nullUser400() throws Exception {
        String json = mapper.writeValueAsString(user);
        mockMvc.perform(post("/registration").contentType(MediaType.APPLICATION_JSON).content(json))
            .andDo(print())
            .andExpect(status().isBadRequest());
    }

    @Test
    public void registrationTest_nullJson500() throws Exception {
        String json = mapper.writeValueAsString(null);
        mockMvc.perform(post("/registration").contentType(MediaType.APPLICATION_JSON).content(json))
            .andDo(print())
            .andExpect(status().isInternalServerError());
    }

}