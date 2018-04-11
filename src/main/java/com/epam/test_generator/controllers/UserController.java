package com.epam.test_generator.controllers;

import com.epam.test_generator.dto.EmailDTO;
import com.epam.test_generator.dto.PasswordResetDTO;
import com.epam.test_generator.dto.RegistrationUserDTO;
import com.epam.test_generator.entities.User;
import com.epam.test_generator.services.EmailService;
import com.epam.test_generator.services.PasswordService;
import com.epam.test_generator.services.TokenService;
import com.epam.test_generator.services.UserService;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Allows to register a new user, confirm his email to activate the account and change the password.
 */
@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private PasswordService passwordService;

    @PostMapping("/registration")
    public ResponseEntity registerUserAccount(@RequestBody @Valid RegistrationUserDTO userDTO,  HttpServletRequest request) {

        User user = userService.createUser(userDTO);
        emailService.sendRegistrationMessage(user, request);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping(value = "/confirm-email")
    public ResponseEntity<String> confirmEmail(@RequestParam String token) {
        userService.confirmUser(token);

        return new ResponseEntity<>("Your account is verified!", HttpStatus.OK);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity passwordForgot(@RequestBody EmailDTO email,
                                         HttpServletRequest request) throws Exception {

        User user = userService.getUserByEmail(email.getEmail());
        userService.checkUserExist(user);
        emailService.sendResetPasswordMessage(user, request);

        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/change-password")
    public ResponseEntity passwordReset(@RequestBody @Valid PasswordResetDTO passwordResetDTO) {
        passwordService.passwordReset(passwordResetDTO);

        return new ResponseEntity(HttpStatus.OK);

    }

    @GetMapping("/validate-reset-token")
    public ResponseEntity displayResetPasswordPage(@RequestParam String token) {
        tokenService.checkToken(token);

        return new ResponseEntity<>(token, HttpStatus.OK);
    }

}
