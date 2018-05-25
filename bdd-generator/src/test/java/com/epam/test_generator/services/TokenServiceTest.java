package com.epam.test_generator.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.epam.test_generator.dao.interfaces.TokenDAO;
import com.epam.test_generator.entities.Token;
import com.epam.test_generator.entities.User;
import com.epam.test_generator.services.exceptions.TokenMalformedException;
import com.epam.test_generator.services.exceptions.TokenMissingException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;


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


    @Test
    public void createToken_SimpleInputParameters_Ok() {
        when(tokenDAO.save(any(Token.class))).thenReturn(token);
        Token token1 = sut.createToken(user, 1);
        Assert.assertEquals(token, token1);

    }

    @Test
    public void checkToken_SimpleToken_Ok() {
        when(tokenDAO.findByTokenUuid(anyString())).thenReturn(token);
        when(token.isExpired()).thenReturn(false);
        sut.checkToken(anyString());

    }

    @Test(expected = TokenMissingException.class)
    public void checkToken_IncorrectToken_Exception() {
        when(tokenDAO.findByTokenUuid(anyString())).thenReturn(null);
        sut.checkToken(anyString());

    }

    @Test(expected = TokenMalformedException.class)
    public void checkToken_ExpiredToken_Exception() {
        when(tokenDAO.findByTokenUuid(anyString())).thenReturn(token);
        when(token.isExpired()).thenReturn(true);
        sut.checkToken(anyString());

    }

    @Test
    public void getTokenByName_SimpleToken_Ok() {
        when(tokenDAO.findByTokenUuid("token")).thenReturn(token);
        Token actualToken = sut.getTokenByName("token");
        verify(tokenDAO).findByTokenUuid("token");
        Assert.assertEquals(token, actualToken);
    }

    @Test
    public void invalidateToken_SimpleInputParameters_Ok() {
        sut.invalidateToken(token);
        verify(tokenDAO).delete(token);
    }

}