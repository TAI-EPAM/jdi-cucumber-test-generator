package com.epam.test_generator.controllers;

import com.epam.test_generator.dto.LoginUserDTO;
import com.epam.test_generator.dto.TokenDTO;
import com.epam.test_generator.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@RestController
public class LoginController {

    @Autowired
    private TokenService tokenService;

    @RequestMapping(value = "/login", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity<TokenDTO> login(@RequestBody @Valid LoginUserDTO userDTO)
        throws Exception {

        final String token = tokenService.getToken(userDTO);

        return new ResponseEntity<>(new TokenDTO(token), HttpStatus.OK);
    }
}
