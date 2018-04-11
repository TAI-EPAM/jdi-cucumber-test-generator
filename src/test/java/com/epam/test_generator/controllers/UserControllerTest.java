package com.epam.test_generator.controllers;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epam.test_generator.dto.EmailDTO;
import com.epam.test_generator.dto.PasswordResetDTO;
import com.epam.test_generator.dto.RegistrationUserDTO;
import com.epam.test_generator.entities.User;
import com.epam.test_generator.services.EmailService;
import com.epam.test_generator.services.PasswordService;
import com.epam.test_generator.services.TokenService;
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
    private TokenService tokenService;

    @Mock
    private PasswordService passwordService;

    @Mock
    private EmailService emailService;

    @Mock
    private UserService userService;

    @Mock
    private User user;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;
    private RegistrationUserDTO userDTO;
    private PasswordResetDTO passwordReset;
    private EmailDTO email;
    private ObjectMapper mapper;

    @Before
    public void setUp() {
        mapper = new ObjectMapper();
        userDTO = new RegistrationUserDTO();
        passwordReset = new PasswordResetDTO();
        email = new EmailDTO();
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
        mockMvc.perform(
            post("/user/registration").contentType(MediaType.APPLICATION_JSON).content(json))
            .andExpect(status().isOk());
    }


    @Test
    public void registration_IncorrectEmail_StatusBadRequest() throws Exception {
        userDTO.setPassword("test");
        userDTO.setEmail("test");
        final String json = mapper.writeValueAsString(userDTO);
        mockMvc.perform(
            post("/user/registration").contentType(MediaType.APPLICATION_JSON).content(json))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void registration_NullPassword_StatusBadRequest() throws Exception {
        userDTO.setEmail("test");
        final String json = mapper.writeValueAsString(userDTO);
        mockMvc.perform(
            post("/user/registration").contentType(MediaType.APPLICATION_JSON).content(json))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void registration_NullEmail_StatusBadRequest() throws Exception {
        userDTO.setEmail("test");
        final String json = mapper.writeValueAsString(userDTO);
        mockMvc.perform(
            post("/user/registration").contentType(MediaType.APPLICATION_JSON).content(json))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void registration_NullUser_StatusBadRequest() throws Exception {
        String json = mapper.writeValueAsString(userDTO);
        mockMvc.perform(
            post("/user/registration").contentType(MediaType.APPLICATION_JSON).content(json))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void registration_NullJson_StatusInternalServerError() throws Exception {
        String json = mapper.writeValueAsString(null);
        mockMvc.perform(
            post("/user/registration").contentType(MediaType.APPLICATION_JSON).content(json))
            .andExpect(status().isInternalServerError());
    }

    @Test
    public void passwordReset_SimplePasswordDTO_StatusOk() throws Exception {
        passwordReset.setPassword("password");
        passwordReset.setToken("token");
        String json = mapper.writeValueAsString(passwordReset);
        mockMvc.perform(post("/user/change-password").contentType(MediaType.APPLICATION_JSON)
            .content(json)).andExpect(status().isOk());
    }

    @Test
    public void displayResetPasswordPage_SimpleToken_StatusOk() throws Exception {
        mockMvc.perform(get("/user/validate-reset-token").param("token", "value"))
            .andExpect(status().isOk());
    }

    @Test
    public void passwordReset_NullJson_StatusInternalServerError() throws Exception {
        String json = mapper.writeValueAsString(null);
        mockMvc.perform(
            post("/user/change-password").contentType(MediaType.APPLICATION_JSON).content(json))
            .andExpect(status().isInternalServerError());
    }

    @Test
    public void passwordForgot_ByEmail_StatusOk() throws Exception {
        when(userService.getUserByEmail(anyString())).thenReturn(user);
        String json = mapper.writeValueAsString(email);
        mockMvc.perform(
            post("/user/forgot-password").contentType(MediaType.APPLICATION_JSON).content(json))
            .andExpect(status().isOk());
    }

    @Test
    public void passwordForgot_IncorrectMail_StatusBadRequest() throws Exception {
        when(userService.getUserByEmail(anyString())).thenReturn(null);
        doCallRealMethod().when(userService).checkUserExist(any(User.class));
        String json = mapper.writeValueAsString(email);
        mockMvc.perform(
            post("/user/forgot-password").contentType(MediaType.APPLICATION_JSON).content(json))
            .andExpect(status().isUnauthorized());
    }

    @Test
    public void passwordForgot_NullJson_StatusInternalServerError() throws Exception {
        String json = mapper.writeValueAsString(null);
        mockMvc.perform(
            post("/user/forgot-password").contentType(MediaType.APPLICATION_JSON).content(json))
            .andExpect(status().isInternalServerError());
    }

    @Test
    public void confirmEmail_SimpleToken_StatusOk() throws Exception {
        mockMvc.perform(get("/user/confirm-email").param("token", "value"))
            .andExpect(status().isOk());
    }
}