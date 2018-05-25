package com.epam.test_generator.services;

import com.epam.test_generator.dao.interfaces.TokenDAO;
import com.epam.test_generator.entities.Token;
import com.epam.test_generator.entities.User;
import com.epam.test_generator.services.exceptions.TokenMalformedException;
import com.epam.test_generator.services.exceptions.TokenMissingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@Service
public class TokenService {

    @Autowired
    private TokenDAO tokenDAO;

    /**
     * Creates token for user for limited time
     * @param user
     * @param minutes time limit
     * @return user's token
     */
    public Token createToken(User user, Integer minutes) {
        Token token = Token.withExpiryDuration(minutes);
        token.setUser(user);
        return tokenDAO.save(token);
    }

    /**
     * Checks token for existing and expiring. Throws exceptions if there is problems with token.
     * @param token
     */
    public void checkToken(String token) {
        Token resetToken = getTokenByName(token);
        if (resetToken == null) {
            throw new TokenMissingException("Could not find password reset token.");
        } else if (resetToken.isExpired()) {
            tokenDAO.delete(resetToken);
            throw new TokenMalformedException(
                    "Token has expired, please request a new password reset.");
        }
    }

    /**
     * Returns token by the Uuid string
     * @param uuid {@link Token#tokenUuid} string
     * @return {@link Token}
     */
    public Token getTokenByName(String uuid) {
        return tokenDAO.findByTokenUuid(uuid);
    }

    /**
     * Removes token from database
     * @param token {@link Token} which will be removed
     */
    public void invalidateToken(Token token) {
        tokenDAO.delete(token);
    }
}
