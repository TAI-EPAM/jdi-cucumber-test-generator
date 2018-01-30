package com.epam.test_generator.services;

import com.epam.test_generator.dao.interfaces.TokenDAO;
import com.epam.test_generator.dto.PasswordResetDTO;
import com.epam.test_generator.entities.Token;
import com.epam.test_generator.entities.User;
import com.epam.test_generator.services.exceptions.IncorrectURI;
import com.epam.test_generator.services.exceptions.TokenMissingException;
import java.net.URI;
import java.net.URISyntaxException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PasswordService {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenDAO tokenDAO;

    private final static String PASSWORD_RESET_PATH = "/cucumber/passwordReset";
    private final static String CONFIRM_ACCOUNT_PATH = "/cucumber/confirmAccount";
    private final static String TOKEN = "token=";

    public String createResetUrl(HttpServletRequest request, Token token) {
        URI uri;
        try {
            uri = new URI(request.getScheme(),
                null,
                request.getServerName(),
                request.getServerPort(),
                PASSWORD_RESET_PATH,
                TOKEN + token.getToken(),
                null);

        } catch (URISyntaxException e) {
            throw new IncorrectURI(e.getMessage());
        }
        return uri.toString();
    }

    public String createConfirmUrl(HttpServletRequest request, Token token) {
        URI uri;
        try {
            uri = new URI(request.getScheme(),
                null,
                request.getServerName(),
                request.getServerPort(),
                CONFIRM_ACCOUNT_PATH,
                TOKEN + token.getToken(),
                null);
        } catch (URISyntaxException e) {
            throw new IncorrectURI(e.getMessage());
        }
        return uri.toString();
    }


    public void passwordReset(PasswordResetDTO passwordResetDTO) {
        String token = passwordResetDTO.getToken();
        Token resetToken = tokenDAO.findByToken(token);
        if (resetToken == null) {
            throw new TokenMissingException("Token is invalid");
        }
        User user = resetToken.getUser();
        String updatedPassword = passwordEncoder.encode(passwordResetDTO.getPassword());
        userService.updatePassword(updatedPassword, user.getEmail());
        tokenDAO.delete(resetToken);
    }

    public Token getTokenByName(String token) {
        return tokenDAO.findByToken(token);
    }
}
