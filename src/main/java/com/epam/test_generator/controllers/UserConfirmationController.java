package com.epam.test_generator.controllers;

import com.epam.test_generator.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserConfirmationController {

    @Autowired
    private UserService userService;

    @GetMapping("/confirmAccount")
    public ResponseEntity<String> displayResetPasswordPage(@RequestParam String token) {
        userService.confirmUser(token);

        return new ResponseEntity<>("Your account is verified!", HttpStatus.OK);
    }
}
