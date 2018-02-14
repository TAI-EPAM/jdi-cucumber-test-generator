package com.epam.test_generator.controllers;

import com.epam.test_generator.dto.LoginUserDTO;
import com.epam.test_generator.dto.TokenDTO;
import com.epam.test_generator.services.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;


/**
 * Controls login process.
 */
@RestController
public class LoginController {


    @Autowired
    private LoginService loginService;

    @RequestMapping(value = "/login", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity<TokenDTO> login(@RequestBody @Valid LoginUserDTO userDTO, HttpServletRequest request)
        throws Exception {
        loginService.checkPassword(userDTO, request);
        final String token = loginService.getLoginJWTToken(userDTO);

        return new ResponseEntity<>(new TokenDTO(token), HttpStatus.OK);
    }
}
