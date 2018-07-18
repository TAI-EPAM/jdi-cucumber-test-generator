package com.epam.admin_client.service;

import com.epam.test_generator.controllers.user.request.LoginUserDTO;
import com.epam.test_generator.dto.TokenDTO;
import org.springframework.security.core.AuthenticationException;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class LoginService {


    private final String uriLogin;

    private RestTemplate restTemplate;

    public LoginService(RestTemplate restTemplate, Environment environment) {
        this.restTemplate = restTemplate;
        this.uriLogin = environment.getRequiredProperty("login.api.url");
    }


    public String authenticate(String email, String password) throws AuthenticationException {
        LoginUserDTO loginUserDTO = new LoginUserDTO();
        loginUserDTO.setEmail(email);
        loginUserDTO.setPassword(password);
        try {
            ResponseEntity<TokenDTO> responseEntity = restTemplate.postForEntity
                (uriLogin, loginUserDTO, TokenDTO.class);
            TokenDTO body = responseEntity.getBody();
            if (null != body) {
                return body.getToken();
            }
        } catch (HttpClientErrorException e) {
            throw new BadCredentialsException("Wrong credential");
        }
        throw new InternalAuthenticationServiceException("Remote server returned empty body");
    }
}
