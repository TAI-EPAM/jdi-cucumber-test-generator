package com.epam.test_generator.controllers.user;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epam.test_generator.controllers.GlobalExceptionController;
import com.epam.test_generator.controllers.user.request.EmailDTO;
import com.epam.test_generator.controllers.user.request.PasswordResetDTO;
import com.epam.test_generator.controllers.user.request.RegistrationUserDTO;
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
import org.mockito.junit.MockitoJUnitRunner;
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
        email.setEmail("test@test.com");
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
        String json = mapper.writeValueAsString(userDTO);
        mockMvc.perform(post("/user/registration")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isOk());
    }


    @Test
    public void registration_IncorrectEmail_StatusBadRequest() throws Exception {
        userDTO.setPassword("test");
        userDTO.setEmail("test");
        String json = mapper.writeValueAsString(userDTO);
        mockMvc.perform(post("/user/registration")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void registration_NullPassword_StatusBadRequest() throws Exception {
        userDTO.setEmail("test");
        String json = mapper.writeValueAsString(userDTO);
        mockMvc.perform(post("/user/registration")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void registration_NullEmail_StatusBadRequest() throws Exception {
        userDTO.setEmail("test");
        String json = mapper.writeValueAsString(userDTO);
        mockMvc.perform(post("/user/registration")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void registration_NullUser_StatusBadRequest() throws Exception {
        String json = mapper.writeValueAsString(userDTO);
        mockMvc.perform(post("/user/registration")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
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
        mockMvc.perform(post("/user/change-password")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isOk());
    }

    @Test
    public void displayResetPasswordPage_SimpleToken_StatusOk() throws Exception {
        mockMvc.perform(get("/user/validate-reset-token").param("token", "value"))
            .andExpect(status().isOk());
    }

    @Test
    public void passwordReset_NullJson_StatusInternalServerError() throws Exception {
        String json = mapper.writeValueAsString(null);
        mockMvc.perform(post("/user/change-password")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isInternalServerError());
    }

    @Test
    public void passwordForgot_ByEmail_StatusOk() throws Exception {
        when(userService.getUserByEmail(anyString())).thenReturn(user);
        String json = mapper.writeValueAsString(email);
        mockMvc.perform(post("/user/forgot-password")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isOk());
    }

    @Test
    public void passwordForgot_IncorrectMail_StatusBadRequest() throws Exception {
        String json = mapper.writeValueAsString(email);
        when(userService.getUserByEmail(email.getEmail())).thenReturn(null);
        when(userService.checkUserExist(null)).thenCallRealMethod();
        mockMvc.perform(
            post("/user/forgot-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isUnauthorized());
    }

    @Test
    public void passwordForgot_NullJson_StatusInternalServerError() throws Exception {
        String json = mapper.writeValueAsString(null);
        mockMvc.perform(post("/user/forgot-password")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isInternalServerError());
    }

    @Test
    public void confirmEmail_SimpleToken_StatusOk() throws Exception {
        mockMvc.perform(get("/user/confirm-email")
            .param("token", "value"))
            .andExpect(status().isOk());
    }
}