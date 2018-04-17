package com.epam.test_generator.controllers;

import com.epam.test_generator.dto.LoginUserDTO;
import com.epam.test_generator.dto.TokenDTO;
import com.epam.test_generator.services.LoginService;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


/**
 * Controls login process.
 */
@RestController
public class LoginController {


    @Autowired
    private LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<TokenDTO> login
        (@RequestBody @Valid LoginUserDTO userDTO, HttpServletRequest request) {
        loginService.checkPassword(userDTO, request);
        String token = loginService.getLoginJWTToken(userDTO);

        return new ResponseEntity<>(new TokenDTO(token), HttpStatus.OK);
    }
}
