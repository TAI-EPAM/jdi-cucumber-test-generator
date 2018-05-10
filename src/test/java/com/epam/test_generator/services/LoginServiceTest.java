package com.epam.test_generator.services;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.epam.test_generator.controllers.user.request.LoginUserDTO;
import com.epam.test_generator.entities.Role;
import com.epam.test_generator.entities.User;
import com.epam.test_generator.services.exceptions.UnauthorizedException;
import javax.servlet.http.HttpServletRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.env.Environment;

@RunWith(MockitoJUnitRunner.class)
public class LoginServiceTest {

    private static final String EMAIL = "email";
    private static final String SECRET = "iteaky";
    private static final String USER_NAME = "name";
    private static final String USER_SURNAME = "surname";
    private static final int AMOUNT_OF_LOGIN_ATTEMPTS = 5;
    private static final String USER_ROLE = "GUEST";

    private static final String BAD_TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJciIsImlkIjoyfQ"
        + ".dpsptV5O_062nzcMUeZa4QLTsAmQfXhQntfnpcMlZLU";
    private static final String GOOD_TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9"
        + ".eyJpc3MiOiJjdWN1bWJlciIsImlkIjoyfQ.dpsptV5O_062nzcMUeZa4QLTsAmQfXhQntfnpcMlZLU";


    @Mock
    private UserService userService;

    @Mock
    private JWTTokenService jwtTokenService;

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

    @InjectMocks
    private LoginService sut;

    @Before
    public void setUp() {
        when(userService.getUserByEmail(anyString())).thenReturn(user);
        when(environment.getProperty(anyString())).thenReturn(SECRET);
        when(user.getName()).thenReturn(USER_NAME);
        when(user.getId()).thenReturn(1L);
        when(user.getSurname()).thenReturn(USER_SURNAME);
        when(user.getEmail()).thenReturn(EMAIL);
        when(user.getLoginAttempts()).thenReturn(AMOUNT_OF_LOGIN_ATTEMPTS);
        when(user.getRole()).thenReturn(role);
        when(role.getName()).thenReturn(USER_ROLE);
    }

    @Test
    public void decodeJwt_SimpleToken_Ok() throws Exception{
        sut.decodeJwt(GOOD_TOKEN);

    }

    @Test(expected = JWTDecodeException.class)
    public void decodeJwt_notValidToken_Exception() throws Exception {
        sut.decodeJwt(BAD_TOKEN);
    }


    @Test
    public void getToken_ValidToken_Ok() {
        when(loginUserDTO.getEmail()).thenReturn(EMAIL);
        when(userService.getUserByEmail(any())).thenReturn(user);
        when(userService.isSamePasswords(anyString(), anyString())).thenReturn(true);
        sut.getLoginJWTToken(loginUserDTO);
    }

    @Test(expected = UnauthorizedException.class)
    public void checkPassword_NotExistedUser_Exception() {
        sut.checkPassword(loginUserDTO,request);
    }

    @Test(expected = UnauthorizedException.class)
    public void checkPassword_IncorrectPassword_Exception() {
        when(loginUserDTO.getEmail()).thenReturn(EMAIL);
        when(userService.getUserByEmail(any())).thenReturn(user);
        sut.checkPassword(loginUserDTO, request);
    }

    @Test(expected = UnauthorizedException.class)
    public void checkPassword_LockedUser_UnauthorizedException() {
        when(user.isLocked()).thenReturn(true);

        when(loginUserDTO.getEmail()).thenReturn(EMAIL);
        when(userService.getUserByEmail(any())).thenReturn(user);
        sut.checkPassword(loginUserDTO, request);
    }

    @Test(expected = UnauthorizedException.class)
    public void getToken_LastFailureAttempt_UnauthorizedException() {

        when(user.getLoginAttempts()).thenReturn(4);
        when(user.isLocked()).thenReturn(false);

        when(loginUserDTO.getEmail()).thenReturn(EMAIL);
        when(userService.getUserByEmail(any())).thenReturn(user);
        when(userService.isSamePasswords(anyString(), anyString())).thenReturn(false);
        sut.checkPassword(loginUserDTO,request);

        verify(userService,times(1)).updateFailureAttempts(anyLong());
        verify(userService,never()).invalidateAttempts(any());
    }

    @Test
    public void getToken_SuccessAttempt_InvalidateUserAttempts(){

        when(loginUserDTO.getEmail()).thenReturn(EMAIL);
        when(userService.getUserByEmail(any())).thenReturn(user);
        when(userService.isSamePasswords(anyString(), anyString())).thenReturn(true);
        sut.checkPassword(loginUserDTO,request);
        sut.getLoginJWTToken(loginUserDTO);

        verify(userService,never()).updateFailureAttempts(any());
        verify(userService,times(1)).invalidateAttempts(any());
    }
}