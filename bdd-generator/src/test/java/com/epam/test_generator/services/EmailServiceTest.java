package com.epam.test_generator.services;

import com.epam.test_generator.entities.Token;
import com.epam.test_generator.entities.User;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import javax.servlet.http.HttpServletRequest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)

public class EmailServiceTest {

    @Mock
    private Environment environment;

    @Mock
    private PasswordService passwordService;

    @Mock
    private JavaMailSender emailSender;

    @Mock
    private JavaMailSenderImpl javaMailSender;

    @Mock
    private TokenService tokenService;

    @Mock
    private Token token;

    @Mock
    private User user;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private EmailServiceImpl sut;

    @Before
    public void setUp() throws Exception {
        when(environment.getProperty(anyString())).thenReturn("environmentValue");

    }

    @Ignore
    @Test
    public void sendRegistrationMessage_SimpleInputDate_Ok() {
        when(tokenService.createToken(user, EmailService.CONFIRMATION_TIME)).thenReturn(token);
        when(passwordService.createConfirmUrl(request, token)).thenReturn("confirmUrl");
        sut.sendRegistrationMessage(user, request);
        verify(emailSender).send(any(SimpleMailMessage.class));
    }

    @Test
    public void sendResetPasswordMessage_SimpleInputDate_Ok() {
        when(tokenService.createToken(user, EmailService.PASSWORD_RESET_TIME)).thenReturn(token);
        when(passwordService.createResetUrl(request, token)).thenReturn("confirmUrl");

        sut.sendResetPasswordMessage(user, request);
        verify(emailSender).send(any(SimpleMailMessage.class));

    }
}