package com.epam.test_generator.services;

import com.epam.test_generator.controllers.user.request.PasswordResetDTO;
import com.epam.test_generator.entities.Token;
import com.epam.test_generator.entities.User;
import com.epam.test_generator.services.exceptions.TokenMissingException;
import java.net.URI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class PasswordService {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenService tokenService;

    private final static String PASSWORD_RESET_PATH = "/user/validate-reset-token";
    private final static String CONFIRM_ACCOUNT_PATH = "/user/confirm-email";
    private final static String TOKEN_PARAM_NAME = "token";

    /**
     * Generates URI path to reset password by user token
     *
     * @param token user token
     * @return URI path
     */
    public String createResetUrl(UriComponentsBuilder uriComponentsBuilder, Token token) {
        return getSecurityUrl(uriComponentsBuilder, PASSWORD_RESET_PATH, token).toString();
    }

    /**
     * Generates URI path to confirm user account by token
     *
     * @param token user token
     * @return URI path
     */
    public String createConfirmUrl(UriComponentsBuilder uriComponentsBuilder, Token token) {
        return getSecurityUrl(uriComponentsBuilder, CONFIRM_ACCOUNT_PATH, token).toString();
    }


    /**
     * Resets password for user specified in passwordResetDTO
     *
     * @param passwordResetDTO info about user token and password
     */
    public void passwordReset(PasswordResetDTO passwordResetDTO) {
        String token = passwordResetDTO.getToken();
        Token resetToken = tokenService.getTokenByName(token);
        if (resetToken == null) {
            throw new TokenMissingException("Token is invalid");
        }
        User user = resetToken.getUser();
        String updatedPassword = passwordEncoder.encode(passwordResetDTO.getPassword());
        userService.updatePassword(updatedPassword, user.getEmail());
        tokenService.invalidateToken(resetToken);
    }

    private URI getSecurityUrl(UriComponentsBuilder uriComponentsBuilder, String path,
                               Token token) {
        return uriComponentsBuilder
            .replacePath(path)
            .replaceQuery("")
            .queryParam(TOKEN_PARAM_NAME, token.getTokenUuid())
            .build()
            .toUri();
    }
}
