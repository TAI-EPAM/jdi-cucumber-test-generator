package com.epam.test_generator.services;

import com.epam.test_generator.controllers.user.response.UserDTO;
import com.epam.test_generator.entities.User;
import javax.servlet.http.HttpServletRequest;


public interface EmailService {

    Integer CONFIRMATION_TIME = 1440;
    Integer PASSWORD_RESET_TIME = 15;

    UserDTO sendRegistrationMessage(User user, HttpServletRequest request);

    default void sendResetPasswordMessage(User user, HttpServletRequest request) {
    }
}
