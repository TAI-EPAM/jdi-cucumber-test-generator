package com.epam.test_generator.controllers;

import com.epam.test_generator.dto.PasswordResetDTO;
import com.epam.test_generator.services.PasswordService;
import com.epam.test_generator.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class PasswordResetController {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private PasswordService passwordService;

    @RequestMapping(value = "/passwordReset", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity passwordReset(@RequestBody @Valid PasswordResetDTO passwordResetDTO) {
        passwordService.passwordReset(passwordResetDTO);

        return new ResponseEntity(HttpStatus.OK);

    }

    @GetMapping("/passwordReset")
    public ResponseEntity displayResetPasswordPage(@RequestParam String token) {
        tokenService.checkToken(token);

        return new ResponseEntity<>(token, HttpStatus.OK);
    }
}
