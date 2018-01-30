package com.epam.test_generator.services;

import com.epam.test_generator.dao.interfaces.TokenDAO;
import com.epam.test_generator.entities.Token;
import com.epam.test_generator.entities.User;
import com.epam.test_generator.services.exceptions.TokenMalformedException;
import com.epam.test_generator.services.exceptions.TokenMissingException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class TokenServiceTest {

    @Mock
    private TokenDAO tokenDAO;

    @Mock
    private Token token;

    @Mock
    private User user;

    @InjectMocks
    private TokenService sut;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void createToken_SimpleInputParameters_Ok() {
        when(tokenDAO.save(any(Token.class))).thenReturn(token);
        Token token1 = sut.createToken(user, 1);
        Assert.assertEquals(token, token1);

    }

    @Test
    public void checkToken_SimpleToken_Ok() {
        when(tokenDAO.findByToken(anyString())).thenReturn(token);
        when(token.isExpired()).thenReturn(false);
        sut.checkToken(anyString());

    }

    @Test(expected = TokenMissingException.class)
    public void checkToken_IncorrectToken_Exception() {
        when(tokenDAO.findByToken(anyString())).thenReturn(null);
        sut.checkToken(anyString());

    }

    @Test(expected = TokenMalformedException.class)
    public void checkToken_ExpiredToken_Exception() {
        when(tokenDAO.findByToken(anyString())).thenReturn(token);
        when(token.isExpired()).thenReturn(true);
        sut.checkToken(anyString());

    }

}