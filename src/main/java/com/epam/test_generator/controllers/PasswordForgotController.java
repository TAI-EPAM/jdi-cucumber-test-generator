package com.epam.test_generator.controllers;

import com.epam.test_generator.dto.EmailDTO;
import com.epam.test_generator.entities.User;
import com.epam.test_generator.services.EmailService;
import com.epam.test_generator.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Sends reset password message to user email.
 */
@RestController
public class PasswordForgotController {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @RequestMapping(value = "/passwordForgot", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity passwordForgot(@RequestBody EmailDTO email,
                                         HttpServletRequest request) throws Exception {

        User user = userService.getUserByEmail(email.getEmail());
        userService.checkUserExist(user);
        emailService.sendResetPasswordMessage(user, request);

        return new ResponseEntity(HttpStatus.OK);
    }
}
