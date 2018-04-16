package com.epam.test_generator.entity;

import com.epam.test_generator.entities.Token;
import com.epam.test_generator.entities.User;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TokenTest {

    private final int LONG_EXPIRATION_DURATION = 60;
    private final int NEGATIVE_EXPIRATION_DURATION = -1;

    @Test
    public void isExpired_NotExpiredToken_False() {
        Token token = Token.withExpiryDuration(LONG_EXPIRATION_DURATION);
        assertFalse(token.isExpired());
    }

    @Test
    public void isExpired_ExpiredToken_True() {
        Token token = Token.withExpiryDuration(NEGATIVE_EXPIRATION_DURATION);
        assertTrue(token.isExpired());
    }


}
