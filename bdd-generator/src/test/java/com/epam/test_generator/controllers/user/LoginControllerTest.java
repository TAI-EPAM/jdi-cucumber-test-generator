package com.epam.test_generator.controllers.user;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epam.test_generator.controllers.GlobalExceptionController;
import com.epam.test_generator.controllers.user.request.LoginUserDTO;
import com.epam.test_generator.services.LoginService;
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
    public void setUp() {
        user = new LoginUserDTO();
        mapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(loginController)
            .setControllerAdvice(new GlobalExceptionController())
            .build();
    }

    @Test
    public void loginTest_SimpleUser_StatusOk() throws Exception {
        user.setPassword("test");
        user.setEmail("test@test.ru");
        String json = mapper.writeValueAsString(user);
        mockMvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON).content(json))
            .andExpect(status().isOk());
    }

    @Test
    public void loginTest_IncorrectEmail_StatusBadRequest() throws Exception {
        user.setPassword("test");
        user.setEmail("test");
        String json = mapper.writeValueAsString(user);
        mockMvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON).content(json))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void loginTest_NullPassword_StatusBadRequest() throws Exception {
        user.setEmail("test");
        String json = mapper.writeValueAsString(user);
        mockMvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON).content(json))

            .andExpect(status().isBadRequest());
    }

    @Test
    public void loginTest_NullEmail_StatusBadRequest() throws Exception {
        user.setEmail("test");
        String json = mapper.writeValueAsString(user);
        mockMvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON).content(json))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void loginTest_NullUser_StatusBadRequest() throws Exception {
        String json = mapper.writeValueAsString(user);
        mockMvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON).content(json))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void loginTest_NullJson_StatusInternalServerError() throws Exception {
        String json = mapper.writeValueAsString(null);
        mockMvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON).content(json))
            .andExpect(status().isInternalServerError());
    }

}