package com.epam.test_generator.services;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.epam.test_generator.dto.LoginUserDTO;
import com.epam.test_generator.entities.Role;
import com.epam.test_generator.entities.User;
import com.epam.test_generator.services.exceptions.UnauthorizedException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.env.Environment;

import javax.servlet.http.HttpServletRequest;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class LoginServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private Environment environment;

    @Mock
    private HttpServletRequest request;

    @Mock
    private LoginUserDTO loginUserDTO;

    @Mock
    private User user;

    @Mock
    private Role role;

    private String badToken;
    private String goodToken;

    @InjectMocks
    private LoginService sut;

    @Before
    public void setUp() throws Exception {
        when(userService.getUserByEmail(anyString())).thenReturn(user);
        when(environment.getProperty(anyString())).thenReturn("iteaky");
        when(user.getName()).thenReturn("name");
        when(user.getSurname()).thenReturn("surname");
        when(user.getEmail()).thenReturn("email");
        when(user.getAttempts()).thenReturn(5);
        when(user.getRole()).thenReturn(role);
        when(role.getName()).thenReturn("GUEST");
        badToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJciIsImlkIjoyfQ.dpsptV5O_062nzcMUeZa4QLTsAmQfXhQntfnpcMlZLU";
        goodToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJjdWN1bWJlciIsImlkIjoyfQ.dpsptV5O_062nzcMUeZa4QLTsAmQfXhQntfnpcMlZLU";

    }

    @Test
    public void validate_SimpleToken_Ok() throws Exception {
        sut.validate(goodToken);

    }

    @Test(expected = JWTDecodeException.class)
    public void validate_notValidToken_Exception() throws Exception {
        sut.validate(badToken);
    }


    @Test
    public void getToken_ValidToken_Ok() throws Exception {
        when(loginUserDTO.getEmail()).thenReturn("email");
        when(userService.getUserByEmail(any())).thenReturn(user);
        when(userService.isSamePasswords(anyString(), anyString())).thenReturn(true);
        sut.getLoginJWTToken(loginUserDTO);
    }

    @Test(expected = UnauthorizedException.class)
    public void checkPassword_NotExistedUser_Exception() throws Exception {
        sut.checkPassword(loginUserDTO,request);
    }

    @Test(expected = UnauthorizedException.class)
    public void checkPassword_IncorrectPassword_Exception() throws Exception {
        when(loginUserDTO.getEmail()).thenReturn("email");
        when(userService.getUserByEmail(any())).thenReturn(user);
        sut.checkPassword(loginUserDTO, request);
    }

    @Test(expected = UnauthorizedException.class)
    public void checkPassword_LockedUser_UnauthorizedException() throws Exception {
        when(user.isLocked()).thenReturn(true);

        when(loginUserDTO.getEmail()).thenReturn("email");
        when(userService.getUserByEmail(any())).thenReturn(user);
        sut.checkPassword(loginUserDTO, request);
    }

    @Test(expected = UnauthorizedException.class)
    public void getToken_LastFailureAttempt_UnauthorizedException() throws Exception {

        when(user.getAttempts()).thenReturn(4);
        when(user.isLocked()).thenReturn(false);

        when(loginUserDTO.getEmail()).thenReturn("email");
        when(userService.getUserByEmail(any())).thenReturn(user);
        when(userService.isSamePasswords(anyString(), anyString())).thenReturn(false);
        sut.checkPassword(loginUserDTO,request);

        verify(userService,times(1)).updateFailureAttempts(anyLong());
        verify(userService,never()).invalidateAttempts(any());
    }

    @Test
    public void getToken_SuccessAttempt_InvalidateUserAttempts(){

        when(loginUserDTO.getEmail()).thenReturn("email");
        when(userService.getUserByEmail(any())).thenReturn(user);
        when(userService.isSamePasswords(anyString(), anyString())).thenReturn(true);
        sut.checkPassword(loginUserDTO,request);
        sut.getLoginJWTToken(loginUserDTO);

        verify(userService,never()).updateFailureAttempts(any());
        verify(userService,times(1)).invalidateAttempts(any());
    }
}