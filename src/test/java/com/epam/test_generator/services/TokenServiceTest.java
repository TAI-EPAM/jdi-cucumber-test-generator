package com.epam.test_generator.services;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.epam.test_generator.dto.LoginUserDTO;
import com.epam.test_generator.entities.User;
import com.epam.test_generator.services.exceptions.UnauthorizedException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.env.Environment;

@RunWith(MockitoJUnitRunner.class)
public class TokenServiceTest {

    @Mock
    UserService userService;

    @Mock
    Environment environment;

    @InjectMocks
    TokenService sut;

    private String badToken;
    private String goodToken;

    @Mock
    LoginUserDTO loginUserDTO;

    @Mock
    User user;


    @Before
    public void setUp() throws Exception {
        when(environment.getProperty(anyString())).thenReturn("iteaky");
        badToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJciIsImlkIjoyfQ.dpsptV5O_062nzcMUeZa4QLTsAmQfXhQntfnpcMlZLU";
        goodToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJjdWN1bWJlciIsImlkIjoyfQ.dpsptV5O_062nzcMUeZa4QLTsAmQfXhQntfnpcMlZLU";

    }

    @Test
    public void validate_ok() throws Exception {
        sut.validate(goodToken);

    }
    @Test(expected = JWTDecodeException.class)
    public void validate_notValidToken() throws Exception {
        sut.validate(badToken);
    }



    @Test
    public void getToken_ok() throws Exception {
        when(loginUserDTO.getEmail()).thenReturn("email");
        when(userService.getUserByEmail(any())).thenReturn(user);
        when(userService.isSamePasswords(anyString(),anyString())).thenReturn(true);
        sut.getToken(loginUserDTO);
    }

    @Test(expected = UnauthorizedException.class)
    public void getToken_NoSuchUser() throws Exception{
        sut.getToken(loginUserDTO);
    }

    @Test(expected = UnauthorizedException.class)
    public void getToken_IncorrectPassword() throws Exception{
        when(loginUserDTO.getEmail()).thenReturn("email");
        when(userService.getUserByEmail(any())).thenReturn(user);
        sut.getToken(loginUserDTO);
    }

}