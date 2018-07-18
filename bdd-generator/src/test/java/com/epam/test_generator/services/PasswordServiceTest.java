package com.epam.test_generator.services;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.epam.test_generator.controllers.user.request.PasswordResetDTO;
import com.epam.test_generator.entities.Token;
import com.epam.test_generator.entities.User;
import com.epam.test_generator.services.exceptions.TokenMissingException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.util.UriComponentsBuilder;

@RunWith(MockitoJUnitRunner.class)
public class PasswordServiceTest {

    @Mock
    private Token token;

    @Mock
    private PasswordResetDTO passwordResetDTO;

    @Mock
    private TokenService tokenService;

    @Mock
    private User user;

    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private PasswordService sut;

    private UriComponentsBuilder uriComponentsBuilder;

    @Before
    public void setUp() {
        uriComponentsBuilder = UriComponentsBuilder.newInstance();
        uriComponentsBuilder.scheme("scheme");
        uriComponentsBuilder.host("serverName");
        uriComponentsBuilder.port(1);
        uriComponentsBuilder.path("");
        when(token.getTokenUuid()).thenReturn("token");
    }

    @Test
    public void createResetUrl_SimpleInputDate_Ok() {
        String resetUrlExpected = sut.createResetUrl(uriComponentsBuilder, token);
        String resetUrlActual = "scheme://serverName:1/user/validate-reset-token?token=token";

        Assert.assertEquals(resetUrlExpected, resetUrlActual);
    }

    @Test
    public void createConfirmUrl_SimpleInputDate_Ok() {
        String resetUrlExpected = sut.createConfirmUrl(uriComponentsBuilder, token);
        String resetUrlActual = "scheme://serverName:1/user/confirm-email?token=token";

        Assert.assertEquals(resetUrlExpected, resetUrlActual);
    }

    @Test
    public void passwordReset_SimplePasswordResetDTO_Ok() {
        when(passwordResetDTO.getToken()).thenReturn("token");
        when(tokenService.getTokenByName(anyString())).thenReturn(token);
        when(token.getUser()).thenReturn(user);

        sut.passwordReset(passwordResetDTO);
        verify(tokenService).invalidateToken(token);
    }

    @Test(expected = TokenMissingException.class)
    public void passwordReset_IncorrectToken_Exception() {
        when(passwordResetDTO.getToken()).thenReturn("token");
        when(tokenService.getTokenByName(anyString())).thenReturn(null);

        sut.passwordReset(passwordResetDTO);
        verify(tokenService).invalidateToken(token);
    }
}