package com.epam.test_generator.controllers;

import com.epam.test_generator.services.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class UserConfirmationControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserConfirmationController userConfirmationController;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(userConfirmationController)
                .setControllerAdvice(new GlobalExceptionController())
                .build();
    }

    @Test
    public void displayResetPasswordPage_SimpleToken_StatusOk() throws Exception {
        mockMvc.perform(get("/confirmAccount").param("token", "value"))
                .andDo(print()).andExpect(status().isOk());
    }
}