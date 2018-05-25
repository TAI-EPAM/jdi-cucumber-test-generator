package com.epam.test_generator.services;

import com.epam.test_generator.controllers.user.UserDTOsTransformer;
import com.epam.test_generator.controllers.user.response.UserDTO;
import com.epam.test_generator.entities.Token;
import com.epam.test_generator.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

@Service
@Transactional
@Profile("integration-tests")
public class EmailServiceIntegrationImpl implements EmailService {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserDTOsTransformer userDTOsTransformer;

    @Override
    public UserDTO sendRegistrationMessage(User user, HttpServletRequest request) {
        Token userConformationToken = tokenService.createToken(user, CONFIRMATION_TIME);
        userService.confirmUser(userConformationToken.getTokenUuid());
        return userDTOsTransformer.toUserDTO(user);
    }
}
