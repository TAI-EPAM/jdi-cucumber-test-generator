package com.epam.test_generator.controllers;

import com.epam.test_generator.dao.interfaces.TokenDAO;
import com.epam.test_generator.dto.PasswordResetDTO;
import com.epam.test_generator.services.PasswordService;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class PasswordResetControllerTest {
    @Mock
    private TokenService tokenService;

    @Mock
    private PasswordService passwordService;

    @Mock
    private TokenDAO tokenDAO;

    @Mock
    private ObjectMapper mapper;

    private MockMvc mockMvc;

    @Mock
    private PasswordResetDTO passwordReset;

    @InjectMocks
    private PasswordResetController passwordResetController;

    @Before
    public void setUp() throws Exception {
        passwordReset = new PasswordResetDTO();
        mapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(passwordResetController)
                .setControllerAdvice(new GlobalExceptionController())
                .build();
    }

    @Test
    public void passwordReset_SimplePasswordDTO_StatusOk() throws Exception {
        passwordReset.setPassword("password");
        passwordReset.setToken("token");
        String json = mapper.writeValueAsString(passwordReset);
        mockMvc.perform(post("/passwordReset").contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print()).andExpect(status().isOk());
    }

    @Test
    public void displayResetPasswordPage_SimpleToken_StatusOk() throws Exception {
        mockMvc.perform(get("/passwordReset").param("token","value"))
                .andDo(print()).andExpect(status().isOk());
    }

    @Test
    public void passwordReset_NullJson_StatusInternalServerError() throws Exception {
        String json = mapper.writeValueAsString(null);
        mockMvc.perform(post("/passwordReset").contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print()).andExpect(status().isInternalServerError());
    }

}