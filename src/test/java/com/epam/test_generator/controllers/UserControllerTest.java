package com.epam.test_generator.controllers;

import com.epam.test_generator.dto.LoginUserDTO;
import com.epam.test_generator.services.EmailService;
import com.epam.test_generator.dto.RegistrationUserDTO;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {

    @Mock
    private EmailService emailService;
    @Mock
    private UserService userService;
    @InjectMocks
    private UserController userController;
    private MockMvc mockMvc;
    private RegistrationUserDTO userDTO;
    private ObjectMapper mapper;

    @Before
    public void setUp() throws Exception {
        mapper = new ObjectMapper();
        userDTO = new RegistrationUserDTO();
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
            .setControllerAdvice(new GlobalExceptionController())
            .build();
    }

    @Test
    public void registration_SimpleUser_StatusOk() throws Exception {
        userDTO.setName("test_name");
        userDTO.setSurname("test_sure_name");
        userDTO.setPassword("test");
        userDTO.setEmail("test@test.ru");
        final String json = mapper.writeValueAsString(userDTO);
        mockMvc.perform(post("/registration").contentType(MediaType.APPLICATION_JSON).content(json))
            .andDo(print()).andExpect(status().isOk());
    }


    @Test
    public void registration_IncorrectEmail_StatusBadRequest() throws Exception {
        userDTO.setPassword("test");
        userDTO.setEmail("test");
        final String json = mapper.writeValueAsString(userDTO);
        mockMvc.perform(post("/registration").contentType(MediaType.APPLICATION_JSON).content(json))
            .andDo(print())
            .andExpect(status().isBadRequest());
    }

    @Test
    public void registration_NullPassword_StatusBadRequest() throws Exception {
        userDTO.setEmail("test");
        final String json = mapper.writeValueAsString(userDTO);
        mockMvc.perform(post("/registration").contentType(MediaType.APPLICATION_JSON).content(json))
            .andDo(print())
            .andExpect(status().isBadRequest());
    }

    @Test
    public void registration_NullEmail_StatusBadRequest() throws Exception {
        userDTO.setEmail("test");
        final String json = mapper.writeValueAsString(userDTO);
        mockMvc.perform(post("/registration").contentType(MediaType.APPLICATION_JSON).content(json))
            .andDo(print())
            .andExpect(status().isBadRequest());
    }

    @Test
    public void registration_NullUser_StatusBadRequest() throws Exception {
        String json = mapper.writeValueAsString(userDTO);
        mockMvc.perform(post("/registration").contentType(MediaType.APPLICATION_JSON).content(json))
            .andDo(print())
            .andExpect(status().isBadRequest());
    }

    @Test
    public void registration_NullJson_StatusInternalServerError() throws Exception {
        String json = mapper.writeValueAsString(null);
        mockMvc.perform(post("/registration").contentType(MediaType.APPLICATION_JSON).content(json))
            .andDo(print())
            .andExpect(status().isInternalServerError());
    }

}